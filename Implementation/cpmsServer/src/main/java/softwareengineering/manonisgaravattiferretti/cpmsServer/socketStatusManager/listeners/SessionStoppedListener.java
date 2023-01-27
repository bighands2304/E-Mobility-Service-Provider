package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.context.ApplicationListener;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SessionStoppedEvent;

public class SessionStoppedListener implements ApplicationListener<SessionStoppedEvent> {
    @Override
    public void onApplicationEvent(SessionStoppedEvent event) {

    }
}
