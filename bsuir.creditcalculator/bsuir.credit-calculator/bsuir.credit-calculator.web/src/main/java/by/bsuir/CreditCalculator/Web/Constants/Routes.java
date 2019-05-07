package by.bsuir.CreditCalculator.Web.Constants;

public class Routes {
    private static final String PREFIX_PATTERN = "/api/%s/";


    public static final String DEFAULT = "/";

    public static final String USERS = "/api/users";
    public static final String USER_REGISTRATION = "/registration";
    public static final String USER_REGISTRATION_COMPLETE = USERS + USER_REGISTRATION;

    public static final String CREDITS = "/api/credits";
    public static final String CALCULATE_CREDIT = "/calculate";
    public static final String CALCULATE_CREDIT_COMPLETE = CREDITS + CALCULATE_CREDIT;
    public static final String MY_CREDITS_COMPLETE = CREDITS + "/mycredits";

    public static final String LOGIN = "/login";


    private static String getFormattedRoute(String route) {
        return String.format(PREFIX_PATTERN, route);
    }
}
