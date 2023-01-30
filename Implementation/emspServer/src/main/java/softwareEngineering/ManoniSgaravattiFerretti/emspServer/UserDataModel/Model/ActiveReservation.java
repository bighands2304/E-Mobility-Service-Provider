package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@EnableAutoConfiguration
public class ActiveReservation extends Reservation {
    private Long sessionId;
}
