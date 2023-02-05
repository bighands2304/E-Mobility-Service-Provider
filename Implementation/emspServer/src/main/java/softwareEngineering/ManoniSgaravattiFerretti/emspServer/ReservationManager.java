package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.CommandsSender;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
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
        for (Reservation r: reservationService.getReservationsByUserId(Long.parseLong(payload.get("userId")))) {
            if (r.getType().equals("ACTIVE") || r.getType().equals("RESERVED"))
                return ResponseEntity.badRequest().body("A reservation is already active");
        }

        //Collect the payload
        Long userId = Long.parseLong(payload.get("userId"));
        String cpId = payload.get("cpId");
        String socketId = payload.get("socketId");
        String tariffId = payload.get("tariffId");

        LocalDateTime reserveTime=LocalDateTime.now();
        //Create the reservation
        User user = userService.findById(userId);
        ChargingPoint cp = cpService.getCPById(cpId);
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCpId(cpId);
        reservation.setSocketId(socketId);
        reservation.setStartTime(reserveTime);
        reservation.setExpiryDate(reserveTime.plusMinutes(20));
        reservation.setTariffId(tariffId);

        //Save the reservation in the DB
        reservationService.save(reservation);

        //Send the reservation to the cpms, if negative delete the reservation and return error
        if(commandsSender.reserveNow(cp, reservation).getStatusCode().is4xxClientError()){
            reservationService.delete(reservation);
            return ResponseEntity.badRequest().body("Impossible reserve");
        }

        //Send reservation response
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/deleteReservation/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId){
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation.getType().equals("RESERVED")) {
                //Send the deletion to its CPMS and retrieve the response code
                if(commandsSender.cancelReservation(reservation).getStatusCode().is2xxSuccessful()){
                    return ResponseEntity.ok("Deleted correctly");
                }else {
                    return ResponseEntity.badRequest().body("Error deleting the reservation");
                }
        }else {
            return ResponseEntity.badRequest().body("That's not an active reservation");
        }
    }
}
