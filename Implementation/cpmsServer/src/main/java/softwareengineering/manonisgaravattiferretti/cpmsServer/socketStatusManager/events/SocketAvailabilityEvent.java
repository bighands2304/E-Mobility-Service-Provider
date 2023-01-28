package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events;

import org.springframework.context.ApplicationEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChangeSocketAvailabilityDTO;

public class SocketAvailabilityEvent extends ApplicationEvent {
    private ChangeSocketAvailabilityDTO changeSocketAvailabilityDTO;
    private String cpId;
    private Integer socketId;

    public SocketAvailabilityEvent(Object source, ChangeSocketAvailabilityDTO changeSocketAvailabilityDTO,
                                   String cpId, Integer socketId) {
        super(source);
        this.changeSocketAvailabilityDTO = changeSocketAvailabilityDTO;
        this.cpId = cpId;
        this.socketId = socketId;
    }

    public ChangeSocketAvailabilityDTO getChangeSocketAvailabilityDTO() {
        return changeSocketAvailabilityDTO;
    }

    public String getCpId() {
        return cpId;
    }

    public Integer getSocketId() {
        return socketId;
    }
}
