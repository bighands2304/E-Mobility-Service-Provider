package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq;

import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.dtos.MeterValue;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StopTransactionReq {
    private Long transactionId;
    private LocalDateTime timestamp;
    private List<MeterValue> transactionData;
}
