package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.OfferTimeSlot;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service.EnergyConsumptionService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.DSOOptimizerEvent;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DSOSelectionOptimizer {
    private final Map<String, Boolean> optimizerSet = new ConcurrentHashMap<>();
    private final ChargingPointService chargingPointService;
    private final DSOOfferService dsoOfferService;
    private final EnergyConsumptionService energyConsumptionService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Logger logger = LoggerFactory.getLogger(DSOSelectionOptimizer.class);

    @Autowired
    public DSOSelectionOptimizer(ChargingPointService chargingPointService,
                                 DSOOfferService dsoOfferService,
                                 EnergyConsumptionService energyConsumptionService,
                                 ApplicationEventPublisher applicationEventPublisher) {
        this.chargingPointService = chargingPointService;
        this.dsoOfferService = dsoOfferService;
        this.energyConsumptionService = energyConsumptionService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(fixedRate = 14400000)
    public void optimize() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minus(1, ChronoUnit.WEEKS);
        for (String cpId: optimizerSet.keySet()) {
            if (optimizerSet.get(cpId)) {
                optimizeCp(cpId, now, oneWeekAgo);
            }
        }
    }

    @Async
    void optimizeCp(String cpId, LocalDateTime now, LocalDateTime oneWeekAgo) {
        logger.info("Optimizing Dso selection for cp with id = " + cpId);
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(cpId);
        if (chargingPointOptional.isEmpty()) return;
        double meanConsumption = energyConsumptionService.findMeanConsumption(chargingPointOptional.get().getCpId(), oneWeekAgo, now);
        List<DSOOffer> dsoOffers = dsoOfferService.findOffersOfCp(cpId);
        if (dsoOffers.size() <= 1) return;
        List<OfferTimeSlot> timeSlots = dsoOffers.stream().map(DSOOffer::getAvailableTimeSlot).distinct().toList();
        for (OfferTimeSlot offerTimeSlot: timeSlots) {
            List<DSOOffer> offers = dsoOffers.stream().filter(offer -> offer.getCapacity() >= meanConsumption)
                    .filter(dsoOffer -> dsoOffer.getAvailableTimeSlot().equals(offerTimeSlot)).toList();
            Optional<DSOOffer> bestOffer = offers.stream().reduce((a, b) -> (a.getPrice() < b.getPrice()) ? a : b);
            bestOffer.ifPresentOrElse(offer -> {
                if (!offer.isInUse()) {
                    DSOOptimizerEvent dsoOptimizerEvent = new DSOOptimizerEvent(this, offer);
                    applicationEventPublisher.publishEvent(dsoOptimizerEvent);
                }
            }, () -> {
                Optional<DSOOffer> best = dsoOffers.stream().filter(dsoOffer -> dsoOffer.getAvailableTimeSlot().equals(offerTimeSlot))
                        .reduce((a, b) -> (a.getPrice() <= b.getPrice()) ? a : b);
                best.ifPresent(offer -> {
                    if (!offer.isInUse()) {
                        DSOOptimizerEvent dsoOptimizerEvent = new DSOOptimizerEvent(this, offer);
                        applicationEventPublisher.publishEvent(dsoOptimizerEvent);
                    }
                });
            });
        }
    }

    @Scheduled(fixedRate = 600000000)
    public void fetchChargingPointsPeriodically() {
        List<ChargingPoint> chargingPoints = chargingPointService.findAll();
        for (ChargingPoint chargingPoint: chargingPoints) {
            optimizerSet.put(chargingPoint.getId(), chargingPoint.isToggleDSOSelectionOptimizer());
        }
    }

    public void switchOptimizer(String cpId, boolean toggle) {
        optimizerSet.put(cpId, toggle);
    }
}
