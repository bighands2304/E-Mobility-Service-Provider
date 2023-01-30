package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events;

import org.springframework.context.ApplicationEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;

public class DSOOptimizerEvent extends ApplicationEvent {
    private final DSOOffer dsoOffer;

    public DSOOptimizerEvent(Object source, DSOOffer dsoOffer) {
        super(source);
        this.dsoOffer = dsoOffer;
    }

    public DSOOffer getDsoOffer() {
        return dsoOffer;
    }
}
