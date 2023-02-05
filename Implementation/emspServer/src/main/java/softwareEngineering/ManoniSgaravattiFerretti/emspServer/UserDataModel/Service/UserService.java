package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository.UserRepository;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User findByAnyCredential(String credential){
        User user = userRepository.findUserByEmail(credential);
        if(user == null)
            user = userRepository.findUserByUsername(credential);
        return user;
    }
    public void saveUser(User user){userRepository.save(user);}

    public void deleteUser(User user){userRepository.delete(user);}
    public User findById(Long id){return userRepository.findUserById(id);}
}
