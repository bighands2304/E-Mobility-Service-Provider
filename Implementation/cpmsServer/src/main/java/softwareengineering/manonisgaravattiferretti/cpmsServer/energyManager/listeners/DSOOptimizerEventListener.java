package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.listeners;

import org.springframework.context.ApplicationListener;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.DSOManager;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.DSOOptimizerEvent;

public class DSOOptimizerEventListener implements ApplicationListener<DSOOptimizerEvent> {
    private final DSOManager dsoManager;

    public DSOOptimizerEventListener(DSOManager dsoManager) {
        this.dsoManager = dsoManager;
    }

    @Override
    public void onApplicationEvent(DSOOptimizerEvent event) {
        DSOOffer dsoOffer = event.getDsoOffer();
        dsoManager.changeDsoProvider(dsoOffer.getChargingPointInternalId(), dsoOffer, dsoOffer.getAvailableTimeSlot());
    }
}
