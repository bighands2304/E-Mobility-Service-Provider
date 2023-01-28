package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class SessionStartedEvent extends ApplicationEvent {
    private final Long reservationId;
    private final Long sessionId;
    private final LocalDateTime time;
    private final String cpId;
    private final Integer socketId;

    public SessionStartedEvent(Object source, Long reservationId, Long sessionId, LocalDateTime localDateTime,
                               String cpId, Integer socketId) {
        super(source);
        this.sessionId = sessionId;
        this.reservationId = reservationId;
        this.cpId = cpId;
        this.time = localDateTime;
        this.socketId = socketId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getCpId() {
        return cpId;
    }

    public Integer getSocketId() {
        return socketId;
    }
}
