package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.LocalDateTime;

@PrimaryKeyClass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimensionsPrimaryKey {
    @PrimaryKeyColumn(name = "dso_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String dsoId;
    @PrimaryKeyColumn(name = "cp_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String cpId;
    @PrimaryKeyColumn(name = "datetime", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private LocalDateTime dateTime;
}
