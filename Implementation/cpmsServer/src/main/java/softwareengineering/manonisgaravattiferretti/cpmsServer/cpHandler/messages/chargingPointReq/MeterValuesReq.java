package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.dtos.MeterValue;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeterValuesReq {
    @NotNull
    private Integer connectorId;
    private Integer transactionId;
    @NotEmpty
    private List<MeterValue> meterValue;
}
