package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events;

import org.springframework.context.ApplicationEvent;

public class EnergyChangeEvent extends ApplicationEvent {
    private final String cpId;

    public EnergyChangeEvent(Object source, String cpId) {
        super(source);
        this.cpId = cpId;
    }

    public String getCpId() {
        return cpId;
    }
}
