package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.DeletedReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.EndedReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController("/user")
public class StatusMonitor {
    @Autowired
    ReservationService reservationService;

    @PostMapping("/deleteReservation")
    public ResponseEntity<?> deleteReservation(@RequestBody Map<String,String> payload){
        Reservation reservation = reservationService.getReservationById(Long.parseLong(payload.get("reservationId")));
        if (reservation instanceof ActiveReservation activeReservation) {
            if (activeReservation.getSessionId()==null) {
                DeletedReservation deletedReservation = (DeletedReservation) reservation;
                deletedReservation.setDeletionTime(LocalDateTime.now());
                //TODO sendDeletion to CPMS
                reservationService.save(deletedReservation);
                return ResponseEntity.ok(deletedReservation);
            }else {
                return ResponseEntity.badRequest().body("That's not an active reservation");
            }
        }else {
            return ResponseEntity.badRequest().body("That's not an active reservation");
        }
    }

    @PostMapping("/endReservation")
    public ResponseEntity<?> endReservation(@RequestBody Map<String,String> payload){
        Reservation reservation = reservationService.getReservationById(Long.parseLong(payload.get("reservationId")));
        if(reservation instanceof ActiveReservation) {
            if(((ActiveReservation) reservation).getSessionId()!=null){
                EndedReservation endedReservation = (EndedReservation) reservation;
                endedReservation.setEndTime(LocalDateTime.now());
                //TODO get those fields from CPMS
                //endedReservation.setPrice();
                //endedReservation.setEnergyAmount();
                reservationService.save(endedReservation);
                reservationService.delete(reservation);
                return ResponseEntity.ok(endedReservation);
            }else {
                return ResponseEntity.badRequest().body("That's not an active session");
            }
        }else {
            return ResponseEntity.badRequest().body("That's not an active session");
        }
    }
}
