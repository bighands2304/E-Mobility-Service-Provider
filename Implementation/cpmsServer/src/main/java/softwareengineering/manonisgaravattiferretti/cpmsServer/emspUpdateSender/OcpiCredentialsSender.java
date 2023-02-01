package softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CpmsRegistrationDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CredentialsDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.exceptions.EmspErrorException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.exceptions.EmspResponseTimeoutException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.EmspDetailsService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Component
public class OcpiCredentialsSender {
    private final Logger logger = LoggerFactory.getLogger(OcpiCredentialsSender.class);
    @Value("${cpms.path}")
    private String cpmsUrl;
    private final EmspDetailsService emspDetailsService;

    @Autowired
    public OcpiCredentialsSender(EmspDetailsService emspDetailsService) {
        this.emspDetailsService = emspDetailsService;
    }

    public void sendCredentials(String emspUrl, String iban) throws EmspErrorException, EmspResponseTimeoutException {
        // header is different since from the standard ocpi since this is the registration
        // of the cpms in the emsp platform
        String emspToken = UUID.randomUUID().toString();

        // build temporary emsp credentials to store in db
        /*EmspDetails emspDetails = new EmspDetails();
        emspDetails.setEmspToken(emspToken);
        emspDetails.setUrl(emspUrl);
        emspDetails = emspDetailsService.insertEmsp(emspDetails);
        logger.info("Inserted temporary credentials for emsp: " + emspDetails);*/

        Optional<CredentialsDTO> credentialsDTOOptional;
        try {
            credentialsDTOOptional = WebClient
                    .create()
                    .method(HttpMethod.POST)
                    .uri(emspUrl + "/ocpi/emsp/credentials")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new CpmsRegistrationDTO(emspToken, cpmsUrl, iban))
                    .retrieve()
                    .bodyToMono(CredentialsDTO.class)
                    .blockOptional(Duration.ofSeconds(10));
        } catch (RuntimeException e) {
            throw new EmspResponseTimeoutException("Request timeout: emsp is not reachable");
        }
        CredentialsDTO credentialsDTO = credentialsDTOOptional.orElseThrow(() -> new EmspErrorException("Bad request"));
        emspDetailsService.insertEmsp(EntityFromDTOConverter.emspDetailsFromCredentialsDTO(credentialsDTO));
    }
}
