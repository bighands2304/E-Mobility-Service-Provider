package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.CommandResult;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.ReservationStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ReservationController {
    private final ReservationService reservationService;
    private final SocketService socketService;
    private final OcppSender ocppSender;

    @Autowired
    public ReservationController(ReservationService reservationService, SocketService socketService, OcppSender ocppSender) {
        this.reservationService = reservationService;
        this.socketService = socketService;
        this.ocppSender = ocppSender;
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
        long id = reservationService.getReservationsCount() + 1;
        CompletableFuture<ConfMessage> responseFuture = ocppSender.sendReserveNow(reserveNowDTO.getChargingPointId(), id,
                reserveNowDTO.getExpiryDate(), reserveNowDTO.getSocketId());
        try {
            ReserveNowConf response = (ReserveNowConf) responseFuture.get();
            if (response.getReservationStatus() != ReservationStatus.ACCEPTED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "socket not available");
            }
            socketService.updateSocketStatus(reserveNowDTO.getChargingPointId(), reserveNowDTO.getSocketId(), "RESERVED");
            reservationService.insertReservation(reserveNowDTO, id, socketOptional.get(), emspDetails);
        } catch (InterruptedException | ExecutionException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, try again later");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ocpi/cpo/commands/CANCEL_RESERVATION/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id, @AuthenticationPrincipal EmspDetails emspDetails) {
        Optional<Reservation> reservationOptional = reservationService.findReservationByEmspId(id, emspDetails);
        if (reservationOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "reservation not found");
        }
        String cpId = reservationOptional.get().getSocket().getCpId();
        Long reservationId = reservationOptional.get().getInternalReservationId();
        CompletableFuture<ConfMessage> responseFuture = ocppSender.sendCancelReservation(cpId, reservationId);
        try {
            CancelReservationConf response = (CancelReservationConf) responseFuture.get();
            if (response.getCommandResult() != CommandResult.ACCEPTED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "could not cancel the reservation");
            }
            socketService.updateSocketStatus(cpId, reservationOptional.get().getSocket().getSocketId(), "RESERVED");
            reservationService.updateReservationStatus(reservationOptional.get().getInternalReservationId(),
                    "DELETED", LocalDateTime.now());
        } catch (InterruptedException | ExecutionException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, try again later");
        }
        return ResponseEntity.ok().build();
    }
}
