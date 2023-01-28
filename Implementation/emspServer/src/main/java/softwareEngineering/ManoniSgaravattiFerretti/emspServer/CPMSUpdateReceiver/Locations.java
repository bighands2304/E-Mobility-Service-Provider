package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.SocketService;

import java.util.Map;

@RestController
@RequestMapping("/ocpi/emsp/2.2/locations")
public class Locations {
    @Autowired
    SocketService socketService;

    @GetMapping("/{connector_id}")
    public ResponseEntity<?> getLocation(@PathVariable String connector_id, @RequestBody Map<String, String> payload){
        socketService.getSocketById(connector_id);
        //TODO add requestBody

        return ResponseEntity.ok("");
    }

    @PutMapping("/{connector_id}")
    public ResponseEntity<?> putLocation(@PathVariable String connector_id, @RequestBody Map<String, String> payload){
        socketService.getSocketById(connector_id);
        //TODO add requestBody

        return ResponseEntity.ok("");
    }

    @PatchMapping("/{connector_id}")
    public ResponseEntity<?> patchLocation(@PathVariable String connector_id, @RequestBody Map<String, String> payload){
        socketService.getSocketById(connector_id);
        //TODO add requestBody

        return ResponseEntity.ok("");
    }
}
