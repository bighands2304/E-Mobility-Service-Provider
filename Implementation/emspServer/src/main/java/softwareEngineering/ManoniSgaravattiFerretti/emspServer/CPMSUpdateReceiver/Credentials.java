package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver.OcpiDTOs.CredentialDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPointOperator;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointOperatorService;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;

@RestController
@RequestMapping("/ocpi/emsp/2.2")
public class Credentials {
    @Autowired
    ChargingPointOperatorService cpoService;
    @Value("${emsp.path}")
    private String emspPath;

    @PostMapping("/credentials")
    public ResponseEntity<?> registerCPO(@RequestBody @Valid CredentialDTO credentials){
        ChargingPointOperator cpo = new ChargingPointOperator();
        cpo.setCpmsUrl(credentials.getCpmsUrl());
        cpo.setIban(credentials.getIban());
        cpo.setTokenEmsp(credentials.getEmspToken());

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String token = new String(bytes, StandardCharsets.UTF_8);

        cpo.setToken(token);
        credentials.setCpmsToken(token);
        credentials.setEmspUrl(emspPath);

        cpoService.saveCPO(cpo);

        return ResponseEntity.ok(credentials);
    }
}
