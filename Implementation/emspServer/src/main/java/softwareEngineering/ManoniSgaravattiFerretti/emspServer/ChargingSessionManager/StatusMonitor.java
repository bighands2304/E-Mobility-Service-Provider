package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.CommandsSender;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;

import java.util.Map;

@RestController("/user")
public class StatusMonitor {
    @Autowired
    ReservationService reservationService;
    @Autowired
    CommandsSender commandsSender;

    @PostMapping("/endSession")
    public ResponseEntity<?> endSession(@RequestBody Map<String,String> payload){
        Reservation reservation = reservationService.getReservationById(Long.parseLong(payload.get("reservationId")));
        if(reservation.getType().equals("ACTIVE")) {
            //Send stop session to the CPMS and save ended reservation in DB
            if (commandsSender.stopSession(reservation).getStatusCode().is2xxSuccessful()){
                reservationService.save(reservation);
                return ResponseEntity.ok(reservation);
            }else {
                return ResponseEntity.badRequest().body("Error ending the session");
            }
        }else {
            return ResponseEntity.badRequest().body("That's not an active session");
        }
    }
}
