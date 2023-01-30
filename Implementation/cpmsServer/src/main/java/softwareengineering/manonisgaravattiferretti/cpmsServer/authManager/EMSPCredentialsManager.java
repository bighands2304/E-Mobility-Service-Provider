package softwareengineering.manonisgaravattiferretti.cpmsServer.authManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.exceptions.EmspErrorException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.exceptions.EmspResponseTimeoutException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiCredentialsSender;

@RestController
public class EMSPCredentialsManager {
    private final OcpiCredentialsSender ocpiCredentialsSender;
    private final Logger logger = LoggerFactory.getLogger(EMSPCredentialsManager.class);

    @Autowired
    public EMSPCredentialsManager(OcpiCredentialsSender ocpiCredentialsSender) {
        this.ocpiCredentialsSender = ocpiCredentialsSender;
    }

    @PostMapping("/api/CPO/addEmsp")
    public ResponseEntity<?> registerEmsp(@RequestParam String emspUrl, @AuthenticationPrincipal CPO cpo) {
        logger.info("Trying to register to emsp at url " + emspUrl + " for cpo " + cpo.getCpoCode());
        try {
            ocpiCredentialsSender.sendCredentials(emspUrl, cpo.getIban());
        } catch (EmspErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EmspResponseTimeoutException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
