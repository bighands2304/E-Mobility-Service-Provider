package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.EMSPUpdateSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SessionStartedEvent;

@Component
public class SessionStartedListener implements ApplicationListener<SessionStartedEvent> {
    private final SocketService socketService;
    private final EMSPUpdateSender emspUpdateSender;
    private final ReservationService reservationService;

    @Autowired
    public SessionStartedListener(SocketService socketService, EMSPUpdateSender emspUpdateSender, ReservationService reservationService) {
        this.socketService = socketService;
        this.emspUpdateSender = emspUpdateSender;
        this.reservationService = reservationService;
    }
    @Override
    public void onApplicationEvent(SessionStartedEvent event) {
        socketService.updateSocketStatus(event.getCpId(), event.getSocketId(), "CHARGING");
        // todo: update emsp
    }
}
