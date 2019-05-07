package by.bsuir.CreditCalculator.Foundation.Specifications.Credit;

import by.bsuir.Common.Specifications.JpqlSpecifications.CompositeJpqlSpecification;
import by.bsuir.CreditCalculator.DomainModel.Credit;

public class GetCreditByNameForUserSpecification  extends CompositeJpqlSpecification<Credit> {
    private final String _creditName;


    public GetCreditByNameForUserSpecification(String creditName) {
        _creditName = creditName;
    }


    @Override
    public String getClause() {
        return String.format("name = '%s'", _creditName);
    }
}