package com.perfumepictor.dev.config.jwt;

import com.perfumepictor.dev.entity.Profiles;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class PrincipalDetails implements UserDetails {

    private String email;
    private String password;
    private String role;

    public PrincipalDetails(Profiles profile) {
        this.email = profile.getEmail();
        this.role = "USER";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO: return memberStatus == ACTIVE;
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO: return memberStatus == ACTIVE;
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
