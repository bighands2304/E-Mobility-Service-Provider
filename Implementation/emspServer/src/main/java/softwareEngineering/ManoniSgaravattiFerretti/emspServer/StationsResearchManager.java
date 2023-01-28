package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class StationsResearchManager {
    @Autowired
    ChargingPointService cpService;
    @GetMapping("/getCPsInRange")
    public ResponseEntity<?> getUserVehicles(@RequestBody Map<String, String> payload){

        Double latitude = Double.parseDouble(payload.get("latitude"));
        Double longitude = Double.parseDouble(payload.get("longitude"));
        Double range = Double.parseDouble(payload.get("range"));

        List<ChargingPoint> cps= cpService.getCPsInRange(latitude, latitude+range, longitude, longitude+range);

        return ResponseEntity.ok(cps);
    }
}
