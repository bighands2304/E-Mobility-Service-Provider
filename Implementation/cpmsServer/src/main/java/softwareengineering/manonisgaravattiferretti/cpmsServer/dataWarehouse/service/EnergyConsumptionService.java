package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.DimensionsPrimaryKey;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.EnergyConsumption;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos.EnergyConsumptionByYear;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos.EnergyConsumptionByYearMonth;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.repository.EnergyConsumptionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnergyConsumptionService {
    private final EnergyConsumptionRepository energyConsumptionRepository;

    @Autowired
    public EnergyConsumptionService(EnergyConsumptionRepository energyConsumptionRepository) {
        this.energyConsumptionRepository = energyConsumptionRepository;
    }

    public void insert(EnergyConsumption energyConsumption) {
        energyConsumptionRepository.insert(energyConsumption);
    }

    public void insertBatch(List<EnergyConsumption> energyConsumptions) {
        energyConsumptionRepository.insert(energyConsumptions);
    }

    public List<EnergyConsumption> findBetween(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return energyConsumptionRepository.findBetween(cpId, dsoId, dateFrom, dateTo);
    }

    public List<EnergyConsumptionByYear> findBetweenByYear(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return energyConsumptionRepository.findBetweenGroupByYear(cpId, dsoId, dateFrom, dateTo);
    }

    public List<EnergyConsumptionByYearMonth> findBetweenByYearMonth(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return energyConsumptionRepository.findBetweenGroupByYearAndMonth(cpId, dsoId, dateFrom, dateTo);
    }

    public Optional<EnergyConsumption> find(String cpId, String dsoId, LocalDateTime timestamp) {
        return energyConsumptionRepository.findById(new DimensionsPrimaryKey(dsoId, cpId, timestamp));
    }
}
