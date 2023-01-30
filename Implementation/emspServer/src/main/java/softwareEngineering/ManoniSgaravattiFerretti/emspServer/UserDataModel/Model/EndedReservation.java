package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDateTime;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@EnableAutoConfiguration
public class EndedReservation extends ActiveReservation {
    private LocalDateTime endTime;
    private Double energyAmount;
    private Double price;
}
