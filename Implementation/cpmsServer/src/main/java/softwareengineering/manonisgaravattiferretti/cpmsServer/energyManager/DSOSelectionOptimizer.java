package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos.MeanConsumption;
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

    @Scheduled(fixedRate = 60000000)
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
        double meanConsumption = energyConsumptionService.findMeanConsumption(cpId, oneWeekAgo, now);
        List<DSOOffer> dsoOffers = dsoOfferService.findOffersOfCp(cpId);
        if (dsoOffers.size() <= 1) {
            return;
        }
        List<String> dsoIds = dsoOffers.stream().map(DSOOffer::getDsoId).distinct().toList();
        List<DSOOffer> bestDsoOffers = new ArrayList<>();
        for (String dsoId: dsoIds) {
            Optional<DSOOffer> bestOffer = dsoOffers.stream()
                    .filter(dsoOffer -> dsoOffer.getCapacity() > meanConsumption)
                    .reduce((a, b) -> (a.getPrice() > b.getPrice()) ? b : a);
            bestOffer.ifPresent(bestDsoOffers::add);
        }
        Optional<DSOOffer> bestGlobalOffer = bestDsoOffers.stream().reduce((a, b) -> (a.getPrice() > b.getPrice()) ? b : a);
        bestGlobalOffer.ifPresent(bestOffer -> {
            if (!bestOffer.isInUse()) {
                DSOOptimizerEvent dsoOptimizerEvent = new DSOOptimizerEvent(this, bestOffer);
                applicationEventPublisher.publishEvent(dsoOptimizerEvent);
            }
        });
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
