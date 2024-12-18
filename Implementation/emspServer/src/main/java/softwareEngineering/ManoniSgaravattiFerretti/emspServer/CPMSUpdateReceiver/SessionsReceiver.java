package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.SessionDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;

@RestController
@RequestMapping("/ocpi/emsp/sessions")
public class SessionsReceiver {
    @Autowired
    ReservationService reservationService;

    @PutMapping("/{session_id}")
    public ResponseEntity<?> putSession(@PathVariable String session_id, @RequestBody SessionDTO session){
        Reservation reservation = reservationService.getReservationById(session.getReservationId());

        reservation.setSessionId(Long.parseLong(session_id));
        reservation.setBatteryPercentage(session.getBatteryPercentage());

        reservationService.save(reservation);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{session_id}")
    public ResponseEntity<?> patchSession(@PathVariable Long session_id, @RequestBody SessionDTO session){
        Reservation reservation = reservationService.getSessionById(session_id);
        if (session.getStatus().equals("ACTIVE")) {

            reservation.setSessionId(session_id);
            reservation.setBatteryPercentage(session.getBatteryPercentage());

            reservationService.save(reservation);
        }

        if (session.getStatus().equals("RESERVATION")) {}

        if (session.getStatus().equals("COMPLETED")) {
            reservation.setEndTime(session.getEndDateTime());
            reservation.setEnergyAmount(session.getKwh());
            reservation.setPrice(session.getTotalCost());

            reservationService.save(reservation);
        }

        if (session.getStatus().equals("INVALID") || session.getStatus().equals("DELETED")) {
            reservation.setDeletionTime(session.getEndDateTime());

            reservationService.save(reservation);
        }

        return ResponseEntity.noContent().build();
    }
}
