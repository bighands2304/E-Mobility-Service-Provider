package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.WebSocketSession;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.MeterValueEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SessionStartedEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SessionStoppedEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SocketStatusChangeEvent;

@Controller
public class OcppReceiver {
    private static final Logger logger = LoggerFactory.getLogger(OcppReceiver.class);
    private final ReservationService reservationService;
    private final SessionsManager sessionsManager;
    private final OcppSender ocppSender;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public OcppReceiver(ReservationService reservationService, SessionsManager sessionsManager, OcppSender ocppSender,
                        ApplicationEventPublisher applicationEventPublisher) {
        this.reservationService = reservationService;
        this.sessionsManager = sessionsManager;
        this.ocppSender = ocppSender;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @MessageMapping("/ocpp/BootNotification")
    public @ResponseBody BootNotificationConf handleBootNotification(BootNotificationReq request, WebSocketSession socketSession) {
        sessionsManager.registerSession(socketSession.getId(), socketSession);
        return new BootNotificationConf();
    }

    @MessageMapping("/ocpp/StatusNotification")
    public @ResponseBody StatusNotificationConf handleStatusNotification(StatusNotificationReq request, WebSocketSession socketSession) {
        SocketStatusChangeEvent socketStatusChangeEvent = new SocketStatusChangeEvent(this,
                sessionsManager.getChargingPointFromSession(socketSession.getId()), request.getConnectorId(),
                request.getStatus(), request.getTimestamp());
        applicationEventPublisher.publishEvent(socketStatusChangeEvent);
        return new StatusNotificationConf();
    }

    @MessageMapping("/ocpp/MeterValues")
    public @ResponseBody MeterValueConf handleMeterValues(MeterValueReq request) {
        MeterValueEvent meterValueEvent = new MeterValueEvent(this, request.getTransactionId(),
                request.getConnectorId(), request.getMeterValue());
        applicationEventPublisher.publishEvent(meterValueEvent);
        return new MeterValueConf();
    }

    @MessageMapping("/ocpp/StartTransaction")
    public @ResponseBody StartTransactionConf handleStartTransaction(StartTransactionReq request, WebSocketSession socketSession) {
        Long sessionId = reservationService.maxSessionId() + 1;
        String cpId = sessionsManager.getChargingPointFromSession(socketSession.getId());
        SessionStartedEvent sessionStartedEvent = new SessionStartedEvent(this, request.getReservationId(),
                sessionId, request.getTimestamp(), cpId, request.getConnectorId());
        applicationEventPublisher.publishEvent(sessionStartedEvent);
        return new StartTransactionConf(sessionId);
    }

    @MessageMapping("/ocpp/StopTransaction")
    public @ResponseBody StopTransactionConf handleStopTransaction(StopTransactionReq request) {
        SessionStoppedEvent sessionStoppedEvent = new SessionStoppedEvent(this, request.getTransactionId(),
                request.getTimestamp(), request.getTransactionData());
        applicationEventPublisher.publishEvent(sessionStoppedEvent);
        return new StopTransactionConf();
    }

    @MessageMapping("/ocpp/ChangeAvailabilityConf")
    public void handleChangeAvailabilityConf(ChangeAvailabilityConf changeAvailabilityConf, @Header("requestId") String reqId) {
        ocppSender.completeRequest(changeAvailabilityConf, reqId);
    }

    @MessageMapping("/ocpp/ClearChargingProfileConf")
    public void handleClearChargingProfileConf(ClearChargingProfileConf clearChargingProfileConf, @Header("requestId") String reqId) {
        ocppSender.completeRequest(clearChargingProfileConf, reqId);
    }

    @MessageMapping("/ocpp/CancelReservationConf")
    public void handleCancelReservationConf(CancelReservationConf cancelReservationConf, @Header("requestId") String reqId) {
        ocppSender.completeRequest(cancelReservationConf, reqId);
    }

    @MessageMapping("/ocpp/RemoteStartTransactionConf")
    public void handleRemoteStartTransactionConf(RemoteStartTransactionConf remoteStartTransactionConf, @Header("requestId") String reqId) {
        ocppSender.completeRequest(remoteStartTransactionConf, reqId);
    }

    @MessageMapping("/ocpp/RemoteStopTransactionConf")
    public void handleRemoteStopTransactionConf(RemoteStopTransactionConf remoteStopTransactionConf, @Header("requestId") String reqId) {
        ocppSender.completeRequest(remoteStopTransactionConf, reqId);
    }

    @MessageMapping("/ocpp/ReserveNowConf")
    public void handleReserveNowConf(ReserveNowConf reserveNowConf, @Header("requestId") String reqId) {
        ocppSender.completeRequest(reserveNowConf, reqId);
    }

    @MessageMapping("/ocpp/SetChargingProfileConf")
    public void handleSetChargingProfileConf(SetChargingProfileConf setChargingProfileConf, @Header("requestId") String reqId) {
        ocppSender.completeRequest(setChargingProfileConf, reqId);
    }
}
