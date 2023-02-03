package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.LocationsSender;
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
        //Collect the payload
        Double latitude = Double.parseDouble(payload.get("latitude"));
        Double longitude = Double.parseDouble(payload.get("longitude"));
        Double range = Double.parseDouble(payload.get("range"));

        //Search for CPs in a certain range
        List<ChargingPoint> cps= cpService.getCPsInRange(latitude, latitude+range, longitude, longitude+range);

        //Return the response with the list of CPs found
        return ResponseEntity.ok(cps);
    }

    @GetMapping("/getCP/{cpId}")
    public ResponseEntity<?> getCp(@PathVariable String cpId){
        //Collect the payload
        ChargingPoint cp = cpService.getCPById(cpId);

        //Fetch the status of the cp from its cpms
        locations.getCp(cp);

        //Return last information
        return ResponseEntity.ok(cpService.getCPById(cpId));
    }

    @GetMapping("/getTariff/{tariffId}")
    public ResponseEntity<?> getTariff(@PathVariable String tariffId){
        //Return the tariff by its id
        return ResponseEntity.ok(tariffService.getTariffById(tariffId));
    }
 }
