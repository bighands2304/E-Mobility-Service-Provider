package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events;

import org.springframework.context.ApplicationEvent;

public class ToggleEnergyMixOptimizerEvent extends ApplicationEvent {
    private final String cpId;
    private final boolean automatic;

    public ToggleEnergyMixOptimizerEvent(Object source, String cpId, boolean automatic) {
        super(source);
        this.automatic = automatic;
        this.cpId = cpId;
    }

    public String getCpId() {
        return cpId;
    }

    public boolean isAutomatic() {
        return automatic;
    }
}
