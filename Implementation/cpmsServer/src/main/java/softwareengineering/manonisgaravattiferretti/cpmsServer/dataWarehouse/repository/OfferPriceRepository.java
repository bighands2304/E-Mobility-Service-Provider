package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.DimensionsPrimaryKey;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.OfferPrice;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos.OfferPriceByYear;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos.OfferPriceByYearMonth;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferPriceRepository extends CassandraRepository<OfferPrice, DimensionsPrimaryKey> {
    @Query("SELECT * FROM offer_price" +
            "WHERE cp_id = ?0 AND dso_id = ?1 AND datetime >= ?2 AND datetime <= ?3" +
            "ALLOW FILTERING")
    List<OfferPrice> findBetween(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query("SELECT cp_id, dso_id, FLOOR(datetime, 1y) AS year, AVG(price) AS avg_price" +
            "FROM offer_price" +
            "WHERE cp_id = ?0 AND dso_id = ?1 AND datetime >= ?2 AND datetime <= ?3" +
            "GROUP BY FLOOR(datetime, 1y), FLOOR(datetime, 1m)" +
            "ALLOW FILTERING")
    List<OfferPriceByYear> findBetweenGroupByYear(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query("SELECT cp_id, dso_id, FLOOR(datetime, 1y) AS year, FLOOR(datetime, 1m) AS month, AVG(price) AS avg_price" +
            "FROM offer_price" +
            "WHERE cp_id = ?0 AND dso_id = ?1 AND datetime >= ?2 AND datetime <= ?3" +
            "GROUP BY FLOOR(datetime, 1y), FLOOR(datetime, 1m)" +
            "ALLOW FILTERING")
    List<OfferPriceByYearMonth> findBetweenGroupByYearAndMonth(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query("SELECT AVG(price) AS mean_price FROM offer_price " +
            "WHERE cp_id = ?0 AND dso_id = ?1 AND datetime >= ?2 AND datetime <= ?3" +
            "ALLOW FILTERING")
    double getMeanPrice(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo);
}
