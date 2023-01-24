package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Data
@Entity
@Table
@EnableAutoConfiguration
@IdClass(UserVehicleId.class)
public class UserVehicle {
    @Id
    @ManyToOne
    private User user;
    @Id
    @ManyToOne
    private Vehicle vehicle;
    private Boolean favourite;
}
