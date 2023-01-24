package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User findByEmail(String mail) { return userRepository.findUserByEmail(mail);}
    public User findByAnyCredential(String credential){
        User user = userRepository.findUserByEmail(credential);
        if(user == null)
            user = userRepository.findUserByUsername(credential);
        return user;
    }
    public User findByUsername(String username){return userRepository.findUserByUsername(username);}
    public User saveUser(User user){return userRepository.save(user);}
    public User findById(Long id){return userRepository.findUserById(id);}
}
