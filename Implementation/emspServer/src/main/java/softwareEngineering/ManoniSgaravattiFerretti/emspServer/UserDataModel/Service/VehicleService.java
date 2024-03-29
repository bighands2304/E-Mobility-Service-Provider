package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Vehicle;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository.VehicleRepository;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    public void saveVehicle(Vehicle vehicle){vehicleRepository.save(vehicle);}

    public Vehicle getVehicleByVin(String vin){return vehicleRepository.findVehicleByVINCode(vin);}

    public void deleteVehicleByVin(String vin){ vehicleRepository.delete(vehicleRepository.findVehicleByVINCode(vin));}

}
