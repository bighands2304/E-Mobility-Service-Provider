package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Vehicle;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);
    User findUserByEmail(String email);
    User findUserByUsername(String username);
}
