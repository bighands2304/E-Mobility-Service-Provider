package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.DSOOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.DSOManager;
import softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events.DSOOptimizerEvent;

@Component
public class DSOOptimizerEventListener implements ApplicationListener<DSOOptimizerEvent> {
    private final DSOManager dsoManager;
    private final Logger logger = LoggerFactory.getLogger(DSOOptimizerEventListener.class);

    @Autowired
    public DSOOptimizerEventListener(DSOManager dsoManager) {
        this.dsoManager = dsoManager;
    }

    @Override
    public void onApplicationEvent(DSOOptimizerEvent event) {
        logger.info("DSO Optimizer event for cp with id = " + event.getDsoOffer().getChargingPointId());
        DSOOffer dsoOffer = event.getDsoOffer();
        dsoManager.changeDsoProvider(dsoOffer.getChargingPointId(), dsoOffer, dsoOffer.getAvailableTimeSlot());
    }
}
