package softwareengineering.manonisgaravattiferretti.cpmsServer.authManager;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CpmsCredentialsDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspCredentialsDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.EmspDetailsService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;

@RestController
public class EMSPCredentialsManager {
    private final EmspDetailsService emspDetailsService;
    private final Logger logger = LoggerFactory.getLogger(EMSPCredentialsManager.class);

    @Autowired
    public EMSPCredentialsManager(EmspDetailsService emspDetailsService) {
        this.emspDetailsService = emspDetailsService;
    }

    @PostMapping("/ocpi/cpo/credentials")
    public ResponseEntity<CpmsCredentialsDTO> registerEmsp(@RequestBody @Valid EmspCredentialsDTO emspCredentials) {
        logger.info("Emsp credentials: " + emspCredentials);
        Optional<EmspDetails> emspDetails = emspDetailsService.findByEmspToken(emspCredentials.getEmspToken());
        if (emspDetails.isPresent()) {
            // emsp is already registered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "emsp already registered");
        }
        emspDetailsService.insertEmsp(EntityFromDTOConverter.emspDetailsFromCredentials(emspCredentials));
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[20];
        secureRandom.nextBytes(bytes);
        String token = new String(bytes, StandardCharsets.UTF_8);
        String url;
        try {
            url = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "could not send the cpms credentials");
        }
        // todo: save credentials
        emspDetailsService.updateEmspAddCpoToken(emspCredentials.getEmspToken(), token);
        CpmsCredentialsDTO credentials = new CpmsCredentialsDTO(token, url, null);
        return ResponseEntity.ok(credentials);
    }
}
