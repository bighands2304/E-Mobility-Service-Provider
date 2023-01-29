package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.events;

import org.springframework.context.ApplicationEvent;

public class TogglePriceOptimizerEvent extends ApplicationEvent {
    private final boolean automatic;
    private final String cpId;

    public TogglePriceOptimizerEvent(Object source, boolean automatic, String cpId) {
        super(source);
        this.automatic = automatic;
        this.cpId = cpId;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public String getCpId() {
        return cpId;
    }
}
