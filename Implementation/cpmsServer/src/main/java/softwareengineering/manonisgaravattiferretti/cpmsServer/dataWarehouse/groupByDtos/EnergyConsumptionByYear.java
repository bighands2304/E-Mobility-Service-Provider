package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos;

import lombok.Data;

@Data
public class EnergyConsumptionByYear {
    private String dsoId;
    private String cpId;
    private String year;
    private double batteryUsageKw;
    private double totalEnergyAmountKw;
    private double cost;
}
