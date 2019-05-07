package by.bsuir.CreditCalculator.Web.DataContracts.Authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationDataContract {
    private String _grantType;
    private String _refreshToken;
    private String _username;
    private String _password;


    @JsonProperty("grant_type")
    public void setGrantType(String grantType) {
        _grantType = grantType;
    }

    public String getGrantType() {
        return _grantType;
    }

    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken) {
        _refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return _refreshToken;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public String getUsername() {
        return _username;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String getPassword() {
        return _password;
    }
}
