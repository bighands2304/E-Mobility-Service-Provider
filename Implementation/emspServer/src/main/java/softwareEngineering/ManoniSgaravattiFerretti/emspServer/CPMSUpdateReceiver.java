package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPointOperator;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointOperatorService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ocpi")
public class CPMSUpdateReceiver {
    @Value("${emsp.path}")
    private String emspPath;
    @Autowired
    ChargingPointService chargingPointService;
    @Autowired
    ChargingPointOperatorService cpoService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCPO(@RequestBody Map<String,String> payload){
        ChargingPointOperator cpo = new ChargingPointOperator();
        cpo.setCpmsUrl(payload.get("url"));
        cpo.setIban(payload.get("iban"));

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        cpo.setToken(bytes.toString());

        cpoService.saveCPO(cpo);
        return ResponseEntity.ok(cpo);
    }

    @PostMapping("/addCPs")
    public ResponseEntity<?> addCPs(@RequestBody Map<String,String> payload){
        ChargingPointOperator cpo = cpoService.getCPOById(payload.get("cpoId"));
        //TODO take CPMS payload
        //List<ChargingPoint> cps = chargingPointService.saveCPs();
        //chargingPointService.saveCPs(cps);
        return ResponseEntity.ok(""/*cps*/);
    }

    @GetMapping("/getCPs")
    public ResponseEntity<?> getCPs(){
        return ResponseEntity.ok(chargingPointService.getAllCPs());
    }

}
