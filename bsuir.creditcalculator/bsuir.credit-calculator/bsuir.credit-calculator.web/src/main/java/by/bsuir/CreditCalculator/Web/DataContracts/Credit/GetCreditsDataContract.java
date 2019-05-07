package by.bsuir.CreditCalculator.Web.DataContracts.Credit;

import by.bsuir.Common.Interfaces.IDataContract;

import java.util.List;

public class GetCreditsDataContract implements IDataContract {
    private List<GetCreditsCreditDataContract> _credits;
    private long _creditsCount;


    public void setCredits(List<GetCreditsCreditDataContract> credits) {
        _credits = credits;
    }

    public List<GetCreditsCreditDataContract> getCredits() {
        return _credits;
    }

    public void setCreditsCount(long creditsCount) {
        _creditsCount = creditsCount;
    }

    public long getCreditsCount() {
        return _creditsCount;
    }
}
