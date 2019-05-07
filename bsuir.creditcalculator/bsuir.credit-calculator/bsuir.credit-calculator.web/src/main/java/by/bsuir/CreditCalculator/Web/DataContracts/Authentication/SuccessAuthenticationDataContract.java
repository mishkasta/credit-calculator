package by.bsuir.CreditCalculator.Web.DataContracts.Authentication;

import by.bsuir.Common.Interfaces.IDataContract;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessAuthenticationDataContract implements IDataContract {
    private String _accessToken;
    private String _tokenType;
    private String _refreshToken;


    public SuccessAuthenticationDataContract(String accessToken, String tokenType, String refreshToken) {
        _accessToken = accessToken;
        _tokenType = tokenType;
        _refreshToken = refreshToken;
    }


    public String getAccessToken() {
        return _accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        _accessToken = accessToken;
    }

    public String getTokenType() {
        return _tokenType;
    }

    @JsonProperty("token_type")
    public void setTokenType(String tokenType) {
        _tokenType = tokenType;
    }

    public String getRefreshToken() {
        return _refreshToken;
    }

    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken) {
        _refreshToken = refreshToken;
    }
}
