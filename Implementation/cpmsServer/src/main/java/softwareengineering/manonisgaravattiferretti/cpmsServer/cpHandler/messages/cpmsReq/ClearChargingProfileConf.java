package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.CommandResult;

@Data
public class ClearChargingProfileConf implements ConfMessage {
    private CommandResult commandResult;
    private String requestId;
}
