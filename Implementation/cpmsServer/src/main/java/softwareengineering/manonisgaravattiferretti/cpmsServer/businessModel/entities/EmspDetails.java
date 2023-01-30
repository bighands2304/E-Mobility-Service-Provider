package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Document(collection = "emspDetails")
public class EmspDetails implements UserDetails {
    @Id
    private String id;
    @Indexed(unique = true)
    private String emspToken;
    private String url;
    private String cpoToken;

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

    public EmspDetails() {
        this.enable = true;
        this.accountNonExpired = true;
        this.credentialNotExpired = true;
        this.accountNonLocked = true;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMSP"));
    }

    @Override
    public String getUsername() {
        return emspToken;
    }

    @Override
    public String getPassword() {
        return emspToken;
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
}
