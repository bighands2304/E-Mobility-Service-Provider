package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.EMSPUpdateSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SocketAvailabilityEvent;

@Component
public class SocketAvailabilityListener implements ApplicationListener<SocketAvailabilityEvent> {
    private final SocketService socketService;
    private final EMSPUpdateSender emspUpdateSender;

    @Autowired
    public SocketAvailabilityListener(SocketService socketService, EMSPUpdateSender emspUpdateSender) {
        this.socketService = socketService;
        this.emspUpdateSender = emspUpdateSender;
    }

    @Override
    public void onApplicationEvent(SocketAvailabilityEvent event) {
        socketService.updateSocketAvailability(event.getChangeSocketAvailabilityDTO(), event.getCpId(), event.getSocketId());
        // todo: update emsp
    }
}
