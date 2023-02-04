package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender.CommandsSender;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.DeletedReservation;
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
        reservation.setCpId(cpId);
        reservation.setSocketId(socketId);
        reservation.setStartTime(LocalDateTime.now());

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
        if (reservation instanceof ActiveReservation activeReservation) {
            if (activeReservation.getSessionId()==null) {
                //Send the deletion to its CPMS and retrieve the response code
                if(commandsSender.cancelReservation(activeReservation).getStatusCode().is2xxSuccessful()){
                    return ResponseEntity.ok("Deleted correctly");
                }else {
                    return ResponseEntity.badRequest().body("Error deleting the reservation");
                }

            }else {
                return ResponseEntity.badRequest().body("That's not an active reservation");
            }
        }else {
            return ResponseEntity.badRequest().body("That's not an active reservation");
        }
    }
}
