package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events;

import org.springframework.context.ApplicationEvent;

public class SessionStoppedEvent extends ApplicationEvent {
    public SessionStoppedEvent(Object source) {
        super(source);
    }
}
