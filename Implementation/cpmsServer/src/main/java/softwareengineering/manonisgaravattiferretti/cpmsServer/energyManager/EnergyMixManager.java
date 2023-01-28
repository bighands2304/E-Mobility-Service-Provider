package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleEnergyMixOptimizerEvent;

@Service
public class EnergyMixManager implements ApplicationListener<ToggleEnergyMixOptimizerEvent> {
    private final ChargingPointService chargingPointService;

    @Autowired
    public EnergyMixManager(ChargingPointService chargingPointService) {
        this.chargingPointService = chargingPointService;
    }


    @Override
    public void onApplicationEvent(ToggleEnergyMixOptimizerEvent event) {
        chargingPointService.updateToggleOptimizer(event.getCpId(), "EnergyMix", event.isAutomatic());

    }
}
