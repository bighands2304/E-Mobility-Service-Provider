package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Socket;

import java.util.Collection;
import java.util.List;

@Repository
public interface ChargingPointRepository extends MongoRepository<ChargingPoint, String> {
    List<ChargingPoint> findAllByLatitudeBetweenAndLongitudeBetween(Double latitude_start, Double latitude_end, Double longitude_start, Double longitude_end);
    ChargingPoint findChargingPointByCpId(String cpId);
    List<ChargingPoint> findChargingPointsByTariffsIdContains(String tariffsId);
}
