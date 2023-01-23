package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.UserVehicle;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.UserVehicleId;

import java.util.List;

@Repository
public interface UserVehicleRepository extends JpaRepository<UserVehicle, UserVehicleId> {
    List<UserVehicle> findAllByUserId(Long userId);
}
