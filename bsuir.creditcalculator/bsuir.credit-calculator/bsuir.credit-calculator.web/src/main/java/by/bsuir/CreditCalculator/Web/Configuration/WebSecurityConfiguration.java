package by.bsuir.CreditCalculator.Web.Configuration;

import by.bsuir.Common.Interfaces.IJsonSerializer;
import by.bsuir.CreditCalculator.DomainModel.Role;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IRefreshTokenService;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IUserService;
import by.bsuir.CreditCalculator.Web.Constants.Routes;
import by.bsuir.CreditCalculator.Web.Security.JwtAuthenticationFilter;
import by.bsuir.CreditCalculator.Web.Security.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService _userDetailsService;
    private final PasswordEncoder _passwordEncoder;
    private final IJsonSerializer _jsonSerializer;
    private final IUserService _userService;
    private final IRefreshTokenService _refreshTokenService;


    @Autowired
    public WebSecurityConfiguration(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            IJsonSerializer jsonSerializer,
            IUserService userService,
            IRefreshTokenService refreshTokenService) {
        _userDetailsService = userDetailsService;
        _passwordEncoder = passwordEncoder;
        _jsonSerializer = jsonSerializer;
        _userService = userService;
        _refreshTokenService = refreshTokenService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, Routes.CALCULATE_CREDIT_COMPLETE).permitAll()
                .antMatchers(HttpMethod.POST, Routes.USER_REGISTRATION_COMPLETE, Routes.LOGIN).anonymous()
                .anyRequest().permitAll()
                .and()
                .addFilter(getJwtAuthenticationFilter())
                .addFilter(getJwtAuthorizationFilter())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(_userDetailsService).passwordEncoder(_passwordEncoder);
    }


    private JwtAuthenticationFilter getJwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(
                authenticationManager(),
                _jsonSerializer,
                _userDetailsService,
                _userService,
                _refreshTokenService);
    }

    private JwtAuthorizationFilter getJwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager());
    }
}
