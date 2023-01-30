package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.DimensionsPrimaryKey;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.EnergyConsumption;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos.EnergyConsumptionByYear;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos.EnergyConsumptionByYearMonth;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EnergyConsumptionRepository extends CassandraRepository<EnergyConsumption, DimensionsPrimaryKey> {
    @Query("SELECT * FROM energy_consumption " +
            "WHERE cp_id = ?0 AND dso_id = ?1 AND datetime >= ?2 AND datetime <= ?3" +
            "ALLOW FILTERING")
    List<EnergyConsumption> findBetween(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query("SELECT cp_id, dso_id, FLOOR(datetime, 1y) as year, AVG(battery_usage_kw) AS avg_battery_usage_kw," +
            "AVG(total_energy_amount) AS avg_total_energy_amount, AVG(cost) AS avg_cost" +
            "FROM offer_price " +
            "WHERE cp_id = ?0 AND dso_id = ?1 AND datetime >= ?2 AND datetime <= ?3" +
            "GROUP BY FLOOR(datetime, 1y)" +
            "ALLOW FILTERING")
    List<EnergyConsumptionByYear> findBetweenGroupByYear(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query("SELECT cp_id, dso_id, FLOOR(datetime, 1y) as year, , FLOOR(datetime, 1m) AS month, AVG(battery_usage_kw) AS avg_battery_usage_kw," +
            "AVG(total_energy_amount) AS avg_total_energy_amount, AVG(cost) AS avg_cost" +
            "FROM offer_price " +
            "WHERE cp_id = ?0 AND dso_id = ?1 AND datetime >= ?2 AND datetime <= ?3" +
            "GROUP BY FLOOR(datetime, 1y)" +
            "ALLOW FILTERING")
    List<EnergyConsumptionByYearMonth> findBetweenGroupByYearAndMonth(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo);
}
