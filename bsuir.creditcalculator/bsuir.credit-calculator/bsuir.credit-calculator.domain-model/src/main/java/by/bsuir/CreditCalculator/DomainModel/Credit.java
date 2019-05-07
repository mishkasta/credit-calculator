package by.bsuir.CreditCalculator.DomainModel;

import by.bsuir.Common.Interfaces.IEntity;

import java.util.Date;

public class Credit implements IEntity {
    private long _id;
    private String _name;
    private double _desiredSum;
    private int _monthsCount;
    private double _interestRate;
    private double _monthlyCharge;
    private double _totalSum;
    private Date _createdDate;
    private User _user;


    @Override
    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public double getDesiredSum() {
        return _desiredSum;
    }

    public void setDesiredSum(double desiredSum) {
        _desiredSum = desiredSum;
    }

    public int getMonthsCount() {
        return _monthsCount;
    }

    public void setMonthsCount(int monthsCount) {
        _monthsCount = monthsCount;
    }

    public double getInterestRate() {
        return _interestRate;
    }

    public void setInterestRate(double interestRate) {
        _interestRate = interestRate;
    }

    public double getMonthlyCharge() {
        return _monthlyCharge;
    }

    public void setMonthlyCharge(double monthlyCharge) {
        _monthlyCharge = monthlyCharge;
    }

    public double getTotalSum() {
        return _totalSum;
    }

    public void setTotalSum(double totalSum) {
        _totalSum = totalSum;
    }

    public Date getCreatedDate() {
        return _createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        _createdDate = createdDate;
    }

    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }
}
