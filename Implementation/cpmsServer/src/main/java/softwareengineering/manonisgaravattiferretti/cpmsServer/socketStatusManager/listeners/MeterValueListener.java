package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.context.ApplicationListener;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.MeterValueEvent;

public class MeterValueListener implements ApplicationListener<MeterValueEvent> {
    @Override
    public void onApplicationEvent(MeterValueEvent event) {

    }
}
