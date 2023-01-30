package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspSocketDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Socket;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.EmspDetailsService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiLocationsSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SocketStatusChangeEvent;

import java.util.List;
import java.util.Optional;

@Component
public class SocketStatusListener implements ApplicationListener<SocketStatusChangeEvent> {
    private final SocketService socketService;
    private final OcpiLocationsSender ocpiLocationsSender;
    private final EmspDetailsService emspDetailsService;

    @Autowired
    public SocketStatusListener(SocketService socketService, OcpiLocationsSender ocpiLocationsSender, EmspDetailsService emspDetailsService) {
        this.socketService = socketService;
        this.ocpiLocationsSender = ocpiLocationsSender;
        this.emspDetailsService = emspDetailsService;
    }

    @Override
    public void onApplicationEvent(SocketStatusChangeEvent event) {
        socketService.updateSocketStatus(event.getCpId(), event.getSocketId(), event.getNewStatus());
        onSocketUpdate(event.getCpId(), event.getSocketId());
    }

    public void onSocketUpdate(String cpId, Integer socketId) {
        List<EmspDetails> emspDetailsList = emspDetailsService.findAll();
        Optional<Socket> socketOptional = socketService.findSocketByCpIdAndSocketId(cpId, socketId);
        socketOptional.ifPresent(socket -> {
            EmspSocketDTO emspSocketDTO = EntityFromDTOConverter.emspSocketDTOFromSocket(socket);
            for (EmspDetails emspDetails: emspDetailsList) {
                ocpiLocationsSender.patchSocket(emspDetails, emspSocketDTO, cpId, true);
            }
        });
    }
}
