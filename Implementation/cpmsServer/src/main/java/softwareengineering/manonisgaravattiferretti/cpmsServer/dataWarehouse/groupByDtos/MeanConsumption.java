package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;

@Data
public class MeanConsumption {
    @Column(value = "mean_consumption")
    private double meanConsumption;
}
