package softwareengineering.manonisgaravattiferretti.cpmsServer.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Document(collection = "cpos")
public class CPO implements UserDetails {
    @Id
    private String id;
    private String cpoCode;
    private String iban;
    private String password;

    @Transient
    private boolean enable;
    @Transient
    private boolean credentialNotExpired;
    @Transient
    private boolean accountNonExpired;
    @Transient
    private boolean accountNonLocked;
    @Transient
    private List<GrantedAuthority> authorities;

    public CPO(){
        this.enable = true;
        this.accountNonExpired = true;
        this.credentialNotExpired = true;
        this.accountNonLocked = true;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("CPO"));
    }
    public CPO(CPO cpo){
        this.id = cpo.id;
        this.cpoCode = cpo.cpoCode;
        this.iban = cpo.iban;
        this.password = cpo.password;
        this.enable = true;
        this.accountNonExpired = true;
        this.credentialNotExpired = true;
        this.accountNonLocked = true;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("CPO"));
    }

    @Override
    public String getUsername() {
        return cpoCode;
    }

    @Override
    public String getPassword() {
        return password;
    }


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
        return credentialNotExpired;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }
}
