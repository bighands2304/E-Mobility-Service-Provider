package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.IncludeBatteryDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.EnergyChangeEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleEnergyMixOptimizerEvent;

@Service
public class EnergyMixManager implements ApplicationListener<ToggleEnergyMixOptimizerEvent> {
    private final ChargingPointService chargingPointService;
    private final EnergyMixOptimizer energyMixOptimizer;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public EnergyMixManager(ChargingPointService chargingPointService, EnergyMixOptimizer energyMixOptimizer,
                            ApplicationEventPublisher applicationEventPublisher) {
        this.chargingPointService = chargingPointService;
        this.energyMixOptimizer = energyMixOptimizer;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    public void onApplicationEvent(ToggleEnergyMixOptimizerEvent event) {
        chargingPointService.updateToggleOptimizer(event.getCpId(), "EnergyMix", event.isAutomatic());
        energyMixOptimizer.switchOptimizer(event.getCpId(), event.isAutomatic());
    }

    public boolean includeBattery(IncludeBatteryDTO includeBatteryDTO, String cpId, Integer batteryId) {
        if (includeBatteryDTO.getMinLevel() > includeBatteryDTO.getMaxLevel()) {
            return false;
        }
        chargingPointService.updateIncludeBattery(includeBatteryDTO, cpId, batteryId);
        chargingPointService.updateToggleOptimizer(cpId, "EnergyMix", false);
        energyMixOptimizer.switchOptimizer(cpId, false);
        EnergyChangeEvent energyChangeEvent = new EnergyChangeEvent(this, cpId);
        applicationEventPublisher.publishEvent(energyChangeEvent);
        return true;
    }
}
