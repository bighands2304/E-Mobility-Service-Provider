package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.DeletedReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.EndedReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.Reservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.ReservationService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/ocpi/emsp/commands")
public class CommandsReceiver {
    @Autowired
    ReservationService reservationService;

    //List of commands invokable by the CPMS:
    //• CANCEL_RESERVATION: Delete the reservation
    //• RESERVE_NOW:
    //• START_SESSION:
    //• STOP_SESSION:
    //• UNLOCK_CONNECTOR:
    @PostMapping("/{command}/{uid}")
    public ResponseEntity<?> cancelReservation(@PathVariable String command, @PathVariable String uid, @RequestBody Map<String,String> commandResult){
        if (command.equals("CANCEL_RESERVATION")){
            if(commandResult.get("result").equals("ACCEPTED")) {
                Reservation reservation = reservationService.getReservationById(Long.parseLong(uid));

                DeletedReservation deletedReservation = (DeletedReservation) reservation;
                deletedReservation.setDeletionTime(LocalDateTime.now());

                reservationService.save(deletedReservation);
                reservationService.delete(reservation);
            }
        }

        if (command.equals("RESERVE_NOW")){
            if(commandResult.get("result").equals("ACCEPTED")){
                Reservation reservation = reservationService.getReservationById(Long.parseLong(uid));
                ActiveReservation activeReservation = (ActiveReservation) reservation;
                activeReservation.setStartTime(LocalDateTime.now());

                reservationService.save(activeReservation);
            }
        }

        if (command.equals("START_SESSION")){
            if(commandResult.get("result").equals("ACCEPTED")){
                ActiveReservation reservation = (ActiveReservation) reservationService.getReservationById(Long.parseLong(uid));
                //TODO insert real session id
                reservation.setSessionId(Long.parseLong(uid));

                reservationService.save(reservation);
            }
        }

        if (command.equals("STOP_SESSION")){
            if(commandResult.get("result").equals("ACCEPTED")){
                ActiveReservation reservation = (ActiveReservation) reservationService.getReservationById(Long.parseLong(uid));
                EndedReservation endedReservation = (EndedReservation) reservation;
                //TODO insert real values
                endedReservation.setEnergyAmount(1.0);
                endedReservation.setEndTime(LocalDateTime.now());
                endedReservation.setPrice(1.0);

                reservationService.save(endedReservation);
                reservationService.delete(reservation);
            }
        }

        return ResponseEntity.noContent().build();
    }
}
