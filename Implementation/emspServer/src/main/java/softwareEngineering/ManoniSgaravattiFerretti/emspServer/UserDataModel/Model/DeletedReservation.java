package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDateTime;
@Data
@Entity
@EnableAutoConfiguration
public class DeletedReservation extends Reservation {
    private LocalDateTime deletionTime;
}
