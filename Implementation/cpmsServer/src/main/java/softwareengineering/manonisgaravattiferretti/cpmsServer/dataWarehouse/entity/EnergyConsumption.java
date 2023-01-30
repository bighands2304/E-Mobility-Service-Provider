package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "energy_consumption")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumption {
    @PrimaryKey
    private DimensionsPrimaryKey dimensionsPrimaryKey;
    @Column(value = "battery_usage_kw")
    private double batteryUsageKw;
    @Column(value = "total_energy_amount_kw")
    private double totalEnergyAmountKw;
    @Column(value = "cost")
    private double cost;
}
