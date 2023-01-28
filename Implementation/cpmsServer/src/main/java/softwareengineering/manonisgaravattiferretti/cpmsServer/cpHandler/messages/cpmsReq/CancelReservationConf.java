package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.CommandResult;

@Data
public class CancelReservationConf implements ConfMessage {
    @NotNull
    private CommandResult commandResult;
}
