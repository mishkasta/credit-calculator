package by.bsuir.CreditCalculator.DomainModel;

import by.bsuir.Common.Interfaces.IEntity;

import java.util.Date;

public class RefreshToken implements IEntity {
    private long _id;
    private String _value;
    private Date _expirationDate;
    private User _user;


    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }

    public Date getExpirationDate() {
        return _expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        _expirationDate = expirationDate;
    }

    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }
}
