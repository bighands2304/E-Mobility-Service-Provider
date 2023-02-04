package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.SocketService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpManager.PriceManager;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiSessionSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.SessionStoppedEvent;

import java.util.Optional;

@Component
public class SessionStoppedListener implements ApplicationListener<SessionStoppedEvent> {
    private final SocketService socketService;
    private final OcpiSessionSender ocpiSessionSender;
    private final ReservationService reservationService;
    private final SocketStatusListener socketStatusListener;
    private final PriceManager priceManager;

    @Autowired
    public SessionStoppedListener(SocketService socketService,
                                  OcpiSessionSender ocpiSessionSender,
                                  ReservationService reservationService,
                                  SocketStatusListener socketStatusListener,
                                  PriceManager priceManager) {
        this.socketService = socketService;
        this.ocpiSessionSender = ocpiSessionSender;
        this.reservationService = reservationService;
        this.socketStatusListener = socketStatusListener;
        this.priceManager = priceManager;
    }

    @Override
    public void onApplicationEvent(SessionStoppedEvent event) {
        Optional<Reservation> reservationOptional = reservationService.findReservationByInternalId(event.getReservationId());
        if (reservationOptional.isEmpty()) {
            return;
        }
        reservationService.updateReservationStatus(reservationOptional.get().getInternalReservationId(),
                "COMPLETED", event.getTime());
        String cpId = reservationOptional.get().getSocket().getCpId();
        Integer socketId = reservationOptional.get().getSocket().getSocketId();
        socketService.updateSocketStatus(cpId, socketId, "AVAILABLE");
        Reservation reservation = reservationOptional.get();
        reservation.setStatus("COMPLETED");
        reservation.setEndTime(event.getTime());
        reservation.setLastUpdated(event.getTime());
        reservation.setTotalCost(priceManager.applyTariff(event.getReservationId(), reservation.getSocket().getCpId()));
        ocpiSessionSender.patchSession(EntityFromDTOConverter.chargingSessionDTOFromReservation(reservation),
                reservation.getEmspDetails());
        reservationService.insertReservation(reservation);
        socketStatusListener.onSocketUpdate(cpId, socketId);
    }
}
