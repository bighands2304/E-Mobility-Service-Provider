package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChargingSessionDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspChargingPointDTOWithId;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspSocketDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class SocketStatusController {
    private final SocketService socketService;
    private final ChargingPointService chargingPointService;
    private final ReservationService reservationService;

    @Autowired
    public SocketStatusController(SocketService socketService, ChargingPointService chargingPointService, ReservationService reservationService) {
        this.socketService = socketService;
        this.chargingPointService = chargingPointService;
        this.reservationService = reservationService;
    }

    @GetMapping("/ocpi/cpo/locations")
    public ResponseEntity<Iterable<EmspChargingPointDTOWithId>> getCps(@RequestParam(required = false) LocalDateTime dateFrom,
                                                                 @RequestParam(required = false) LocalDateTime dateTo,
                                                                 @RequestParam(defaultValue = "0") Integer offset,
                                                                 @RequestParam(defaultValue = "100") Integer limit) {
        Page<ChargingPoint> chargingPoints = (dateFrom == null) ? chargingPointService.findAllPaginated(offset, limit) :
                chargingPointService.findAllLastUpdatePaginated(dateFrom, dateTo, offset, limit);
        Page<EmspChargingPointDTOWithId> chargingPointDTOS = chargingPoints.map(
                EntityFromDTOConverter::emspChargingPointDTOWithIdFromChargingPoint);
        return new ResponseEntity<>(chargingPointDTOS, HttpStatus.OK);
    }

    @GetMapping("/ocpi/cpo/locations/{cpId}")
    public ResponseEntity<EmspChargingPointDTOWithId> getCp(@PathVariable String cpId) {
        Optional<ChargingPoint> chargingPointOptional = chargingPointService.findChargingPointById(cpId);
        ChargingPoint chargingPoint = chargingPointOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Charging point not found"));
        return new ResponseEntity<>(EntityFromDTOConverter.emspChargingPointDTOWithIdFromChargingPoint(chargingPoint), HttpStatus.OK);
    }

    @GetMapping("/ocpi/cpo/locations/{cpId}/{socketId}")
    public ResponseEntity<EmspSocketDTO> getSocket(@PathVariable String cpId, @PathVariable Integer socketId) {
        Optional<Socket> socketOptional = socketService.findSocketByCpIdAndSocketId(cpId, socketId);
        Socket socket = socketOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Socket not found"));
        return new ResponseEntity<>(EntityFromDTOConverter.emspSocketDTOFromSocket(socket), HttpStatus.OK);
    }

    @GetMapping("/ocpi/cpo/sessions")
    public ResponseEntity<Iterable<ChargingSessionDTO>> getSessions(@RequestParam(required = false) LocalDateTime dateFrom,
                                                                    @RequestParam(required = false) LocalDateTime dateTo,
                                                                    @RequestParam(defaultValue = "0") Integer offset,
                                                                    @RequestParam(defaultValue = "100") Integer limit) {
        Page<Reservation> reservations;
        if (dateFrom == null) {
            reservations = reservationService.findAll(offset, limit);
        } else {
            LocalDateTime dateToCorrect = (dateTo == null) ? LocalDateTime.now() : dateTo;
            reservations = reservationService.findAll(dateFrom, dateToCorrect, offset, limit);
        }
        Page<ChargingSessionDTO> sessions = reservations.map(EntityFromDTOConverter::chargingSessionDTOFromReservation);
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }

    @GetMapping("/ocpi/cpo/sessions/{sessionId}")
    public ResponseEntity<ChargingSessionDTO> getSession(@PathVariable Long sessionId) {
        Optional<Reservation> reservationOptional = reservationService.findReservationBySessionId(sessionId);
        if (reservationOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "session not found");
        }
        return new ResponseEntity<>(EntityFromDTOConverter.chargingSessionDTOFromReservation(reservationOptional.get()), HttpStatus.OK);
    }

    @GetMapping("/ocpi/cpo/tariffs")
    public ResponseEntity<Iterable<Tariff>> getTariffs(@RequestParam(required = false) LocalDateTime dateFrom,
                                                       @RequestParam(required = false) LocalDateTime dateTo,
                                                       @RequestParam(defaultValue = "0") Integer offset,
                                                       @RequestParam(defaultValue = "100") Integer limit) {
        Page<Tariff> tariffs;
        if (dateFrom == null) {
            tariffs = chargingPointService.findTariffs(offset, limit);
        } else {
            tariffs = chargingPointService.findTariffsBetween(dateFrom, dateTo, offset, limit);
        }
        return new ResponseEntity<>(tariffs, HttpStatus.OK);
    }
}
