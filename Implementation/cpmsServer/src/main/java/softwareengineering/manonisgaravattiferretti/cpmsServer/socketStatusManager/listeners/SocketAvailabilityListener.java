package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspSocketDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.EmspDetailsService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.OcppSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.ChangeAvailabilityConf;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.ConfMessage;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.cpmsReq.dtos.CommandResult;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiLocationsSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SocketAvailabilityEvent;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class SocketAvailabilityListener implements ApplicationListener<SocketAvailabilityEvent> {
    private final SocketService socketService;
    private final OcpiLocationsSender ocpiLocationsSender;
    private final OcppSender ocppSender;
    private final EmspDetailsService emspDetailsService;
    private final SocketStatusListener socketStatusListener;
    private final Logger logger = LoggerFactory.getLogger(SocketAvailabilityListener.class);

    @Autowired
    public SocketAvailabilityListener(SocketService socketService,
                                      OcpiLocationsSender ocpiLocationsSender,
                                      OcppSender ocppSender,
                                      EmspDetailsService emspDetailsService,
                                      SocketStatusListener socketStatusListener) {
        this.socketService = socketService;
        this.ocpiLocationsSender = ocpiLocationsSender;
        this.ocppSender = ocppSender;
        this.emspDetailsService = emspDetailsService;
        this.socketStatusListener = socketStatusListener;
    }

    @Override
    public void onApplicationEvent(SocketAvailabilityEvent event) {
        CompletableFuture<ConfMessage> futureResponse = ocppSender.sendChangeAvailability(event.getCpId(),
                event.getSocketId(), event.getChangeSocketAvailabilityDTO().getAvailable());
        socketService.updateSocketAvailability(event.getChangeSocketAvailabilityDTO(), event.getCpId(), event.getSocketId());
        ChangeAvailabilityConf changeAvailabilityConf;
        try {
            changeAvailabilityConf = (ChangeAvailabilityConf) futureResponse.orTimeout(120, TimeUnit.SECONDS).get();
            if (changeAvailabilityConf.getCommandResult() != CommandResult.ACCEPTED) {
                return;
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Response from charging point has not arrived");
            return;
        }
        socketStatusListener.onSocketUpdate(event.getCpId(), event.getSocketId());
    }
}
