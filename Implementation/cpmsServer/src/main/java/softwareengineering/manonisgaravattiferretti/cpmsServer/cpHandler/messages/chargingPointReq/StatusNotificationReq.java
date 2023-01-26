package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class StatusNotificationReq {
    @NotNull
    @Min(0)
    private Integer connectorId;
    @NotNull
    private String errorCode; // "NoError" if there isn't any error
    @NotNull
    private String status;
    private LocalDateTime timestamp;
}
