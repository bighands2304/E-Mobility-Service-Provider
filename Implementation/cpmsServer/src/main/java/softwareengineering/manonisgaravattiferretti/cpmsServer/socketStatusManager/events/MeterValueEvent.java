package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events;

import org.springframework.context.ApplicationEvent;

public class MeterValueEvent extends ApplicationEvent {
    public MeterValueEvent(Object source) {
        super(source);
    }
}
