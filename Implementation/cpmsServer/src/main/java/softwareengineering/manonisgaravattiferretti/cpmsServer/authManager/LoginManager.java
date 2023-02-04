package softwareengineering.manonisgaravattiferretti.cpmsServer.authManager;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CPOLoginDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChangePasswordDTO;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginManager(UserDetailsServiceImpl userDetailsService, CPOService cpoService,
                        @Lazy AuthenticationManager authenticationManager, TokenManager tokenManager,
                        PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.cpoService = cpoService;
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/CPO/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody @Valid CPOLoginDTO loginDTO) {
        logger.info("Login request received: " + loginDTO);
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getCpoCode(), loginDTO.getPassword()));
        } catch (AuthenticationException e){
            logger.info(e.getMessage());
            throw new AccessDeniedException("Bad credentials");
        }
        final CPO userDetails = userDetailsService.loadUserByUsername(loginDTO.getCpoCode());
        final String jwt = tokenManager.generateToken(userDetails);

        logger.info(loginDTO + " has logged on");
        Map<String,Object> response = new HashMap<>();
        response.put("jwt", jwt);
        response.put("user", cpoService.getCPOData(loginDTO.getCpoCode()));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/api/CPO/changePassword")
    public ResponseEntity<CPO> changePassword(@AuthenticationPrincipal CPO cpo,
                                            @RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        logger.info("Change password from cpo: " + cpo.getCpoCode());
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(cpo.getCpoCode(), changePasswordDTO.getOldPassword()));
        } catch (AuthenticationException e){
            logger.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password not correct");
        }
        cpo.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        cpoService.insertCPO(cpo);
        return new ResponseEntity<>(cpo, HttpStatus.CREATED);
    }
}
