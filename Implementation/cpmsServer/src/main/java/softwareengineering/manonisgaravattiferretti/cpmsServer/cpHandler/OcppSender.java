package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingProfile;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.AvailabilityType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class OcppSender {
    private final SessionsManager sessionsManager;
    private final SimpMessagingTemplate template;
    private final Map<String, CompletableFuture<ConfMessage>> pendingResponses = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(OcppSender.class);

    @Autowired
    public OcppSender(SessionsManager sessionsManager, SimpMessagingTemplate template) {
        this.sessionsManager = sessionsManager;
        this.template = template;
    }

    public CompletableFuture<ConfMessage> sendChangeAvailability(String cpId, Integer socketId, Boolean available) {
        ChangeAvailabilityReq changeAvailabilityReq = new ChangeAvailabilityReq(
                socketId, AvailabilityType.getFromBoolean(available));
        return send(changeAvailabilityReq, cpId, "ChangeAvailability");
    }

    public CompletableFuture<ConfMessage> sendClearChargingProfile(String cpId) {
        return sendClearChargingProfile(cpId, 0);
    }

    public CompletableFuture<ConfMessage> sendClearChargingProfile(String cpId, Integer socketId) {
        ClearChargingProfileReq request = new ClearChargingProfileReq(null, socketId);
        return send(request, cpId, "ClearChargingProfile");
    }

    public CompletableFuture<ConfMessage> sendCancelReservation(String cpId, Long reservationId) {
        CancelReservationReq request = new CancelReservationReq(reservationId);
        return send(request, cpId, "CancelReservation");
    }

    public CompletableFuture<ConfMessage> sendRemoteStartTransaction(String cpId, Integer socketId,
                                                                     ChargingProfile chargingProfile,
                                                                     Long reservationId) {
        RemoteStartTransactionReq request = new RemoteStartTransactionReq(socketId, reservationId, chargingProfile);
        return send(request, cpId, "RemoteStartTransaction");
    }

    public CompletableFuture<ConfMessage> sendRemoteStopTransaction(String cpId, Long transactionId) {
        RemoteStopTransactionReq request = new RemoteStopTransactionReq(transactionId);
        return send(request, cpId, "RemoteStopTransaction");
    }

    public CompletableFuture<ConfMessage> sendReserveNow(String cpId, Long reservationId,
                                                         LocalDateTime expiryDate, Integer socketId) {
        ReserveNowReq request = new ReserveNowReq(socketId, expiryDate, reservationId);
        return send(request, cpId, "ReserveNow");
    }

    public CompletableFuture<ConfMessage> sendSetChargingProfile(String cpId, Integer socketId,
                                                                 ChargingProfile chargingProfile) {
        SetChargingProfileReq request = new SetChargingProfileReq(socketId, chargingProfile);
        return send(request, cpId, "SetChargingProfile");
    }

    public void completeRequest(ConfMessage confMessage, String requestId) {
        if (pendingResponses.containsKey(requestId)) {
            CompletableFuture<ConfMessage> future = pendingResponses.get(requestId);
            pendingResponses.remove(requestId);
            future.complete(confMessage);
        }
    }

    private CompletableFuture<ConfMessage> send(Object message, String cpId, String messageName) {
        String requestId = UUID.randomUUID().toString();
        String sessionId = sessionsManager.getSessionIdFromChargingPointId(cpId);
        if (sessionId == null) {
            return CompletableFuture.failedFuture(new RuntimeException());
        }
        logger.info("Sending message " + messageName +" to session id" + sessionId);
        Map<String, Object> headers = Map.of("requestId", requestId, "sessionId", sessionId);
        template.convertAndSend("/topic/"+ cpId + messageName + "/topic/ocpp/" + messageName, message, headers);
        CompletableFuture<ConfMessage> futureResponse = new CompletableFuture<>();
        pendingResponses.put(requestId, futureResponse);
        return futureResponse;
    }
}
