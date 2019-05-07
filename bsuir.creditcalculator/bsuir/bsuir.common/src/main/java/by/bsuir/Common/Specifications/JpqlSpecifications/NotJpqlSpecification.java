package by.bsuir.Common.Specifications.JpqlSpecifications;

import by.bsuir.Common.Interfaces.IJpqlSpecification;

public class NotJpqlSpecification<TCandidate>
        extends CompositeJpqlSpecification<TCandidate>
        implements IJpqlSpecification<TCandidate> {
    private final IJpqlSpecification<TCandidate> _specification;


    public NotJpqlSpecification(IJpqlSpecification<TCandidate> specification) {
        _specification = specification;
    }


    @Override
    public String getClause() {
        return String.format("NOT (%s)", _specification.getClause());
    }
}
