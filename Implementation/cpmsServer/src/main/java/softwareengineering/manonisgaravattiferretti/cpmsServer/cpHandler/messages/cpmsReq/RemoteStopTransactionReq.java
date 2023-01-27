package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RemoteStopTransactionReq {
    private Long transactionId;
}
