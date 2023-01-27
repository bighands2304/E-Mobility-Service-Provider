package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelReservationConf implements ConfMessage {
    @NotNull
    private String status;
}
