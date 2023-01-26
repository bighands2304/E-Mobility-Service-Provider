package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReserveNowReq {
    private Integer connectorId;
    private LocalDateTime expiryDate;
    private Integer reservationId;
}
