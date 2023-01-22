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
    public User loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = userService.findByEmail(mail);
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
            System.out.println(e);
            throw new AccessDeniedException("Bad credentials");
        }
        //Cerco l'esistenza dell'utente nel db
        final User userDetails = loadUserByUsername(username);
        //Genero la stringa jwt relativa a quell'utente
        final String jwt = tokenManager.generateToken(userDetails);

        //Invio la risposta con l'autenticazione e i dettagli dell'utente
        Map<String,Object> response = new HashMap<>();
        response.put("jwt", jwt);
        response.put("user", userService.findById(userDetails.getId()));
        return ResponseEntity.ok(response);
    }
}
