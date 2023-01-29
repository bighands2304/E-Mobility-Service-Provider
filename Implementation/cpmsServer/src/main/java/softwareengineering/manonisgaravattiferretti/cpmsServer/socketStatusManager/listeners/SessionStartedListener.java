package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiSessionSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SessionStartedEvent;

import java.util.Optional;

@Component
public class SessionStartedListener implements ApplicationListener<SessionStartedEvent> {
    private final SocketService socketService;
    private final OcpiSessionSender ocpiSessionSender;
    private final ReservationService reservationService;
    private final SocketStatusListener socketStatusListener;

    @Autowired
    public SessionStartedListener(SocketService socketService, OcpiSessionSender ocpiSessionSender,
                                  ReservationService reservationService, SocketStatusListener socketStatusListener) {
        this.socketService = socketService;
        this.ocpiSessionSender = ocpiSessionSender;
        this.reservationService = reservationService;
        this.socketStatusListener = socketStatusListener;
    }
    @Override
    public void onApplicationEvent(SessionStartedEvent event) {
        Optional<Reservation> reservationOptional = reservationService.findReservationBySessionId(event.getSessionId());
        if (reservationOptional.isEmpty()) {
            return;
        }
        socketService.updateSocketStatus(event.getCpId(), event.getSocketId(), "CHARGING");
        reservationService.updateReservationStatus(reservationOptional.get().getInternalReservationId(),
                "ACTIVE", event.getTime());
        String cpId = reservationOptional.get().getSocket().getCpId();
        Integer socketId = reservationOptional.get().getSocket().getSocketId();
        Reservation reservation = reservationOptional.get();
        reservation.setStatus("ACTIVE");
        reservation.setLastUpdated(event.getTime());
        ocpiSessionSender.putSession(EntityFromDTOConverter.chargingSessionDTOFromReservation(reservation),
                reservation.getEmspDetails());
        socketStatusListener.onSocketUpdate(cpId, socketId);
    }
}
