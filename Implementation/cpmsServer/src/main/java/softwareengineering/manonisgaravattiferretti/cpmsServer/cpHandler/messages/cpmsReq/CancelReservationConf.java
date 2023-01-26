package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelReservationConf {
    @NotNull
    private String status;
}
