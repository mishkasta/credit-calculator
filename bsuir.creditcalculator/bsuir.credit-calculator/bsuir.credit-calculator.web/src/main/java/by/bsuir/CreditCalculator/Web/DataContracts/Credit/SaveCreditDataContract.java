package by.bsuir.CreditCalculator.Web.DataContracts.Credit;

import by.bsuir.Common.Interfaces.IDataContract;

public class SaveCreditDataContract implements IDataContract {
    private String _name;
    private double _desiredSum;
    private int _monthsCount;
    private double _interestRate;
    private double _monthlyCharge;
    private double _totalSum;


    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setDesiredSum(double desiredSum) {
        _desiredSum = desiredSum;
    }

    public double getDesiredSum() {
        return _desiredSum;
    }

    public void setMonthsCount(int monthsCount) {
        _monthsCount = monthsCount;
    }

    public int getMonthsCount() {
        return _monthsCount;
    }

    public void setInterestRate(double interestRate) {
        _interestRate = interestRate;
    }

    public double getInterestRate() {
        return _interestRate;
    }

    public void setMonthlyCharge(double monthlyCharge) {
        _monthlyCharge = monthlyCharge;
    }

    public double getMonthlyCharge() {
        return _monthlyCharge;
    }

    public void setTotalSum(double totalSum) {
        _totalSum = totalSum;
    }

    public double getTotalSum() {
        return _totalSum;
    }
}
