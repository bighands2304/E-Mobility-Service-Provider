package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class SocketStatusChangeEvent extends ApplicationEvent {
    private final String cpId;
    private final Integer socketId;
    private final String newStatus;
    private final LocalDateTime timestamp;

    public SocketStatusChangeEvent(Object source, String cpId, Integer socketId, String newStatus, LocalDateTime timestamp) {
        super(source);
        this.cpId = cpId;
        this.socketId = socketId;
        this.newStatus = newStatus;
        this.timestamp = timestamp;
    }

    public String getCpId() {
        return cpId;
    }

    public Integer getSocketId() {
        return socketId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public LocalDateTime getEventTimestamp() {
        return timestamp;
    }
}
