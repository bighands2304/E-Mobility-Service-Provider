package softwareengineering.manonisgaravattiferretti.cpmsServer.energyManager.events;

import org.springframework.context.ApplicationEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.IncludeBatteryDTO;

public class EnergyMixOptimizerEvent extends ApplicationEvent {
    private final IncludeBatteryDTO includeBatteryDTO;
    private final String cpId;
    private final Integer batteryId;

    public EnergyMixOptimizerEvent(Object source, IncludeBatteryDTO includeBatteryDTO, String cpId, Integer batteryId) {
        super(source);
        this.includeBatteryDTO = includeBatteryDTO;
        this.cpId = cpId;
        this.batteryId = batteryId;
    }

    public IncludeBatteryDTO getIncludeBatteryDTO() {
        return includeBatteryDTO;
    }

    public String getCpId() {
        return cpId;
    }

    public Integer getBatteryId() {
        return batteryId;
    }
}
