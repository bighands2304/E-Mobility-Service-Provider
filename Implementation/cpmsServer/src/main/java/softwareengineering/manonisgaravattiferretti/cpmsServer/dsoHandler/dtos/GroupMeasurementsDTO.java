package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupMeasurementsDTO {
    private String id;
    private List<EnergyMeasurementDTO> measurements;
}
