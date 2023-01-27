package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StartTransactionReq {
    private Integer connectorId;
    private Long reservationId;
    private LocalDateTime timestamp;
}
