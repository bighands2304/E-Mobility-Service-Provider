package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.oscpDtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EnergyMeasurementDTO {
    private Double value;
    private String unit;
    private EnergyFlowDirection direction;
    private LocalDateTime measureTime;
}
