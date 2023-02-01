package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.EnergyMixManager;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.EnergyMixOptimizerEvent;

@Component
public class EnergyMixOptimizerEventListener implements ApplicationListener<EnergyMixOptimizerEvent> {
    private final EnergyMixManager energyMixManager;

    @Autowired
    public EnergyMixOptimizerEventListener(EnergyMixManager energyMixManager) {
        this.energyMixManager = energyMixManager;
    }

    @Override
    public void onApplicationEvent(EnergyMixOptimizerEvent event) {
        energyMixManager.includeBattery(event.getIncludeBatteryDTO(), event.getCpId(), event.getBatteryId());
    }
}
