package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.aggregationResults.TariffUnwind;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChargingPointRepository extends MongoRepository<ChargingPoint, String>, ChargingPointCustomUpdate {
    Optional<ChargingPoint> findChargingPointByCpIdAndCpoCode(String cpId, String cpoCode);
    Optional<ChargingPoint> findChargingPointByCpId(String cpId);
    List<ChargingPoint> findChargingPointsByAddress(String address);
    Page<ChargingPoint> findChargingPointsByCpoCode(String cpoCode, Pageable pageable);
    Page<ChargingPoint> findAllByLastUpdatedBetween(LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    Optional<ChargingPoint> findChargingPointByAuthenticationKey(String authenticationKey);

    @Aggregation(pipeline = {"""
            {
                $unwind: {
                    path: '$tariffs'
                }
            }
            """, """
            {
                $match: {
                    'tariffs.lastUpdated': {'$gte': ?0, '$lte': ?1}
                }
            }
            """, """
            {
                $project: {
                    '_id': 0,
                    'tariff': '$tariffs'
                }
            }
            """, """
            {
                $sort: {
                    'tariff.lastUpdated': -1
                }
            }
            """, """
            {
                $skip: ?2
            }
            """, """
            {
                $limit: ?3
            }
            """
    })
    AggregationResults<TariffUnwind> findAllTariffsBetween(LocalDateTime dateFrom, LocalDateTime dateTo, int skip, int limit);

    @Aggregation(pipeline = {"""
            {
                $unwind: {
                    path: '$tariffs'
                }
            }
            """, """
            {
                $project: {
                    '_id': 0,
                    'tariff': '$tariffs'
                }
            }
            """, """
            {
                $sort: {
                    'tariff.lastUpdated': -1
                }
            }
            """, """
            {
                $skip: ?2
            }
            """, """
            {
                $limit: ?3
            }
            """
    })
    AggregationResults<TariffUnwind> findAllTariffs(int skip, int limit);
}
