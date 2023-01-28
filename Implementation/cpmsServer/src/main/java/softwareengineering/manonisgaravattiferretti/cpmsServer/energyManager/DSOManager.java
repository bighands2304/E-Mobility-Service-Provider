package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleDsoSelectionOptimizerEvent;

@Service
public class DSOManager implements ApplicationListener<ToggleDsoSelectionOptimizerEvent> {
    private final ChargingPointService chargingPointService;

    @Autowired
    public DSOManager(ChargingPointService chargingPointService) {
        this.chargingPointService = chargingPointService;
    }

    @Override
    public void onApplicationEvent(ToggleDsoSelectionOptimizerEvent event) {
        chargingPointService.updateToggleOptimizer(event.getCpId(), "DsoSelection", event.isAutomatic());
        // todo
    }
}
