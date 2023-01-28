package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.SocketService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class ReservationManager {
    @Autowired
    ReservationService reservationService;
    @Autowired
    UserService userService;
    @Autowired
    CPMSRequestSender cpmsRequestSender;
    @Autowired
    SocketService socketService;

    @PostMapping("/makeReservation")
    public ResponseEntity<?> makeReservation(@RequestBody Map<String,String> payload){
        //Collect the payload
        Long userId = Long.parseLong(payload.get("userId"));
        String socketId = payload.get("socketId");
        String tariffId = payload.get("tariffId");

        //Create the reservation
        User user = userService.findById(userId);
        Reservation reservation = new ActiveReservation();
        reservation.setUser(user);
        reservation.setSocketId(socketId);
        reservation.setTariffId(tariffId);

        //Collect data to send to the CPMS
        reservation = cpmsRequestSender.reserve(reservation);

        //Save the reservation in the DB
        reservationService.save(reservation);
        return ResponseEntity.ok(reservation);
    }
}
