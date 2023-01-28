package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.EMSPUpdateSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SocketStatusChangeEvent;

@Component
public class SocketStatusListener implements ApplicationListener<SocketStatusChangeEvent> {
    private final SocketService socketService;
    private final EMSPUpdateSender emspUpdateSender;

    @Autowired
    public SocketStatusListener(SocketService socketService, EMSPUpdateSender emspUpdateSender) {
        this.socketService = socketService;
        this.emspUpdateSender = emspUpdateSender;
    }

    @Override
    public void onApplicationEvent(SocketStatusChangeEvent event) {
        socketService.updateSocketStatus(event.getCpId(), event.getSocketId(), event.getNewStatus());
        // todo: send to emsp update sender
    }
}
