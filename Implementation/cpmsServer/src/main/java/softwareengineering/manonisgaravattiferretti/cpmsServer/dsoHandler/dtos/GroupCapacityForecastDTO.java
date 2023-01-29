package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupCapacityForecastDTO {
    private String id;
    private CapacityForecastedType capacityForecastedType;
    private List<ForecastedBlockDTO> forecastedBlocks;
}
