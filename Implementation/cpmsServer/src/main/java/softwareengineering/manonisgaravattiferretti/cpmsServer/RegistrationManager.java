package softwareengineering.manonisgaravattiferretti.cpmsServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.authManager.UserDetailsServiceImpl;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CPORegistrationDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.CPOService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;

@RestController
public class RegistrationManager {
    private final UserDetailsServiceImpl userDetailsService;
    private final CPOService cpoService;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(RegistrationManager.class);

    @Autowired
    public RegistrationManager(UserDetailsServiceImpl userDetailsService, CPOService cpoService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.cpoService = cpoService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/CPO/register")
    public ResponseEntity<?> registerCPO(@RequestBody CPORegistrationDTO cpoRegistrationDTO) {
        logger.info("Received registration cpo: " + cpoRegistrationDTO);
        try {
            CPO existingCPO = userDetailsService.loadUserByUsername(cpoRegistrationDTO.getCpoCode());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPO already present");
        } catch (UsernameNotFoundException e) {
            CPO cpo = EntityFromDTOConverter.cpoFromRegistrationDTO(cpoRegistrationDTO);
            cpo.encodePassword(passwordEncoder);
            cpoService.insertCPO(cpo);
            logger.info("Registered cpo: " + cpoRegistrationDTO);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
