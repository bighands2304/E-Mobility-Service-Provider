package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.AvailabilityType;

@Data
@AllArgsConstructor
public class ChangeAvailabilityReq {
    private Integer connectorId;
    private AvailabilityType availabilityType;
}


