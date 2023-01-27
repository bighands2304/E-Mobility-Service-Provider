package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events;

import org.springframework.context.ApplicationEvent;

public class SessionStartedEvent extends ApplicationEvent {
    public SessionStartedEvent(Object source) {
        super(source);
    }
}
