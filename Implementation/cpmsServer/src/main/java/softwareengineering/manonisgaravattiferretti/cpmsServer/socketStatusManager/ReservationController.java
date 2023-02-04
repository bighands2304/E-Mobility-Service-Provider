package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CommandResultType;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ReserveNowDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.OcppSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.CancelReservationConf;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.ConfMessage;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.ReserveNowConf;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.ReservationStatus;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiCommandSender;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
public class ReservationController {
    private final ReservationService reservationService;
    private final SocketService socketService;
    private final OcppSender ocppSender;
    private final ChargingPointCommandResponseDispatcher chargingPointResponseDispatcher;
    private final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    public ReservationController(ReservationService reservationService, SocketService socketService, OcppSender ocppSender,
                                 ChargingPointCommandResponseDispatcher chargingPointResponseDispatcher) {
        this.reservationService = reservationService;
        this.socketService = socketService;
        this.ocppSender = ocppSender;
        this.chargingPointResponseDispatcher = chargingPointResponseDispatcher;
    }

    @PostMapping("/ocpi/cpo/commands/RESERVE_NOW")
    public ResponseEntity<?> reserveNow(@RequestBody @Valid ReserveNowDTO reserveNowDTO,
                                        @AuthenticationPrincipal EmspDetails emspDetails) {
        Optional<Socket> socketOptional = socketService.findSocketByCpIdAndSocketId(reserveNowDTO.getChargingPointId(),
                reserveNowDTO.getSocketId());
        if (socketOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cp or socket not found");
        }
        if (!socketOptional.get().getAvailability().equals("AVAILABLE")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "socket not available");
        }
        logger.info("Received RESERVE_NOW command, all parameters are ok");
        long id = reservationService.getReservationsCount() + 1;
        logger.info("Assigned an  internalId = {} to the reservation", id);
        logger.info("Sending an asynchronous message to the charging point");
        CompletableFuture<ConfMessage> responseFuture = ocppSender.sendReserveNow(reserveNowDTO.getChargingPointId(), id,
                reserveNowDTO.getExpiryDate(), reserveNowDTO.getSocketId());
        chargingPointResponseDispatcher.sendReserveNowResponse(responseFuture, socketOptional.get(),
                id, reserveNowDTO, emspDetails);
        logger.info("Sending response to the emsp");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ocpi/cpo/commands/CANCEL_RESERVATION/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id, @AuthenticationPrincipal EmspDetails emspDetails) {
        Optional<Reservation> reservationOptional = reservationService.findReservationByEmspId(id, emspDetails);
        // todo: check expiry date (set invalid)
        if (reservationOptional.isEmpty()) {
            logger.info("Received CANCEL_RESERVATION command, the reservation was not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "reservation not found");
        }
        logger.info("Received CANCEL_RESERVATION command, all parameters are ok");
        if (!reservationOptional.get().getStatus().equals("RESERVED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot delete reservation");
        }
        String cpId = reservationOptional.get().getSocket().getCpId();
        Long reservationId = reservationOptional.get().getInternalReservationId();
        CompletableFuture<ConfMessage> responseFuture = ocppSender.sendCancelReservation(cpId, reservationId);
        chargingPointResponseDispatcher.sendCancelReservationResponse(responseFuture, cpId, id,
                reservationOptional.get().getSocket().getSocketId(),
                reservationOptional.get().getInternalReservationId(), emspDetails);
        logger.info("Sending response to the emsp");
        return ResponseEntity.ok().build();
    }
}
