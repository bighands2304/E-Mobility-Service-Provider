package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CommandResultType;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ReserveNowDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.ReservationStatus;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiCommandSender;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class ChargingPointCommandResponseDispatcher {
    private final OcpiCommandSender ocpiCommandSender;
    private final ReservationService reservationService;
    private final SocketService socketService;
    private final Logger logger = LoggerFactory.getLogger(ChargingPointCommandResponseDispatcher.class);

    @Autowired
    public ChargingPointCommandResponseDispatcher(OcpiCommandSender ocpiCommandSender,
                                                  ReservationService reservationService,
                                                  SocketService socketService) {
        this.ocpiCommandSender = ocpiCommandSender;
        this.reservationService = reservationService;
        this.socketService = socketService;
    }

    @Async
    void sendReserveNowResponse(CompletableFuture<ConfMessage> futureCpResponse, Socket socket, Long internalReservationId,
                                ReserveNowDTO reserveNowDTO, EmspDetails emspDetails) {
        CommandResultType commandResultType;
        try {
            ReserveNowConf response = (ReserveNowConf) futureCpResponse.orTimeout(5, TimeUnit.SECONDS).get();
            logger.info("response from the charging point: " + response);
            commandResultType = CommandResultType.getFromReservationStatus(response.getCommandResult());
            if (response.getCommandResult() == ReservationStatus.ACCEPTED) {
                socketService.updateSocketStatus(reserveNowDTO.getChargingPointId(), reserveNowDTO.getSocketId(), "RESERVED");
                reservationService.insertReservation(reserveNowDTO, internalReservationId, socket, emspDetails);
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.info("response from the charging point not received");
            commandResultType = CommandResultType.TIMEOUT;
        }
        ocpiCommandSender.sendCommandResult(emspDetails, reserveNowDTO.getReservationId(), commandResultType, "RESERVE_NOW");
        if (commandResultType == CommandResultType.ACCEPTED) {
            checkExpirationTime(internalReservationId, reserveNowDTO.getExpiryDate());
        }
    }

    private void checkExpirationTime(Long reservationInternalId, LocalDateTime expiryTime) {
        Date expiryDate = Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Optional<Reservation> reservation = reservationService.findReservationByInternalId(reservationInternalId);
                reservation.ifPresent(res -> {
                    // expiry date is reached, if the status is reserved then the reservation
                    // is invalid since it has not already started
                    if (res.getStatus().equals("RESERVED")) {
                        reservationService.updateReservationStatus(reservationInternalId, "INVALID", LocalDateTime.now());
                    }
                });
            }
        }, expiryDate);
    }

    @Async
    void sendCancelReservationResponse(CompletableFuture<ConfMessage> futureCpResponse, String cpId, Long emspReservationId,
                                       Integer socketId, Long internalReservationId, EmspDetails emspDetails) {
        CommandResultType commandResultType;
        try {
            CancelReservationConf response = (CancelReservationConf) futureCpResponse.get();
            commandResultType = CommandResultType.getFromCpCommandResult(response.getCommandResult());
            if (commandResultType == CommandResultType.ACCEPTED) {
                socketService.updateSocketStatus(cpId, socketId, "AVAILABLE");
                reservationService.updateReservationStatus(internalReservationId, "DELETED", LocalDateTime.now());
            }
        } catch (InterruptedException | ExecutionException e) {
            commandResultType = CommandResultType.TIMEOUT;
        }
        ocpiCommandSender.sendCommandResult(emspDetails, emspReservationId, commandResultType, "CANCEL_RESERVATION");
    }

    @Async
    void sendStartSessionResponse(CompletableFuture<ConfMessage> futureCpResponse, EmspDetails emspDetails, Long reservationId) {
        CommandResultType commandResultType;
        try {
            RemoteStartTransactionConf response = (RemoteStartTransactionConf) futureCpResponse.get();
            commandResultType = CommandResultType.getFromCpCommandResult(response.getCommandResult());
        } catch (InterruptedException | ExecutionException e) {
            commandResultType = CommandResultType.TIMEOUT;
        }
        ocpiCommandSender.sendCommandResult(emspDetails, reservationId, commandResultType, "START_SESSION");
    }

    @Async
    void sendStopSessionResponse(CompletableFuture<ConfMessage> futureCpResponse, EmspDetails emspDetails, Long reservationId) {
        CommandResultType commandResultType;
        try {
            RemoteStopTransactionConf response = (RemoteStopTransactionConf) futureCpResponse.get();
            commandResultType = CommandResultType.getFromCpCommandResult(response.getCommandResult());
        } catch (InterruptedException | ExecutionException e) {
            commandResultType = CommandResultType.TIMEOUT;
        }
        ocpiCommandSender.sendCommandResult(emspDetails, reservationId, commandResultType, "STOP_SESSION");
    }
}
