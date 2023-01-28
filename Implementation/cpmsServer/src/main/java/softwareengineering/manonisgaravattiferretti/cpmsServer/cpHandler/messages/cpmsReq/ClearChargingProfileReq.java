package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClearChargingProfileReq {
    private Integer id; // optional (the id of the charging profile)
    private Integer connectorId;
}
