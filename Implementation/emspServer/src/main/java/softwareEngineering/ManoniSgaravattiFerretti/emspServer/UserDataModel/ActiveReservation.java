package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDateTime;

@Data
@Entity
@Table
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@EnableAutoConfiguration
public class ActiveReservation extends Reservation {
    private Long SessionId;
}
