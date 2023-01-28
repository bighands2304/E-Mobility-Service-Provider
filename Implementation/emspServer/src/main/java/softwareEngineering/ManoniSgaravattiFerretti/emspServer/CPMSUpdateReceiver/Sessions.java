package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.ActiveReservation;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Repository.ActiveReservationRepository;

import java.util.Map;

@RestController
@RequestMapping("/ocpi/emsp/2.2/sessions")
public class Sessions {
    @Autowired
    ActiveReservationRepository activeReservationRepository;
    @GetMapping("/{session_id}")
    public ResponseEntity<?> getSession(@PathVariable String session_id){
        ActiveReservation session = activeReservationRepository.findActiveReservationBySessionId(Long.parseLong(session_id));
        return ResponseEntity.ok(session);
    }

    @PutMapping("/{session_id}")
    public ResponseEntity<?> putSession(@PathVariable String session_id){
        ActiveReservation session = activeReservationRepository.findActiveReservationBySessionId(Long.parseLong(session_id));
        //TODO add fields to update of the session

        return ResponseEntity.ok(session);
    }

    @PatchMapping("/{session_id")
    public ResponseEntity<?> patchSession(@PathVariable String session_id){
        ActiveReservation session = activeReservationRepository.findActiveReservationBySessionId(Long.parseLong(session_id));
        //TODO add field to update of the session

        return ResponseEntity.ok(session);
    }
}
