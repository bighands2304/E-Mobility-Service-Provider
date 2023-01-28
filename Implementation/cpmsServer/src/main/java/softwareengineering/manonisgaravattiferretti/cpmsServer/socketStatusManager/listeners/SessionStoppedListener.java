package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.EMSPUpdateSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SessionStoppedEvent;

import java.util.Optional;

@Component
public class SessionStoppedListener implements ApplicationListener<SessionStoppedEvent> {
    private final SocketService socketService;
    private final EMSPUpdateSender emspUpdateSender;
    private final ReservationService reservationService;

    @Autowired
    public SessionStoppedListener(SocketService socketService, EMSPUpdateSender emspUpdateSender, ReservationService reservationService) {
        this.socketService = socketService;
        this.emspUpdateSender = emspUpdateSender;
        this.reservationService = reservationService;
    }

    @Override
    public void onApplicationEvent(SessionStoppedEvent event) {
        Optional<Reservation> reservationOptional = reservationService.findReservationBySessionId(event.getSessionId());
        if (reservationOptional.isEmpty()) {
            return;
        }
        reservationService.updateReservationStatus(reservationOptional.get().getInternalReservationId(),
                "ENDED", event.getTime());
        String cpId = reservationOptional.get().getSocket().getCpId();
        Integer socketId = reservationOptional.get().getSocket().getSocketId();
        socketService.updateSocketStatus(cpId, socketId, "AVAILABLE");
        //todo: send update to emsp
    }
}
