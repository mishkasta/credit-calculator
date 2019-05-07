package by.bsuir.CreditCalculator.Web.DataContracts.User;

public class RegisterNewUserDataContract {
    private String _username;
    private String _email;
    private String _password;


    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }
}
