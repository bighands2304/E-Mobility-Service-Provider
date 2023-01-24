package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Data
@Entity
@Table
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@EnableAutoConfiguration
public class ActiveReservation extends Reservation {
    private Long SessionId;
}
