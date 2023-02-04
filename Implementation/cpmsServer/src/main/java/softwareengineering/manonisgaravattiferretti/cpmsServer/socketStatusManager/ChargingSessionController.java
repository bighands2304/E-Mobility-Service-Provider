package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager;

import jakarta.validation.Valid;
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
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.StartSessionDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.OcppSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.ConfMessage;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.RemoteStartTransactionConf;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.RemoteStopTransactionConf;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiCommandSender;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ChargingSessionController {
    private final ReservationService reservationService;
    private final OcppSender ocppSender;
    private final ChargingPointCommandResponseDispatcher chargingPointResponseDispatcher;

    public ChargingSessionController(ReservationService reservationService, OcppSender ocppSender,
                                     ChargingPointCommandResponseDispatcher chargingPointResponseDispatcher) {
        this.reservationService = reservationService;
        this.ocppSender = ocppSender;
        this.chargingPointResponseDispatcher = chargingPointResponseDispatcher;
    }

    @PostMapping("/ocpi/cpo/commands/START_SESSION")
    public ResponseEntity<?> startSession(@RequestBody @Valid StartSessionDTO startSessionDTO,
                                          @AuthenticationPrincipal EmspDetails emspDetails) {
        Optional<Reservation> reservationOptional = reservationService.findReservationByEmspId(
                startSessionDTO.getReservationId(), emspDetails);
        if (reservationOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "reservation not found");
        }
        if (!reservationOptional.get().getStatus().equals("RESERVED") ||
                !reservationOptional.get().getSocket().getCpId().equals(startSessionDTO.getChargingPointId()) ||
                !reservationOptional.get().getSocket().getSocketId().equals(startSessionDTO.getSocketId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reservation status error");
        }
        // Todo: select a charging profile?
        CompletableFuture<ConfMessage> responseFuture = ocppSender.sendRemoteStartTransaction(
                startSessionDTO.getChargingPointId(), startSessionDTO.getSocketId(),
                null, reservationOptional.get().getInternalReservationId());
        chargingPointResponseDispatcher.sendStartSessionResponse(responseFuture, emspDetails,
                reservationOptional.get().getReservationIdEmsp());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ocpi/cpo/commands/STOP_SESSION/{sessionId}")
    public ResponseEntity<?> stopSession(@PathVariable Long sessionId,
                                         @AuthenticationPrincipal EmspDetails emspDetails) {
        Optional<Reservation> reservationOptional = reservationService.findReservationBySessionId(sessionId);
        if (reservationOptional.isEmpty() ||
                !reservationOptional.get().getEmspDetails().getEmspToken().equals(emspDetails.getEmspToken())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "session not found");
        }
        if (!reservationOptional.get().getStatus().equals("ACTIVE")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "session is already finished");
        }
        CompletableFuture<ConfMessage> responseFuture = ocppSender.sendRemoteStopTransaction(
                reservationOptional.get().getSocket().getCpId(), reservationOptional.get().getInternalReservationId());
        chargingPointResponseDispatcher.sendStopSessionResponse(responseFuture, emspDetails,
                reservationOptional.get().getReservationIdEmsp());
        return ResponseEntity.ok().build();
    }
}
