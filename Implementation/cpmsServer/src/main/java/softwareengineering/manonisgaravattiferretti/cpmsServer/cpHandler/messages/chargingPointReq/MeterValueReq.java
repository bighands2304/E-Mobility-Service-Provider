package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.dtos.MeterValue;

import java.util.List;

@Data
public class MeterValueReq {
    @NotNull
    private Integer connectorId;
    private Long transactionId;
    @NotEmpty
    private List<MeterValue> meterValue;
}
