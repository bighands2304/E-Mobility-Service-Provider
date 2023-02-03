package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.oscpDtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ForecastedBlockDTO {
    private Double capacity;
    private String unit;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
