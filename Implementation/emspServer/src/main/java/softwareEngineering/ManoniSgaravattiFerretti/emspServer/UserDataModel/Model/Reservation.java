package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDateTime;

@Data
@Entity
@Table
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@EnableAutoConfiguration
public abstract class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @Column(name="reservation_id", nullable = false, unique = true)
    private Long id;
    private LocalDateTime startTime;
    @ManyToOne
    private User user;

    private String tariffId;
    private String socketId;
}
