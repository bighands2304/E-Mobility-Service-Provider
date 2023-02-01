package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events;

import org.springframework.context.ApplicationEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.dtos.MeterValue;

import java.time.LocalDateTime;
import java.util.List;

public class SessionStoppedEvent extends ApplicationEvent {
    private final Long reservationId;
    private final LocalDateTime time;
    private final List<MeterValue> meterValues;

    public SessionStoppedEvent(Object source, Long reservationId, LocalDateTime time, List<MeterValue> meterValues) {
        super(source);
        this.reservationId = reservationId;
        this.time = time;
        this.meterValues = meterValues;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public List<MeterValue> getMeterValues() {
        return meterValues;
    }
}
