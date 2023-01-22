package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Data
@Entity
@Table
@EnableAutoConfiguration
public class UserVehicle {
    @Id
    @ManyToOne
    private User user;
    @Id
    @ManyToOne
    private Vehicle vehicle;
    private Boolean favourite;
}
