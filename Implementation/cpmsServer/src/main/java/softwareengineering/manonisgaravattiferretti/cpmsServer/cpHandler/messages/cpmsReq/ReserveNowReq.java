package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReserveNowReq {
    private Integer connectorId;
    private LocalDateTime expiryDate;
    private Integer reservationId;
}
