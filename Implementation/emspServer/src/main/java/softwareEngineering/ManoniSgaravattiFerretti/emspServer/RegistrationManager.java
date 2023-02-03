package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;

import java.util.Map;

@RestController
public class RegistrationManager {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        //Collect required fields
        User user = new User();
        user.setUsername(payload.get("username"));
        user.setEmail(payload.get("email"));
        user.setPassword(passwordEncoder.encode(payload.get("password")));

        //Collect not required fields
        user.setName(payload.get("name"));
        user.setSurname(payload.get("surname"));

        try {
            //Save the user in the DB
            userService.saveUser(user);
            //Return the user in the response
            return ResponseEntity.ok(user);
        }catch (Exception e){
            //Return error message in the response if occurs
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
