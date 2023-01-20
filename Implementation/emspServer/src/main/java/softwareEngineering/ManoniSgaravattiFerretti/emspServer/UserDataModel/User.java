package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @Column(name="user_id", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String name;
    private String surname;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<UserVehicle> vehicleList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ActiveReservation> reservations = new ArrayList<>();
}
