package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingProfile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoteStartTransactionReq {
    private Integer connectorId;
    private ChargingProfile chargingProfile;
}
