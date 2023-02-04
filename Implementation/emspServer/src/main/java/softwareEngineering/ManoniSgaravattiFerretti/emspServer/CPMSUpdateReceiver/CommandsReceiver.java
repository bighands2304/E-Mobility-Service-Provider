package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingSessionManager.NotificationGenerator;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.NotificationSender;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/ocpi/emsp/commands")
public class CommandsReceiver {
    @Autowired
    ReservationService reservationService;
    @Autowired
    NotificationSender notificationSender;

    //List of commands invokable by the CPMS to confirm:
    //• CANCEL_RESERVATION: the deletion of the reservation
    //• RESERVE_NOW: the creation of the reservation
    //• START_SESSION:
    //• STOP_SESSION:
    //• UNLOCK_CONNECTOR:
    @PostMapping("/{command}/{uid}")
    public ResponseEntity<?> cancelReservation(@PathVariable String command, @PathVariable String uid, @RequestBody Map<String,String> commandResult){
        if (command.equals("CANCEL_RESERVATION")){
            if(commandResult.get("result").equals("ACCEPTED")) {
                //Get the reservation to delete
                Reservation reservation = reservationService.getReservationById(Long.parseLong(uid));
                if (reservation.getType().equals("RESERVED")) {
                    //Set the reservation as deleted and set the deletion time to now
                    reservation.setDeletionTime(LocalDateTime.now());
                    //Save the reservation as deleted

                    reservationService.save(reservation);
                }
            }
        }

        if (command.equals("RESERVE_NOW")){
            Reservation reservation = reservationService.getReservationById(Long.parseLong(uid));
            if(commandResult.get("result").equals("ACCEPTED")){

                reservation.setStartTime(LocalDateTime.now());

                reservationService.save(reservation);
            }if(commandResult.get("result").equals("TIMEOUT")){
                reservationService.delete(reservation);
            }
        }

        if (command.equals("START_SESSION")){
            if(commandResult.get("result").equals("ACCEPTED")){}
        }

        if (command.equals("STOP_SESSION")){
            if(commandResult.get("result").equals("ACCEPTED")){
                Reservation reservation = reservationService.getReservationById(Long.parseLong(uid));

                //Create a notification to send when the session ends
                NotificationGenerator notification= new NotificationGenerator();
                notification.setTo(reservation.getUser().getId());
                notification.setTitle("Session ended");
                notification.setBody("Go to unplug your car");

                //Send the notification to the user
                try {
                    notificationSender.sendNotification(notification);
                } catch (FirebaseMessagingException e) {}


                reservationService.save(reservation);
            }
        }

        return ResponseEntity.noContent().build();
    }
}
