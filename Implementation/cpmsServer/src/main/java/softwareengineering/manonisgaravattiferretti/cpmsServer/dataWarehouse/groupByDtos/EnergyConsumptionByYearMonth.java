package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos;

import lombok.Data;

@Data
public class EnergyConsumptionByYearMonth {
    private String dsoId;
    private String cpId;
    private String year;
    private String month;
    private double batteryUsageKw;
    private double totalEnergyAmountKw;
    private double cost;
}
