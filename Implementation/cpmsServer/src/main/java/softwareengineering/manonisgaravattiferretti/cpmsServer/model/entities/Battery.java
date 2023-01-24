package softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Battery {
    @Id
    private Integer batteryId;
    private String status;
    private Double maxCapacity;
}
