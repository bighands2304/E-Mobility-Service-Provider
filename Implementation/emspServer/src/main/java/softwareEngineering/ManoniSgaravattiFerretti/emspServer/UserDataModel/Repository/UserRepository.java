package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
}
