package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.ToggleDsoSelectionOptimizerEvent;

@Service
public class DSOManager implements ApplicationListener<ToggleDsoSelectionOptimizerEvent> {
    private final ChargingPointService chargingPointService;
    private final DSOSelectionOptimizer dsoSelectionOptimizer;

    @Autowired
    public DSOManager(ChargingPointService chargingPointService, DSOSelectionOptimizer dsoSelectionOptimizer) {
        this.chargingPointService = chargingPointService;
        this.dsoSelectionOptimizer = dsoSelectionOptimizer;
    }

    @Override
    public void onApplicationEvent(ToggleDsoSelectionOptimizerEvent event) {
        chargingPointService.updateToggleOptimizer(event.getCpId(), "DsoSelection", event.isAutomatic());
        dsoSelectionOptimizer.switchOptimizer(event.getCpId(), event.isAutomatic());
    }


}
