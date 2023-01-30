package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Entity
@Table
@EnableAutoConfiguration
@JsonIgnoreProperties(value= {"password"})
public class User implements UserDetails {
    public User(){
        this.enable = true;
        this.accountNonExpired = true;
        this.credentialExpired = true;
        this.accountNonLocked = true;
        this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }
    public User(User user){
        this.id = user.id;
        this.username = user.username;
        this.name = user.name;
        this.surname = user.surname;
        this.email = user.email;
        this.password = user.password;
        this.vehicleList=user.vehicleList;
        this.reservations=user.reservations;
        this.enable = true;
        this.accountNonExpired = true;
        this.credentialExpired = true;
        this.accountNonLocked = true;
        this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }
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
    @JsonIgnore
    private List<UserVehicle> vehicleList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ActiveReservation> reservations = new ArrayList<>();

    private boolean enable;
    private boolean credentialExpired;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private List<GrantedAuthority> authorities = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialExpired;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
