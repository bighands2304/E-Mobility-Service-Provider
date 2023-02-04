package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.CommandsSender;
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
    @Autowired
    CommandsSender commandsSender;

    @PostMapping("/endReservation")
    public ResponseEntity<?> endReservation(@RequestBody Map<String,String> payload){
        Reservation reservation = reservationService.getReservationById(Long.parseLong(payload.get("reservationId")));
        if(reservation instanceof ActiveReservation activeReservation) {
            if(activeReservation.getSessionId()!=null){
                //Send stop session to the CPMS and save ended reservation in DB
                if (commandsSender.stopSession(activeReservation).getStatusCode().is2xxSuccessful()){
                    EndedReservation endedReservation = (EndedReservation) reservation;
                    reservationService.save(endedReservation);
                    return ResponseEntity.ok(endedReservation);
                }else {
                    return ResponseEntity.badRequest().body("Error ending the session");
                }
            }else {
                return ResponseEntity.badRequest().body("That's not an active session");
            }
        }else {
            return ResponseEntity.badRequest().body("That's not an active session");
        }
    }
}
