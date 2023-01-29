package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.CommandsSender;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class SessionCreator {
    @Autowired
    ReservationService reservationService;
    @Autowired
    ChargingPointService cpService;
    @Autowired
    CommandsSender commandsSender;


    @PostMapping("/startChargingSession")
    public ResponseEntity<?> startChargingSession(@RequestBody Map<String,String> payload){
        Reservation reservation = reservationService.getReservationById(Long.parseLong(payload.get("reservationId")));
        ChargingPoint cp = cpService.getCPById(payload.get("cpId"));
        if (reservation instanceof ActiveReservation) {
            ActiveReservation activeReservation = (ActiveReservation) reservation;
            //send start request to the CPMS
            commandsSender.startSession(activeReservation, cp);
            reservationService.save(activeReservation);
            reservationService.delete(reservation);
            return ResponseEntity.ok(activeReservation);

        }else {
            return ResponseEntity.badRequest().body("Reservation is not an active reservation");
        }

    }
}
