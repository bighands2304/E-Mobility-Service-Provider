package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class Battery {
    @Indexed
    private Integer batteryId;
    private String status;
    private Double maxCapacity;
    private Double minLevel;
    private Double maxLevel;
    private Double percent;
}
