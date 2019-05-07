package by.bsuir.CreditCalculator.Web.Security;

import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IUserService;
import by.bsuir.CreditCalculator.Foundation.Users.GetUserError;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final IUserService _userService;



    public UserDetailsServiceImpl(IUserService userService) {
        _userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        OperationResult<GetUserError, User> getUserResult = null;
        try {
            getUserResult = _userService.getByEmail(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException(username);
        }
        if (!getUserResult.isSuccessful()) {
            throw new UsernameNotFoundException(username);
        }
        User dbUser = getUserResult.getResult();
        String dbUsername = dbUser.getEmail();
        String dbPassword = dbUser.getPassword();
        List<GrantedAuthority> grantedAuthorities = dbUser.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(dbUsername, dbPassword, grantedAuthorities);
    }
}
