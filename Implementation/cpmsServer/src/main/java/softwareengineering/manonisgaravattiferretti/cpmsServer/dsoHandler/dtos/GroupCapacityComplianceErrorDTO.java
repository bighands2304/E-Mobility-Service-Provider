package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupCapacityComplianceErrorDTO {
    private String message;
    private List<ForecastedBlockDTO> forecastedBlocks;
}
