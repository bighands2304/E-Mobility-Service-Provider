package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IncludeBatteryDTO {
    @NotBlank
    private String cpId;
    @NotNull
    @Min(1)
    private Integer batteryId;
    @Min(0)
    @Max(100)
    private Double minLevel;
    @Min(0)
    @Max(100)
    private Double maxLevel;
    @Min(0)
    @Max(100)
    private Double percent;
}
