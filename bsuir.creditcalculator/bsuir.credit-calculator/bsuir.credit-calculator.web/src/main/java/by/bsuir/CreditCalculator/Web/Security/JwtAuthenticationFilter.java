package by.bsuir.CreditCalculator.Web.Security;

import by.bsuir.Common.DataContracts.ErrorDataContract;
import by.bsuir.Common.Interfaces.IJsonSerializer;
import by.bsuir.Common.JsonSerializer.SerializeError;
import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.CreditCalculator.DomainModel.RefreshToken;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IRefreshTokenService;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IUserService;
import by.bsuir.CreditCalculator.Foundation.RefreshToken.DeleteRefreshTokenError;
import by.bsuir.CreditCalculator.Web.Constants.User.AuthenticationConstants;
import by.bsuir.CreditCalculator.Web.Constants.HttpConstants;
import by.bsuir.CreditCalculator.Web.Constants.SecurityConstants;
import by.bsuir.CreditCalculator.Web.DataContracts.Authentication.AuthenticationDataContract;
import by.bsuir.CreditCalculator.Web.DataContracts.Authentication.SuccessAuthenticationDataContract;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static by.bsuir.CreditCalculator.Web.Constants.SecurityConstants.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager _authenticationManager;
    private final IJsonSerializer _jsonSerializer;
    private final UserDetailsService _userDetailsService;
    private final IUserService _userService;
    private final IRefreshTokenService _refreshTokenService;


    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            IJsonSerializer jsonSerializer,
            UserDetailsService userDetailsService,
            IUserService userService,
            IRefreshTokenService refreshTokenService) {
        _authenticationManager = authenticationManager;
        _jsonSerializer = jsonSerializer;
        _userDetailsService = userDetailsService;
        _userService = userService;
        _refreshTokenService = refreshTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            OperationResult<SerializeError, AuthenticationDataContract> deserializeResult = _jsonSerializer.deserialize(
                    request.getInputStream(),
                    AuthenticationDataContract.class);
            if (!deserializeResult.isSuccessful()) {
                throw new IOException("Failed to deserialize");
            }

            AuthenticationDataContract authenticationDataContract = deserializeResult.getResult();
            String grantType = authenticationDataContract.getGrantType();
            if (grantType.equals(SecurityConstants.GrantTypes.PASSWORD)) {
                return getUsernameAuthentication(authenticationDataContract);
            } else if (grantType.equals(SecurityConstants.GrantTypes.REFRESH_TOKEN)) {
                try {
                    return getRefreshTokenAuthentication(authenticationDataContract);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new AuthenticationCredentialsNotFoundException("invalid grant_type");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException {
        String email = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
        User dbUser;
        try {
            dbUser = _userService.getByEmail(email).getResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String subject = dbUser.getUsername();
        String[] roles = dbUser.getRoles()
                .stream()
                .map(r -> ROLE_PREFIX + r.getName())
                .toArray(String[]::new);
        String accessToken = JWT.create()
                .withSubject(subject)
                .withArrayClaim("roles", roles)
                .withClaim("email", email)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.ACCESS_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        Date refreshTokenExpirationDate = new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_TOKEN_EXPIRATION_TIME);
        String refreshTokenValue = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT_PREFIX + subject)
                .withExpiresAt(refreshTokenExpirationDate)
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setValue(refreshTokenValue);
        refreshToken.setExpirationDate(refreshTokenExpirationDate);
        try {
            _refreshTokenService.createRefreshToken(refreshToken, email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SuccessAuthenticationDataContract successAuthenticationDataContract = new SuccessAuthenticationDataContract(
                accessToken,
                SecurityConstants.TOKEN_TYPE,
                refreshTokenValue);
        OperationResult<SerializeError, String> serializeResult = _jsonSerializer.serialize(successAuthenticationDataContract);
        response.getOutputStream().write(serializeResult.getResult().getBytes());
        response.addHeader(HttpConstants.CONTENT_TYPE, HttpConstants.APPLICATION_JSON);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        ErrorDataContract errorDataContract = new ErrorDataContract();
        errorDataContract.setErrors(AuthenticationConstants.AUTHENTICATION_FAILED);
        String serializedErrorDataContract = _jsonSerializer.serialize(errorDataContract).getResult();

        response.getOutputStream().write(serializedErrorDataContract.getBytes());
        response.addHeader(HttpConstants.CONTENT_TYPE, HttpConstants.APPLICATION_JSON);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }


    private Authentication getUsernameAuthentication(AuthenticationDataContract authenticationDataContract) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationDataContract.getUsername(),
                authenticationDataContract.getPassword(),
                new ArrayList<>());

        return _authenticationManager.authenticate(authenticationToken);
    }

    private Authentication getRefreshTokenAuthentication(AuthenticationDataContract authenticationDataContract) throws Exception {
        String refreshTokenValue = authenticationDataContract.getRefreshToken();
        RefreshToken dbRefreshToken = _refreshTokenService.getRefreshToken(refreshTokenValue);
        if (dbRefreshToken == null) {
            throw new AuthenticationCredentialsNotFoundException("invalid refresh_token");
        }
        Date expirationDate = dbRefreshToken.getExpirationDate();
        if (expirationDate.compareTo(new Date()) <= 0) {
            throw new AuthenticationCredentialsNotFoundException("refresh_token expired");
        }
        OperationResult<DeleteRefreshTokenError, RefreshToken> deleteRefreshTokenResult = _refreshTokenService.deleteRefreshToken(dbRefreshToken);
        if (!deleteRefreshTokenResult.isSuccessful()) {
            throw new AuthenticationCredentialsNotFoundException("invalid refresh_token");
        }

        UserDetails dbUser = _userDetailsService.loadUserByUsername(dbRefreshToken.getUser().getEmail());

        Authentication authentication = new RefreshTokenAuthnetication(dbUser, dbUser.getAuthorities());
        authentication.setAuthenticated(true);

        return authentication;
    }
}
