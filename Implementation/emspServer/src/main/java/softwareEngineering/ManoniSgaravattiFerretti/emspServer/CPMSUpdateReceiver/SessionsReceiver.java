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

        reservation.setStartTime(session.getStartDateTime());
        reservation.setId(session.getReservationId());
        reservation.setSessionId(Long.parseLong(session_id));
        reservation.setSocketId(session.getSocketId());
        reservation.setBatteryPercentage(session.getBatteryPercentage());

        reservationService.save(reservation);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{session_id}")
    public ResponseEntity<?> patchSession(@PathVariable String session_id, @RequestBody SessionDTO session){
        if (session.getStatus().equals("ACTIVE")) {
            Reservation reservation = reservationService.getReservationById(session.getReservationId());

            reservation.setStartTime(session.getStartDateTime());
            reservation.setId(session.getReservationId());
            reservation.setSessionId(Long.parseLong(session_id));
            reservation.setSocketId(session.getSocketId());
            reservation.setBatteryPercentage(session.getBatteryPercentage());

            reservationService.save(reservation);
        }

        if (session.getStatus().equals("RESERVATION")) {
            Reservation reservation = reservationService.getReservationById(session.getReservationId());

            reservation.setStartTime(session.getStartDateTime());
            reservation.setId(session.getReservationId());
            reservation.setSessionId(Long.parseLong(session.getSessionId()));
            reservation.setSocketId(session.getSocketId());
            reservation.setBatteryPercentage(session.getBatteryPercentage());

            reservationService.save(reservation);
        }

        if (session.getStatus().equals("COMPLETED")) {
            Reservation reservation = reservationService.getReservationById(session.getReservationId());

            reservation.setStartTime(session.getStartDateTime());
            reservation.setId(session.getReservationId());
            reservation.setSessionId(Long.parseLong(session.getSessionId()));
            reservation.setSocketId(session.getSocketId());
            reservation.setBatteryPercentage(session.getBatteryPercentage());

            reservation.setEndTime(session.getEndDateTime());
            reservation.setEnergyAmount(session.getKwh());
            reservation.setPrice(session.getTotalCost());

            reservationService.save(reservation);
        }

        if (session.getStatus().equals("INVALID") || session.getStatus().equals("DELETED")) {
            Reservation reservation = reservationService.getReservationById(session.getReservationId());

            reservation.setStartTime(session.getStartDateTime());
            reservation.setId(session.getReservationId());
            reservation.setSocketId(session.getSocketId());
            reservation.setBatteryPercentage(session.getBatteryPercentage());

            reservation.setDeletionTime(session.getEndDateTime());

            reservationService.save(reservation);
        }

        return ResponseEntity.noContent().build();
    }
}
