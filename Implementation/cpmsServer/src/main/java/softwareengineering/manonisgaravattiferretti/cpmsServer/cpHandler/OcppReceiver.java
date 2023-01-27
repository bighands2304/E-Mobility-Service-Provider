package softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.WebSocketSession;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ChargingPointService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.*;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.*;

@Controller
public class OcppReceiver {
    private static final Logger logger = LoggerFactory.getLogger(OcppReceiver.class);

    private final ChargingPointService chargingPointService;
    private final SessionsManager sessionsManager;
    private final OcppSender ocppSender;

    @Autowired
    public OcppReceiver(ChargingPointService chargingPointService, SessionsManager sessionsManager, OcppSender ocppSender) {
        this.chargingPointService = chargingPointService;
        this.sessionsManager = sessionsManager;
        this.ocppSender = ocppSender;
    }

    @MessageMapping("/ocpp/BootNotification")
    public @ResponseBody BootNotificationConf handleBootNotification(BootNotificationReq request, WebSocketSession socketSession) {
        //BootNotificationConf response = chargingPointService.handleBootNotification(request);
        sessionsManager.registerSession(socketSession.getId(), socketSession);
        // Todo
        return null;
    }

    @MessageMapping("/ocpp/StatusNotification")
    public @ResponseBody StatusNotificationConf handleStatusNotification(StatusNotificationReq request) {
        //chargingPointService.handleStatusNotification(request);
        // Todo
        return null;
    }

    @MessageMapping("/ocpp/MeterValues")
    @SendTo("/topic/messages")
    public @ResponseBody MeterValueConf handleMeterValues(MeterValueReq request) {
        //chargingPointService.handleMeterValues(request);
        // Todo
        return null;
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
