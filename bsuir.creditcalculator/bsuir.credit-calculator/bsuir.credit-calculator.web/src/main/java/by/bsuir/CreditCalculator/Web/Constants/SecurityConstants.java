package by.bsuir.CreditCalculator.Web.Constants;

public class SecurityConstants {
    public static final String TOKEN_PATTERN = "Bearer %s";


    public static final String SECRET = "29100434-e231-40d7-b0a7-206194781f98";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 900_000_000; // 150 minutes
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 7_776_000_000L; // 60 days
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_SUBJECT_PREFIX = "REFRESH_";
    public static final String ROLE_PREFIX = "ROLE_";


    public static String getToken(String token) {
        return String.format(TOKEN_PATTERN, token);
    }



    public static class GrantTypes {
        public static final String PASSWORD = "password";
        public static final String REFRESH_TOKEN = "refresh_token";
    }
}
