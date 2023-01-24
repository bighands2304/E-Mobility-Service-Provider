package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.DeletedReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class SessionCreator {
    @Autowired
    ReservationService reservationService;

    @PostMapping("/startChargingSession")
    public ResponseEntity<?> startChargingSession(@RequestBody Map<String,String> payload){
        Reservation reservation = reservationService.getReservationById(Long.parseLong(payload.get("reservationId")));
        if (reservation instanceof ActiveReservation) {
            ActiveReservation activeReservation = (ActiveReservation) reservation;
            //TODO questo va preso dal cpms
            //activeReservation.setSessionId(Long.parseLong(payload.get("sessionId")));
            reservationService.save(activeReservation);
            reservationService.delete(reservation);
            return ResponseEntity.ok(activeReservation);
            //TODO send informations of reservation to CPMS
        }else {
            return ResponseEntity.badRequest().body("Reservation is not an active reservation");
        }

    }
}
