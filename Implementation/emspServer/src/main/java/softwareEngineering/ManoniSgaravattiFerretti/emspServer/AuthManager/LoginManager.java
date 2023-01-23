package softwareEngineering.ManoniSgaravattiFerretti.emspServer.AuthManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;

import java.util.HashMap;
import java.util.Map;

@Service
@RestController
public class LoginManager implements UserDetailsService {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenManager tokenManager;

    @Override
    public User loadUserByUsername(String credential) throws UsernameNotFoundException {
        User user = userService.findByAnyCredential(credential);
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return new User(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) throws Exception {
        String username = payload.get("username");
        String password = payload.get("password");
        try{
            //Crea un autenticazione tramite i valori username e password passati nel body
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(username, password));
        }catch (BadCredentialsException e){
            return ResponseEntity.badRequest().body("Bad credentials");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e);
        }
        //Load the userdetails
        final User userDetails = loadUserByUsername(username);
        //Generate a jwt based on the user
        final String jwt = tokenManager.generateToken(userDetails);

        //send the response with the jwt and user
        Map<String,Object> response = new HashMap<>();
        try{
            response.put("jwt", jwt);
            response.put("user", userService.findById(userDetails.getId()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }
        return ResponseEntity.ok(response);
    }
}
