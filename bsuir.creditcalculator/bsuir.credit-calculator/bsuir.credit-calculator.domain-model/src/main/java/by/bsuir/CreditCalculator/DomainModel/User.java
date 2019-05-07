package by.bsuir.CreditCalculator.DomainModel;

import by.bsuir.Common.Interfaces.IEntity;

import java.util.Date;
import java.util.List;

public class User implements IEntity {
    private long _id;
    private String _username;
    private String _email;
    private String _password;
    private List<Credit> _credits;
    private Date _createDate;
    private List<Role> _roles;
    private List<RefreshToken> _refreshTokens;

    @Override
    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

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

    public List<Credit> getCredits() {
        return _credits;
    }

    public void setCredits(List<Credit> credits) {
        _credits = credits;
    }

    public void setCreateDate(Date createDate) {
        _createDate = createDate;
    }

    public Date getCreateDate() {
        return _createDate;
    }

    public void setRoles(List<Role> roles) {
        _roles = roles;
    }

    public List<Role> getRoles() {
        return _roles;
    }

    public List<RefreshToken> getRefreshTokens() {
        return _refreshTokens;
    }

    public void setRefreshTokens(List<RefreshToken> refreshTokens) {
        _refreshTokens = refreshTokens;
    }
}
