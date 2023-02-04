package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.WebSocketMessage;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.MeterValueEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SessionStartedEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SessionStoppedEvent;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SocketStatusChangeEvent;

import java.util.Map;

@Controller
public class OcppReceiver {
    private static final Logger logger = LoggerFactory.getLogger(OcppReceiver.class);
    private final ReservationService reservationService;
    private final SessionsManager sessionsManager;
    private final OcppSender ocppSender;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SimpMessagingTemplate template;

    @Autowired
    public OcppReceiver(ReservationService reservationService, SessionsManager sessionsManager, OcppSender ocppSender,
                        ApplicationEventPublisher applicationEventPublisher, SimpMessagingTemplate template) {
        this.reservationService = reservationService;
        this.sessionsManager = sessionsManager;
        this.ocppSender = ocppSender;
        this.applicationEventPublisher = applicationEventPublisher;
        this.template = template;
    }

    @MessageMapping("/ocpp")
    public void handleMessage(String messageJson) {
        logger.info("message " + messageJson);
    }

    @MessageMapping("/ocpp/BootNotification")
    public @ResponseBody BootNotificationConf handleBootNotification(@RequestBody BootNotificationReq request,
                                                                     SimpMessageHeaderAccessor headerAccessor) {
        logger.info("arrived boot notification message from session: " + headerAccessor.getSessionAttributes().get("sessionId"));
        //Map<String, Object> headers = Map.of("sessionId", headerAccessor.getSessionId());
        sessionsManager.updateSessionId(request.getCpId(), headerAccessor.getSessionId());
        //template.convertAndSendToUser(headerAccessor.getSessionId(), "topic/ocpp/BootNotification",
        //        new BootNotificationConf(headerAccessor.getSessionId()), headers);
        sessionsManager.getSessionIdFromChargingPointId(request.getCpId());
        return new BootNotificationConf((String) headerAccessor.getSessionAttributes().get("sessionId"));
    }

    @MessageMapping("/ocpp/StatusNotification")
    public @ResponseBody StatusNotificationConf handleStatusNotification(@RequestBody StatusNotificationReq request,
                                                                         SimpMessageHeaderAccessor headerAccessor) {
        SocketStatusChangeEvent socketStatusChangeEvent = new SocketStatusChangeEvent(this,
                sessionsManager.getChargingPointFromSession(headerAccessor.getSessionId()), request.getConnectorId(),
                request.getStatus(), request.getTimestamp());
        applicationEventPublisher.publishEvent(socketStatusChangeEvent);
        return new StatusNotificationConf();
    }

    @MessageMapping("/ocpp/MeterValue")
    public @ResponseBody MeterValueConf handleMeterValues(@RequestBody MeterValueReq request) {
        MeterValueEvent meterValueEvent = new MeterValueEvent(this, request.getTransactionId(),
                request.getConnectorId(), request.getMeterValue());
        applicationEventPublisher.publishEvent(meterValueEvent);
        return new MeterValueConf();
    }

    @MessageMapping("/ocpp/StartTransaction")
    public @ResponseBody StartTransactionConf handleStartTransaction(@RequestBody StartTransactionReq request,
                                                                     SimpMessageHeaderAccessor headerAccessor) {
        Long sessionId = reservationService.maxSessionId() + 1;
        String cpId = sessionsManager.getChargingPointFromSession(headerAccessor.getSessionId());
        SessionStartedEvent sessionStartedEvent = new SessionStartedEvent(this, request.getReservationId(),
                sessionId, request.getTimestamp(), cpId, request.getConnectorId());
        applicationEventPublisher.publishEvent(sessionStartedEvent);
        return new StartTransactionConf(sessionId);
    }

    @MessageMapping("/ocpp/StopTransaction")
    public @ResponseBody StopTransactionConf handleStopTransaction(@RequestBody StopTransactionReq request) {
        SessionStoppedEvent sessionStoppedEvent = new SessionStoppedEvent(this, request.getTransactionId(),
                request.getTimestamp(), request.getTransactionData());
        applicationEventPublisher.publishEvent(sessionStoppedEvent);
        return new StopTransactionConf();
    }

    @MessageMapping("/ocpp/ChangeAvailabilityConf")
    public void handleChangeAvailabilityConf(@RequestBody ChangeAvailabilityConf changeAvailabilityConf) {
        ocppSender.completeRequest(changeAvailabilityConf, changeAvailabilityConf.getRequestId());
    }

    @MessageMapping("/ocpp/ClearChargingProfileConf")
    public void handleClearChargingProfileConf(@RequestBody ClearChargingProfileConf clearChargingProfileConf) {
        ocppSender.completeRequest(clearChargingProfileConf, clearChargingProfileConf.getRequestId());
    }

    @MessageMapping("/ocpp/CancelReservationConf")
    public void handleCancelReservationConf(@RequestBody CancelReservationConf cancelReservationConf) {
        ocppSender.completeRequest(cancelReservationConf, cancelReservationConf.getRequestId());
    }

    @MessageMapping("/ocpp/RemoteStartTransactionConf")
    public void handleRemoteStartTransactionConf(@RequestBody RemoteStartTransactionConf remoteStartTransactionConf) {
        ocppSender.completeRequest(remoteStartTransactionConf, remoteStartTransactionConf.getRequestId());
    }

    @MessageMapping("/ocpp/RemoteStopTransactionConf")
    public void handleRemoteStopTransactionConf(@RequestBody RemoteStopTransactionConf remoteStopTransactionConf) {
        ocppSender.completeRequest(remoteStopTransactionConf, remoteStopTransactionConf.getRequestId());
    }

    @MessageMapping("/ocpp/ReserveNowConf")
    public void handleReserveNowConf(@RequestBody ReserveNowConf reserveNowConf) {
        logger.info("Received reserve now");
        ocppSender.completeRequest(reserveNowConf, reserveNowConf.getRequestId());
    }

    @MessageMapping("/ocpp/SetChargingProfileConf")
    public void handleSetChargingProfileConf(@RequestBody SetChargingProfileConf setChargingProfileConf) {
        ocppSender.completeRequest(setChargingProfileConf, setChargingProfileConf.getRequestId());
    }
}
