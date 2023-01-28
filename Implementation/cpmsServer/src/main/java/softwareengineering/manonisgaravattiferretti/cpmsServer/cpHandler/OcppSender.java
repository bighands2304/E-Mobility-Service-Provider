package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler;

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

@Service
public class OcppSender {
    private final SessionsManager sessionsManager;
    private final SimpMessagingTemplate template;
    private final Map<String, CompletableFuture<ConfMessage>> pendingResponses = new ConcurrentHashMap<>();

    @Autowired
    public OcppSender(SessionsManager sessionsManager, SimpMessagingTemplate template) {
        this.sessionsManager = sessionsManager;
        this.template = template;
    }

    public CompletableFuture<ConfMessage> sendChangeAvailability(String cpId, Integer socketId, Boolean available) {
        ChangeAvailabilityReq changeAvailabilityReq = new ChangeAvailabilityReq(
                socketId, AvailabilityType.getFromBoolean(available));
        return send(changeAvailabilityReq, cpId, "ChangeAvailabilityConf");
    }

    public CompletableFuture<ConfMessage> sendClearChargingProfile(String cpId) {
        return sendClearChargingProfile(cpId, 0);
    }

    public CompletableFuture<ConfMessage> sendClearChargingProfile(String cpId, Integer socketId) {
        ClearChargingProfileReq request = new ClearChargingProfileReq(null, socketId);
        return send(request, cpId, "ClearChargingProfileConf");
    }

    public CompletableFuture<ConfMessage> sendCancelReservation(String cpId, Long reservationId) {
        CancelReservationReq request = new CancelReservationReq(reservationId);
        return send(request, cpId, "CancelReservationConf");
    }

    public CompletableFuture<ConfMessage> sendRemoteStartTransaction(String cpId, Integer socketId,
                                                                                    ChargingProfile chargingProfile) {
        RemoteStartTransactionReq request = new RemoteStartTransactionReq(socketId, chargingProfile);
        return send(request, cpId, "RemoteStartTransactionConf");
    }

    public CompletableFuture<ConfMessage> sendRemoteStopTransaction(String cpId, Long transactionId) {
        RemoteStopTransactionReq request = new RemoteStopTransactionReq(transactionId);
        return send(request, cpId, "RemoteStopTransactionConf");
    }

    public CompletableFuture<ConfMessage> sendReserveNow(String cpId, Long reservationId,
                                                         LocalDateTime expiryDate, Integer socketId) {
        ReserveNowReq request = new ReserveNowReq(socketId, expiryDate, reservationId);
        return send(request, cpId, "RemoteStopTransactionConf");
    }

    public CompletableFuture<ConfMessage> sendSetChargingProfile(String cpId, Integer socketId,
                                                                            ChargingProfile chargingProfile) {
        SetChargingProfileReq request = new SetChargingProfileReq(socketId, chargingProfile);
        return send(request, cpId, "RemoteStopTransactionConf");
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
        Map<String, Object> headers = Map.of("requestId", requestId);
        template.convertAndSendToUser(sessionId, "/ocpp/" + messageName, message, headers);
        CompletableFuture<ConfMessage> futureResponse = new CompletableFuture<>();
        pendingResponses.put(requestId, futureResponse);
        return futureResponse;
    }
}
