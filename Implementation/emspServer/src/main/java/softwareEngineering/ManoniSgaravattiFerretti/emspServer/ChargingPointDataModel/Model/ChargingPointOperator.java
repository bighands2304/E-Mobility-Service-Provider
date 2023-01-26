package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class ChargingPointOperator implements UserDetails{
    public ChargingPointOperator(){
        this.authorities.add(new SimpleGrantedAuthority("ROLE_CPMS"));
    }
    public ChargingPointOperator(ChargingPointOperator cpo){
        this.cpoId=cpo.cpoId;
        this.token=cpo.token;
        this.iban=cpo.iban;
        this.cpmsUrl=cpo.cpmsUrl;
        this.chargingPoints=cpo.chargingPoints;
        this.authorities.add(new SimpleGrantedAuthority("ROLE_CPMS"));
    }
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String cpoId;
    private String token;
    private String iban;
    private String cpmsUrl;
    private List<ChargingPoint> chargingPoints = new ArrayList<>();

    private List<GrantedAuthority> authorities = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
