package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.UserVehicle;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository.UserVehicleRepository;

import java.util.List;

@Service
public class UserVehicleService {
    @Autowired
    UserVehicleRepository userVehicleRepository;

    public void saveUserVehicle(UserVehicle userVehicle) {userVehicleRepository.save(userVehicle);}

    public List<UserVehicle> getUserVehicles(Long userId) {return userVehicleRepository.findAllByUserId(userId);}

    public UserVehicle findFavouriteOfUser(Long userId){return userVehicleRepository.findByUserIdAndFavouriteIsTrue(userId);}

    public UserVehicle findVehcileByIds(Long userId, String vehicleVin){return userVehicleRepository.findByUserIdAndVehicleVINCode(userId,vehicleVin);}
}
