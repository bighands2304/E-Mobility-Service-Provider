package softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;

import java.util.Optional;

@RestController("/api/CPO/chargingPoints")
public class ChargingPointsManager {
    private final ChargingPointService chargingPointService;

    @Autowired
    public ChargingPointsManager(ChargingPointService chargingPointService) {
        this.chargingPointService = chargingPointService;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<ChargingPoint>> getAllCps(@AuthenticationPrincipal CPO cpo) {
        return new ResponseEntity<>(chargingPointService.getChargingPointsOfCpo(cpo.getCpoCode()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargingPoint> getCPById(@AuthenticationPrincipal CPO cpo, @PathVariable String id) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Charging point not found");
        }
        return new ResponseEntity<>(chargingPointOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChargingPoint(@AuthenticationPrincipal CPO cpo, @PathVariable String id) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointByInternalId(id, cpo.getCpoCode());
        if (chargingPointOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Charging point not found");
        }
        chargingPointService.deleteChargingPoint(id);
        return new ResponseEntity<>(chargingPointOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/{cpId}/sockets")
    public ResponseEntity<Socket> getSockets(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId) {
        return ResponseEntity.ok(null); //TODO
    }

    @GetMapping("/{cpId}/sockets/{socketId}")
    public ResponseEntity<Socket> getSocketInfo(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId,
                                                @PathVariable String socketId) {
        return ResponseEntity.ok(null); //TODO
    }

    @GetMapping("/{cpId}/tariffs")
    public ResponseEntity<Socket> getTariffs(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId) {
        return ResponseEntity.ok(null); //TODO
    }

    @GetMapping("/{cpId}/tariffs/{tariffId}")
    public ResponseEntity<Socket> getTariffs(@AuthenticationPrincipal CPO cpo, @PathVariable String cpId,
                                             @PathVariable String tariffId) {
        return ResponseEntity.ok(null); //TODO
    }

    @PostMapping()
    public void addChargingPoint() {
        //Todo
    }
}
