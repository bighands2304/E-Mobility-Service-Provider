package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events;

import org.springframework.context.ApplicationEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.dtos.MeterValue;

import java.util.List;

public class MeterValueEvent extends ApplicationEvent {
    private final Long reservationId;
    private final Integer socketId;
    private final List<MeterValue> meterValues;
    private final Double batteryPercentage;

    public MeterValueEvent(Object source, Long reservationId, Integer socketId,
                           List<MeterValue> meterValues, Double batteryPercentage) {
        super(source);
        this.reservationId = reservationId;
        this.socketId = socketId;
        this.meterValues = meterValues;
        this.batteryPercentage = batteryPercentage;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Integer getSocketId() {
        return socketId;
    }

    public List<MeterValue> getMeterValues() {
        return meterValues;
    }

    public Double getBatteryPercentage() {
        return batteryPercentage;
    }
}
