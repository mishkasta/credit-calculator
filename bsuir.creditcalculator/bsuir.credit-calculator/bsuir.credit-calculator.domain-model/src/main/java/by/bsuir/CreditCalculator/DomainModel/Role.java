package by.bsuir.CreditCalculator.DomainModel;

import by.bsuir.Common.Interfaces.IEntity;

import java.util.List;

public class Role implements IEntity {
    private long _id;
    private String _name;
    private List<User> _users;


    public Role() {

    }


    public void setId(long id) {
        _id = id;
    }

    @Override
    public long getId() {
        return _id;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setUsers(List<User> users) {
        _users = users;
    }

    public List<User> getUsers() {
        return _users;
    }



    public class BuiltIn {
        public static final String USER = "User";
        public static final String ADMIN = "Admin";
    }
}
