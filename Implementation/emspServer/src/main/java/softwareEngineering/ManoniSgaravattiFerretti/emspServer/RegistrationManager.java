package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;

import java.util.List;
import java.util.Map;

@RestController
public class RegistrationManager {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        User user = new User();
        user.setUsername(payload.get("username"));
        user.setEmail(payload.get("email"));
        user.setPassword(passwordEncoder.encode(payload.get("password")));

        try {
            userService.saveUser(user);
            return ResponseEntity.ok(user);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body(e);
        }
    }
}
