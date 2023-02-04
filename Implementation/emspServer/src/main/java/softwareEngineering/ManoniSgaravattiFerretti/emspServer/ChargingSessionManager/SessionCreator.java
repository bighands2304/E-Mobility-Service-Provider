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
        if (reservation instanceof ActiveReservation activeReservation) {
            //Send startSession request to the CPMS and save the active reservation in the DB
            if(commandsSender.startSession(activeReservation).getStatusCode().is4xxClientError()){
                reservationService.save(activeReservation);
                return ResponseEntity.ok(activeReservation);
            }
            return ResponseEntity.badRequest().body("Impossible start the Charging Session");

        }else {
            return ResponseEntity.badRequest().body("Reservation is not an active reservation");
        }

    }
}
