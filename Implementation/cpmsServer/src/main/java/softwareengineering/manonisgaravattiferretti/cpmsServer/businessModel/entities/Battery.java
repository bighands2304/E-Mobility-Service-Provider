package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Battery {
    @Id
    private Integer batteryId;
    private String status;
    private Double maxCapacity;
    private Double minLevel;
    private Double maxLevel;
    private Double percent;
}
