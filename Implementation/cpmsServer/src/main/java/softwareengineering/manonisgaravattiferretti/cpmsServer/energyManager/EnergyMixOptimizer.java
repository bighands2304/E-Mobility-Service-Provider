package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.IncludeBatteryDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Battery;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.DSOOfferService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service.EnergyConsumptionService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.EnergyMixOptimizerEvent;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EnergyMixOptimizer {
    private final Map<String, Boolean> optimizerSet = new ConcurrentHashMap<>();
    private final ChargingPointService chargingPointService;
    private final EnergyConsumptionService energyConsumptionService;
    private final DSOOfferService dsoOfferService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Logger logger = LoggerFactory.getLogger(EnergyMixOptimizer.class);

    @Autowired
    public EnergyMixOptimizer(ChargingPointService chargingPointService,
                              EnergyConsumptionService energyConsumptionService,
                              DSOOfferService dsoOfferService, ApplicationEventPublisher applicationEventPublisher) {
        this.chargingPointService = chargingPointService;
        this.energyConsumptionService = energyConsumptionService;
        this.dsoOfferService = dsoOfferService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(fixedRate = 14400000)
    public void optimize() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minus(1, ChronoUnit.WEEKS);
        for (String cpId: optimizerSet.keySet()) {
            if (optimizerSet.get(cpId)) {
                optimizeCp(cpId, oneWeekAgo, now);
            }
        }
    }

    @Async
    void optimizeCp(String cpId, LocalDateTime oneWeekAgo, LocalDateTime now) {
        logger.info("Starting energy mix optimization process for cp with id = " + cpId);
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(cpId);
        if (chargingPointOptional.isEmpty() || chargingPointOptional.get().getBatteries().size() == 0) return;
        List<Battery> batteries = chargingPointOptional.get().getBatteries();
        double meanConsumption = energyConsumptionService.findMeanConsumption(chargingPointOptional.get().getCpId(), oneWeekAgo, now);
        LocalTime currentTime = LocalTime.now();
        List<DSOOffer> currentOffers = dsoOfferService.findCurrentCpOffers(cpId)
                .stream().filter(dsoOffer -> dsoOffer.getAvailableTimeSlot().getStartTime().isBefore(currentTime) &&
                        dsoOffer.getAvailableTimeSlot().getEndTime().isAfter(currentTime)).toList();
        Optional<DSOOffer> bestConsumptionOffer = currentOffers.stream().reduce((offer1, offer2) ->
                (offer1.getCapacity() < offer2.getCapacity()) ? offer2 : offer1);
        if (bestConsumptionOffer.isEmpty()) return;
        double notFulfilledConsumption = meanConsumption - bestConsumptionOffer.get().getCapacity();
        if (notFulfilledConsumption < 0.0) return;
        double batteriesPercent = notFulfilledConsumption / (meanConsumption * batteries.size());
        for (Battery battery: batteries) {
            IncludeBatteryDTO includeBatteryDTO = new IncludeBatteryDTO();
            includeBatteryDTO.setPercent(batteriesPercent);
            includeBatteryDTO.setMinLevel(10.0);
            includeBatteryDTO.setMaxLevel(90.0);
            EnergyMixOptimizerEvent event = new EnergyMixOptimizerEvent(this, includeBatteryDTO, cpId, battery.getBatteryId());
            applicationEventPublisher.publishEvent(event);
        }
        logger.info("Optimized cp with id = {} with batteryPercent = {}", cpId, batteriesPercent);
    }

    @Scheduled(fixedRate = 600000000)
    public void fetchChargingPointsPeriodically() {
        List<ChargingPoint> chargingPoints = chargingPointService.findAll();
        for (ChargingPoint chargingPoint: chargingPoints) {
            optimizerSet.put(chargingPoint.getId(), chargingPoint.isToggleEnergyMixOptimizer());
        }
    }

    public void switchOptimizer(String cpId, boolean toggle) {
        optimizerSet.put(cpId, toggle);
    }
}
