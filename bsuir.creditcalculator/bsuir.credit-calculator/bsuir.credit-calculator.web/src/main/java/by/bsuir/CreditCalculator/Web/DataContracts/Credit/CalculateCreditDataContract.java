package by.bsuir.CreditCalculator.Web.DataContracts.Credit;

import by.bsuir.Common.Interfaces.IDataContract;

public class CalculateCreditDataContract implements IDataContract {
    private double _desiredSum;
    private int _monthsCount;
    private double _interestRate;
    private double _totalSum;
    private double _monthlyCharge;


    public CalculateCreditDataContract(
            double desiredSum,
            int monthsCount,
            double interestRate,
            double monthlyCharge,
            double totalSum) {
        _desiredSum = desiredSum;
        _monthsCount = monthsCount;
        _interestRate = interestRate;
        _monthlyCharge = monthlyCharge;
        _totalSum = totalSum;
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

    public double getTotalSum() {
        return _totalSum;
    }

    public void setTotalSum(double totalSum) {
        _totalSum = totalSum;
    }

    public double getMonthlyCharge() {
        return _monthlyCharge;
    }

    public void setMonthlyCharge(double monthlyCharge) {
        _monthlyCharge = monthlyCharge;
    }
}
