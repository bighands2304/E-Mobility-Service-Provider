package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events;

import org.springframework.context.ApplicationEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.dtos.MeterValue;

import java.util.List;

public class MeterValueEvent extends ApplicationEvent {
    private final Long sessionId;
    private final Integer socketId;
    private final List<MeterValue> meterValues;

    public MeterValueEvent(Object source, Long sessionId, Integer socketId, List<MeterValue> meterValues) {
        super(source);
        this.sessionId = sessionId;
        this.socketId = socketId;
        this.meterValues = meterValues;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Integer getSocketId() {
        return socketId;
    }

    public List<MeterValue> getMeterValues() {
        return meterValues;
    }
}
