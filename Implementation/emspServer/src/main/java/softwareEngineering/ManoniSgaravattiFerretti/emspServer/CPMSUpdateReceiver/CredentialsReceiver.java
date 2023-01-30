package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.LocationsSender;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.CredentialDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPointOperator;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointOperatorService;

import java.util.UUID;

@RestController
@RequestMapping("/ocpi/emsp")
public class CredentialsReceiver {
    LocationsSender locations;
    ChargingPointOperatorService cpoService;
    @Value("${emsp.path}")
    private String emspPath;

    @PostMapping("/credentials")
    public ResponseEntity<?> credentials(@RequestBody @Valid CredentialDTO credentials){
        ChargingPointOperator cpo = new ChargingPointOperator();
        cpo.setCpmsUrl(credentials.getCpmsUrl());
        cpo.setIban(credentials.getIban());
        cpo.setTokenEmsp(credentials.getEmspToken());

        String token = UUID.randomUUID().toString();

        cpo.setToken(token);
        credentials.setCpmsToken(token);
        credentials.setEmspUrl(emspPath);

        cpoService.saveCPO(cpo);

        locations.getCps(cpo);

        return ResponseEntity.ok(credentials);
    }
}
