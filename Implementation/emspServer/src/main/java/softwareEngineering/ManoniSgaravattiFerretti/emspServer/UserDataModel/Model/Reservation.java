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
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @Column(name="reservation_id", nullable = false, unique = true)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime expiryDate;
    private Double batteryPercentage;
    @ManyToOne
    private User user;

    private String tariffId;
    private String cpId;
    private String socketId;

    //Active reservation fields
    private Long sessionId;

    //Deleted reservation fields
    private LocalDateTime deletionTime;

    //Ended reservation fields
    private LocalDateTime endTime;
    private Double energyAmount;
    private Double price;

    public String getType(){
        if(deletionTime!=null){
            return "DELETED";
        }else if(endTime!=null || energyAmount!=null || price!=null){
            return "ENDED";
        }else if (sessionId!=null){
            return "ACTIVE";
        }else{
            return "RESERVED";
        }
    }
}
