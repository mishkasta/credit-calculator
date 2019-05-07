package by.bsuir.CreditCalculator.Foundation.Specifications.User;

import by.bsuir.Common.Specifications.JpqlSpecifications.CompositeJpqlSpecification;
import by.bsuir.CreditCalculator.DomainModel.User;

public class EmailSpecification extends CompositeJpqlSpecification<User> {
    private final String _email;


    public EmailSpecification(String email) {
        _email = email;
    }


    @Override
    public String getClause() {
        return String.format("email = '%s'", _email);
    }
}
