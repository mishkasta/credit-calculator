package by.bsuir.CreditCalculator.Foundation.Specifications.RefreshToken;

import by.bsuir.Common.Specifications.JpqlSpecifications.CompositeJpqlSpecification;
import by.bsuir.CreditCalculator.DomainModel.RefreshToken;

public class ValueSpecification extends CompositeJpqlSpecification<RefreshToken> {
    private final String _value;


    public ValueSpecification(String value) {
        _value = value;
    }


    @Override
    public String getClause() {
        return String.format("value = '%s'", _value);
    }
}
