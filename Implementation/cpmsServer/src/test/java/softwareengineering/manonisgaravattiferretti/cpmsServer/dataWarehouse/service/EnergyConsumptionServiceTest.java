package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.DimensionsPrimaryKey;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.EnergyConsumption;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos.MeanConsumption;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.repository.EnergyConsumptionRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class EnergyConsumptionServiceTest {
    @Autowired
    private EnergyConsumptionService energyConsumptionService;
    @Autowired
    private EnergyConsumptionRepository energyConsumptionRepository;

    @Test
    void meanConsumptionTest() {
        List<EnergyConsumption> energyConsumptionList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LocalDateTime localDateTime = LocalDateTime.now().minus(i, ChronoUnit.HOURS);
            DimensionsPrimaryKey dimensionsPrimaryKey = new DimensionsPrimaryKey("dso", "cp", localDateTime);
            EnergyConsumption energyConsumption = new EnergyConsumption(dimensionsPrimaryKey, 0.0, 10.0 + i, 0.0);
            energyConsumptionList.add(energyConsumption);
        }
        energyConsumptionService.insertBatch(energyConsumptionList);
        LocalDateTime from = LocalDateTime.now().minus(20, ChronoUnit.HOURS);
        LocalDateTime to = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        double meanConsumption = energyConsumptionService.findMeanConsumption("cp", from, to);
        energyConsumptionRepository.deleteAllById(energyConsumptionList.stream().map(EnergyConsumption::getDimensionsPrimaryKey).toList());
        Assertions.assertEquals(14.5, meanConsumption);
    }
}
