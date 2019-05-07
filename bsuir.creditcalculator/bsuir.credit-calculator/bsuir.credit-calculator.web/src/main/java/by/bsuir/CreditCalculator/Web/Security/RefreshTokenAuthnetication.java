package by.bsuir.CreditCalculator.Web.Security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class RefreshTokenAuthnetication extends AbstractAuthenticationToken {
    private final UserDetails _principal;


    public RefreshTokenAuthnetication(UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);

        _principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return _principal;
    }
}
