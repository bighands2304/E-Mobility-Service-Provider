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
    public ResponseEntity<?> registerEmsp(@RequestBody @Valid EmspCredentialsDTO emspCredentials) {
        logger.info("Emsp credentials: " + emspCredentials);
        Optional<EmspDetails> emspDetails = emspDetailsService.findByEmspToken(emspCredentials.getEmspToken());
        if (emspDetails.isPresent()) {
            // emsp is already registered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "emsp already registered");
        }
        emspDetailsService.insertEmsp(EntityFromDTOConverter.emspDetailsFromCredentials(emspCredentials));
        try {
            sendCredentials(emspCredentials.getUrl(), emspCredentials.getEmspToken());
        } catch (UnknownHostException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
        }
        return ResponseEntity.ok(emspCredentials);
    }

    @Async
    void sendCredentials(String emspUrl, String emspToken) throws UnknownHostException {
        String token = ""; //Todo
        String url = InetAddress.getLocalHost().getHostName();
        CpmsCredentialsDTO credentials = new CpmsCredentialsDTO(token, url);
        Mono<EmspCredentialsDTO> resp = WebClient.create(emspUrl)
                .post()
                .uri("/ocpi/emsp/credentials")
                .bodyValue(credentials)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(EmspCredentialsDTO.class);
                    } else {
                        return Mono.empty();
                    }
                });
        if (resp.blockOptional().isPresent()) {
            emspDetailsService.updateEmspAddCpoToken(emspToken, token);
        }
    }
}
