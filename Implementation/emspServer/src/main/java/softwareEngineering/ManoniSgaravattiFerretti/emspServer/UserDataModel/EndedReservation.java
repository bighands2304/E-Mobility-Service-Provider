package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDateTime;
@Data
@Entity
@Table
@EnableAutoConfiguration
public class EndedReservation extends ActiveReservation {
    private LocalDateTime endTime;
    private Double energyAmount;
    private Double price;
}
