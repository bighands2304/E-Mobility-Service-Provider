package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
@EnableAutoConfiguration
public class Vehicle {
    @Id
    @Column(name="vin_code", nullable = false, unique = true, length =17)
    private String VINCode;
    private String socketType;
    private String model;
    private Integer batteryPercentage;
    private Integer kmRange;

    @OneToMany(mappedBy = "vehicle")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<UserVehicle> usersList = new ArrayList<>();
}
