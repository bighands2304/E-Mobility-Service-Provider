package softwareengineering.manonisgaravattiferretti.cpmsServer.authManager;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CpmsCredentialsDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspCredentialsDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.EmspDetailsService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Optional;

@RestController
public class EMSPCredentialsManager {
    private final EmspDetailsService emspDetailsService;
    private final Logger logger = LoggerFactory.getLogger(EMSPCredentialsManager.class);
    private final TokenManager tokenManager;
    @Value("${cpms.path}")
    private String cpmsUrl;

    @Autowired
    public EMSPCredentialsManager(EmspDetailsService emspDetailsService, TokenManager tokenManager) {
        this.emspDetailsService = emspDetailsService;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/ocpi/cpo/credentials")
    public ResponseEntity<CpmsCredentialsDTO> registerEmsp(@RequestBody @Valid EmspCredentialsDTO emspCredentials) {
        logger.info("Emsp credentials: " + emspCredentials);
        Optional<EmspDetails> emspDetails = emspDetailsService.findByEmspToken(emspCredentials.getEmspToken());
        if (emspDetails.isPresent()) {
            // emsp is already registered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "emsp already registered");
        }
        EmspDetails emspDetailsEntity = emspDetailsService
                .insertEmsp(EntityFromDTOConverter.emspDetailsFromCredentials(emspCredentials));
        String token = tokenManager.generateToken(emspDetailsEntity);
        emspDetailsService.updateEmspAddCpoToken(emspCredentials.getEmspToken(), token);
        CpmsCredentialsDTO credentials = new CpmsCredentialsDTO(token, cpmsUrl, null);
        return ResponseEntity.ok(credentials);
    }
}
