package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.OfferTimeSlot;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.openAdrDtos.PricingTimeSlotDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.EnergyChangeEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleDsoSelectionOptimizerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DSOManager implements ApplicationListener<ToggleDsoSelectionOptimizerEvent> {
    private final ChargingPointService chargingPointService;
    private final DSOSelectionOptimizer dsoSelectionOptimizer;
    private final DSOOfferService dsoOfferService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Logger logger = LoggerFactory.getLogger(DSOManager.class);

    @Autowired
    public DSOManager(ChargingPointService chargingPointService, DSOSelectionOptimizer dsoSelectionOptimizer,
                      DSOOfferService dsoOfferService, ApplicationEventPublisher applicationEventPublisher) {
        this.chargingPointService = chargingPointService;
        this.dsoSelectionOptimizer = dsoSelectionOptimizer;
        this.dsoOfferService = dsoOfferService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onApplicationEvent(ToggleDsoSelectionOptimizerEvent event) {
        chargingPointService.updateToggleOptimizer(event.getCpId(), "DsoSelection", event.isAutomatic());
        dsoSelectionOptimizer.switchOptimizer(event.getCpId(), event.isAutomatic());
    }

    public boolean changeDsoProviderManual(String cpInternalId, String cpId, String offerId, OfferTimeSlot offerTimeSlot) {
        Optional<DSOOffer> dsoOfferOptional = dsoOfferService.findOfferById(offerId);
        if (dsoOfferOptional.isEmpty() || !dsoOfferOptional.get().getChargingPointId().equals(cpId) ||
                dsoOfferOptional.get().getAvailableTimeSlot().getStartTime().isAfter(offerTimeSlot.getStartTime()) ||
                dsoOfferOptional.get().getAvailableTimeSlot().getEndTime().isBefore(offerTimeSlot.getEndTime())) {
            return false;
        }
        dsoSelectionOptimizer.switchOptimizer(cpInternalId, false);
        DSOOffer dsoOffer = dsoOfferOptional.get();
        if (dsoOffer.isInUse()) {
            return true;
        }
        changeDsoProvider(cpId, dsoOffer, offerTimeSlot);
        return true;
    }

    public void changeDsoProvider(String cpId, DSOOffer dsoOffer, OfferTimeSlot offerTimeSlot) {
        Optional<DSOOffer> currentOffer = dsoOfferService.findDSOOfferFromCpAndTimeSlot(cpId,
                dsoOffer.getAvailableTimeSlot(), true);
        currentOffer.ifPresent(currentOff -> {
            currentOff.setInUse(false);
            dsoOfferService.insertOffer(currentOff);
        });
        dsoOffer.setInUse(true);
        dsoOffer.setUsedTimeSlot(offerTimeSlot);
        dsoOfferService.insertOffer(dsoOffer);
        EnergyChangeEvent energyChangeEvent = new EnergyChangeEvent(this, cpId);
        applicationEventPublisher.publishEvent(energyChangeEvent);
    }

    public void refactorOffers(String cpId, String dsoToken, List<DSOOffer> currentOffers,
                               List<PricingTimeSlotDTO> pricingTimeSlots) {
        if (currentOffers.size() == 0) {
            return;
        }
        logger.info("updating offers of dso " + currentOffers.get(0).getCompanyName() + " for cp " + cpId);
        int offersUpdated = 0;
        List<DSOOffer> validOffers = currentOffers.stream().filter(DSOOffer::isValid).toList();
        if (validOffers.size() == pricingTimeSlots.size()) {
            for (PricingTimeSlotDTO pricingTimeSlotDTO: pricingTimeSlots) {
                boolean updated = false;
                for (int i = 0; i < currentOffers.size() && !updated; i++) {
                    OfferTimeSlot offerTimeSlot = currentOffers.get(i).getAvailableTimeSlot();
                    if (offerTimeSlot.getStartTime().equals(pricingTimeSlotDTO.getStartTime()) &&
                            offerTimeSlot.getEndTime().equals(pricingTimeSlotDTO.getEndTime())) {
                        dsoOfferService.updateOfferById(currentOffers.get(i).getId(), pricingTimeSlotDTO.getPrice());
                        offersUpdated++;
                    }
                }
            }
        }
        if (offersUpdated == pricingTimeSlots.size()) {
            return;
        }
        // if here current offer was not valid (created temporarily until the timeslots arrived)
        dsoOfferService.removeOffer(currentOffers);
        DSOOffer dsoOfferOld = currentOffers.get(0);
        List<DSOOffer> dsoOffers = new ArrayList<>();
        for (PricingTimeSlotDTO pricingTimeSlotDTO: pricingTimeSlots) {
            DSOOffer dsoOffer = new DSOOffer();
            BeanUtils.copyProperties(dsoOfferOld, dsoOffer);
            dsoOffer.setPrice(pricingTimeSlotDTO.getPrice());
            dsoOffer.setAvailableTimeSlot(new OfferTimeSlot(pricingTimeSlotDTO.getStartTime(), pricingTimeSlotDTO.getEndTime()));
            dsoOffer.setInUse(false);
            dsoOffer.setValid(true);
            dsoOffer.setId(null);
            dsoOffers.add(dsoOffer);
        }
        dsoOfferService.insertAll(dsoOffers);
    }
}
