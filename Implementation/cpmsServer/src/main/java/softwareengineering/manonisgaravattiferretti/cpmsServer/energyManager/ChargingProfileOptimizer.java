package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.EnergyChangeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChargingProfileOptimizer implements ApplicationListener<EnergyChangeEvent> {
    private final Map<String, Boolean> optimizerSet = new ConcurrentHashMap<>();
    private final ChargingPointService chargingPointService;

    @Autowired
    public ChargingProfileOptimizer(ChargingPointService chargingPointService) {
        this.chargingPointService = chargingPointService;
    }

    @Override
    public void onApplicationEvent(EnergyChangeEvent event) {
        //todo: optimize charging profiles
    }
}
