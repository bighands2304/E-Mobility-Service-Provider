package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingProfile;

@Data
public class SetChargingProfileReq {
    private Integer connectorId;
    private ChargingProfile chargingProfile;
}
