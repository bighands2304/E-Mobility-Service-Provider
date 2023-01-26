package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeterValue {
    @NotNull
    private LocalDateTime timestamp;
    @NotNull
    private String sampledValue;
}
