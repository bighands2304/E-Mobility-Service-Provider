package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChargingPointRepository extends MongoRepository<ChargingPoint, String>, ChargingPointCustomUpdate {
    Optional<ChargingPoint> findChargingPointByCpIdAndCpoCode(Integer cpId, String cpoCode);
    Optional<ChargingPoint> findChargingPointByCpId(Integer cpId);
    List<ChargingPoint> findChargingPointsByAddress(String address);
    List<ChargingPoint> findChargingPointsByCpoCode(String cpoCode);
    List<ChargingPoint> findChargingPointsByLatitudeBetweenAndLongitudeBetween(
            String latitudeFrom, String latitudeTo, String longitudeFrom, String LongitudeTo);
    Page<ChargingPoint> findAllByLastUpdatedBetween(LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    Optional<ChargingPoint> findChargingPointByAuthenticationKey(String authenticationKey);
}
