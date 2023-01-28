/*package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

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
@RequestMapping("/ocpi/emsp/2.2")
public class CPMSUpdateReceiver {
    @Value("${emsp.path}")
    private String emspPath;
    @Autowired
    ChargingPointService chargingPointService;
    @Autowired
    ChargingPointOperatorService cpoService;



    @PostMapping("/addCPs")
    public ResponseEntity<?> addCPs(@RequestBody Map<String,String> payload){
        ChargingPointOperator cpo = cpoService.getCPOById(payload.get("cpoId"));
        //TODO take CPMS payload
        //List<ChargingPoint> cps = chargingPointService.saveCPs();
        //chargingPointService.saveCPs(cps);
        return ResponseEntity.ok(""cps);
    }

    @GetMapping("/getCPs")
    public ResponseEntity<?> getCPs(){
        return ResponseEntity.ok(chargingPointService.getAllCPs());
    }


}*/
