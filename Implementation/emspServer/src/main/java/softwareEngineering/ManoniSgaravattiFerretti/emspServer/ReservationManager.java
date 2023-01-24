package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    UserService userService;

    @PostMapping("/makeReservation")
    public ResponseEntity<?> makeReservation(@RequestBody Map<String,String> payload){
        User user = userService.findById(Long.parseLong(payload.get("userId")));
        LocalDateTime startTime = LocalDateTime.parse(payload.get("startTime"));
        Reservation reservation = new ActiveReservation();
        reservation.setUser(user);

        reservation.setSocketId(payload.get("socketId"));
        reservation.setTariffId(payload.get("tariffId"));
        reservation.setStartTime(startTime);

        reservationService.save(reservation);
        //TODO send informations of reservation to CPMS
        return ResponseEntity.ok("");
    }
}
