package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class SocketStatusController {
    private final SocketService socketService;
    private final ChargingPointService chargingPointService;

    @Autowired
    public SocketStatusController(SocketService socketService, ChargingPointService chargingPointService) {
        this.socketService = socketService;
        this.chargingPointService = chargingPointService;
    }

    @GetMapping("/ocpi/cpo/locations")
    public ResponseEntity<Iterable<ChargingPoint>> getCps(@RequestParam(required = false) LocalDateTime dateFrom,
                                                          @RequestParam(required = false) LocalDateTime dateTo,
                                                          @RequestParam(defaultValue = "0") Integer offset,
                                                          @RequestParam(defaultValue = "100") Integer limit) {
        Page<ChargingPoint> chargingPoints = (dateFrom == null) ? chargingPointService.findAllPaginated(offset, limit) :
                chargingPointService.findAllLastUpdatePaginated(dateFrom, dateTo, offset, limit);
        return new ResponseEntity<>(chargingPoints, HttpStatus.OK);
    }

    @GetMapping("/ocpi/cpo/locations/{cpId}")
    public ResponseEntity<ChargingPoint> getCp(@PathVariable Integer cpId) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointById(cpId);
        ChargingPoint chargingPoint = chargingPointOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Charging point not found"));
        return new ResponseEntity<>(chargingPoint, HttpStatus.OK);
    }

    @GetMapping("ocpi/cpo/locations/{cpId}/{socketId}")
    public ResponseEntity<Socket> getSocket(@PathVariable Integer cpId, @PathVariable Integer socketId) {
        Optional<Socket> socketOptional = socketService.findSocketByCpIdAndSocketId(cpId, socketId);
        Socket socket = socketOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Socket not found"));
        return new ResponseEntity<>(socket, HttpStatus.OK);
    }
}
