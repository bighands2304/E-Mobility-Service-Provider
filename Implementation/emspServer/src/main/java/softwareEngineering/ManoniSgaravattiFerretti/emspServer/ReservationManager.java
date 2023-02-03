package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

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
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class ReservationManager {
    @Autowired
    ReservationService reservationService;
    @Autowired
    ChargingPointService cpService;
    @Autowired
    UserService userService;
    @Autowired
    CommandsSender commandsSender;

    @PostMapping("/makeReservation")
    public ResponseEntity<?> makeReservation(@RequestBody Map<String,String> payload){
        //Collect the payload
        Long userId = Long.parseLong(payload.get("userId"));
        String cpId = payload.get("cpId");
        String socketId = payload.get("socketId");

        //Create the reservation
        User user = userService.findById(userId);
        ChargingPoint cp = cpService.getCPById(cpId);
        Reservation reservation = new ActiveReservation();
        reservation.setUser(user);
        reservation.setSocketId(socketId);
        reservation.setStartTime(LocalDateTime.now());

        //Save the reservation in the DB
        reservationService.save(reservation);

        //Send the reservation to the cpms
        commandsSender.reserveNow(cp, reservation);

        //Send reservation response
        return ResponseEntity.ok(reservation);
    }
}
