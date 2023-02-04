package softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Reservation;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.ReservationService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.utils.EntityFromDTOConverter;
import softwareengineering.manonisgaravattiferretti.cpmsServer.cpHandler.messages.chargingPointReq.dtos.MeterValue;
import softwareengineering.manonisgaravattiferretti.cpmsServer.emspUpdateSender.OcpiSessionSender;
import softwareengineering.manonisgaravattiferretti.cpmsServer.socketStatusManager.events.MeterValueEvent;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class MeterValueListener implements ApplicationListener<MeterValueEvent> {
    private final OcpiSessionSender ocpiSessionSender;
    private final ReservationService reservationService;

    @Autowired
    public MeterValueListener(OcpiSessionSender ocpiSessionSender, ReservationService reservationService) {
        this.ocpiSessionSender = ocpiSessionSender;
        this.reservationService = reservationService;
    }

    @Override
    public void onApplicationEvent(MeterValueEvent event) {
        Optional<Reservation> reservationOptional = reservationService.findReservationByInternalId(event.getReservationId());
        if (reservationOptional.isEmpty()) {
            return;
        }
        Double energyConsumed = event.getMeterValues().stream()
                .map(MeterValue::getSampledValue)
                .reduce(0.0, Double::sum);
        Reservation reservation = reservationOptional.get();
        reservation.setEnergyAmount(energyConsumed + reservation.getEnergyAmount());
        reservation.setLastUpdated(LocalDateTime.now());
        reservationService.insertReservation(reservation);
        ocpiSessionSender.patchSession(EntityFromDTOConverter.chargingSessionDTOFromReservation(reservation),
                reservation.getEmspDetails());
    }
}
