package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingProfile;

@Data
@AllArgsConstructor
public class SetChargingProfileReq {
    private Integer connectorId;
    private ChargingProfile chargingProfile;
}
