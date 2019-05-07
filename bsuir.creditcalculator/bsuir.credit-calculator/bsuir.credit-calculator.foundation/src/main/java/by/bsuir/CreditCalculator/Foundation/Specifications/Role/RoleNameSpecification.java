package by.bsuir.CreditCalculator.Foundation.Specifications.Role;

import by.bsuir.Common.Specifications.JpqlSpecifications.CompositeJpqlSpecification;
import by.bsuir.CreditCalculator.DomainModel.Role;

public class RoleNameSpecification extends CompositeJpqlSpecification<Role> {
    private final String _name;


    public RoleNameSpecification(String name) {
        _name = name;
    }


    @Override
    public String getClause() {
        return String.format("name = '%s'", _name);
    }
}
