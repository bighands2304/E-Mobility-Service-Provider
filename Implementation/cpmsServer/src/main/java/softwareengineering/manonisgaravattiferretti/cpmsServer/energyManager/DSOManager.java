package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.OfferTimeSlot;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.EnergyChangeEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleDsoSelectionOptimizerEvent;

import java.util.Optional;

@Service
public class DSOManager implements ApplicationListener<ToggleDsoSelectionOptimizerEvent> {
    private final ChargingPointService chargingPointService;
    private final DSOSelectionOptimizer dsoSelectionOptimizer;
    private final DSOOfferService dsoOfferService;
    private final ApplicationEventPublisher applicationEventPublisher;

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

    public boolean changeDsoProviderManual(String cpId, String offerId, OfferTimeSlot offerTimeSlot) {
        Optional<DSOOffer> dsoOfferOptional = dsoOfferService.findOfferById(offerId);
        if (dsoOfferOptional.isEmpty() || !dsoOfferOptional.get().getChargingPointId().equals(cpId)) {
            return false;
        }
        dsoSelectionOptimizer.switchOptimizer(cpId, false);
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
}
