package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.context.ApplicationListener;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SessionStartedEvent;

public class SessionStartedListener implements ApplicationListener<SessionStartedEvent> {
    @Override
    public void onApplicationEvent(SessionStartedEvent event) {

    }
}
