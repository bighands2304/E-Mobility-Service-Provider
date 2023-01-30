package softwareengineering.manonisgaravattiferretti.cpmsServer.authManager;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CPOLoginDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.CPOService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginManager {
    private final UserDetailsServiceImpl userDetailsService;
    private final CPOService cpoService;
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;
    private final Logger logger = LoggerFactory.getLogger(LoginManager.class);

    @Autowired
    public LoginManager(UserDetailsServiceImpl userDetailsService, CPOService cpoService,
                        @Lazy AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.cpoService = cpoService;
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/api/CPO/login")
    public ResponseEntity<?> login(@RequestBody @Valid CPOLoginDTO loginDTO) {
        logger.info("Login request received: " + loginDTO);
        try{
            //Crea un autenticazione tramite i valori username e password passati nel body
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getCpoCode(), loginDTO.getPassword()));
        }catch (AuthenticationException e){
            logger.info(e.getMessage());
            throw new AccessDeniedException("Bad credentials");
        }
        //Cerco l'esistenza dell'utente nel db
        final CPO userDetails = userDetailsService.loadUserByUsername(loginDTO.getCpoCode());
        //Genero la stringa jwt relativa a quell'utente
        final String jwt = tokenManager.generateToken(userDetails);

        //Invio la risposta con l'autenticazione e i dettagli dell'utente
        logger.info(loginDTO + " has logged on");
        Map<String,Object> response = new HashMap<>();
        response.put("jwt", jwt);
        response.put("user", cpoService.getCPOData(loginDTO.getCpoCode()));
        return ResponseEntity.ok(response);
    }
}
