package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChargingPointRepository extends MongoRepository<ChargingPoint, String> {
    Optional<ChargingPoint> findChargingPointByCpIdAndCpoCode(String cpId, String cpoCode);
    List<ChargingPoint> findChargingPointsByAddress(String address);
    List<ChargingPoint> findChargingPointsByCpoCode(String cpoCode);
    List<ChargingPoint> findChargingPointsByLatitudeBetweenAndLongitudeBetween(
            String latitudeFrom, String latitudeTo, String longitudeFrom, String LongitudeTo);
}
