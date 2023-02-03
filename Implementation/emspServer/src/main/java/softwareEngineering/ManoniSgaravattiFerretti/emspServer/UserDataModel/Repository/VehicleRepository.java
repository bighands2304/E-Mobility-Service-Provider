package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Vehicle;


@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findVehicleByVINCode(String vin);
}
