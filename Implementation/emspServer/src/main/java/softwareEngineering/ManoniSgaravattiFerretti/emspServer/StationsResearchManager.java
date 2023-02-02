package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.LocationsSender;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.TariffsSender;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.TariffService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class StationsResearchManager {
    @Autowired
    ChargingPointService cpService;
    @Autowired
    LocationsSender locations;
    @Autowired
    TariffService tariffService;
    @GetMapping("/getCPsInRange")
    public ResponseEntity<?> getCpsInRange(@RequestBody Map<String, String> payload){

        Double latitude = Double.parseDouble(payload.get("latitude"));
        Double longitude = Double.parseDouble(payload.get("longitude"));
        Double range = Double.parseDouble(payload.get("range"));

        List<ChargingPoint> cps= cpService.getCPsInRange(latitude, latitude+range, longitude, longitude+range);

        return ResponseEntity.ok(cps);
    }

    @GetMapping("/getCP/{cpId}")
    public ResponseEntity<?> getCp(@PathVariable String cpId){
        ChargingPoint cp = cpService.getCPById(cpId);
        //fetch the status of the cp from its cpms
        locations.getCp(cp);
        //return last informations
        return ResponseEntity.ok(cpService.getCPById(cpId));
    }

    @GetMapping("/getTariff/{tariffId}")
    public ResponseEntity<?> getTariff(@PathVariable String tariffId){
        return ResponseEntity.ok(tariffService.getTariffById(tariffId));
    }
 }
