package softwareengineering.manonisgaravattiferretti.cpmsServer.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities.ChargingPoint;

import java.util.List;
import java.util.Optional;

public interface ChargingPointRepository extends MongoRepository<ChargingPoint, String> {
    Optional<ChargingPoint> findChargingPointByCpId(String cpId);
    List<ChargingPoint> findChargingPointsByAddress(String address);
    List<ChargingPoint> findChargingPointsByCpoCpoCode(String cpoCode);
    List<ChargingPoint> findChargingPointsByLatitudeBetweenAndLongitudeBetween(
            String latitudeFrom, String latitudeTo, String longitudeFrom, String LongitudeTo);
}
