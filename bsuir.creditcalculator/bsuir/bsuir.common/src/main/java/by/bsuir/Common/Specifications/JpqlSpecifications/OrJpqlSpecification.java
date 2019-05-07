package by.bsuir.Common.Specifications.JpqlSpecifications;

import by.bsuir.Common.Interfaces.IJpqlSpecification;

public class OrJpqlSpecification<TCandidate> extends CompositeJpqlSpecification<TCandidate>
        implements IJpqlSpecification<TCandidate> {
    private final IJpqlSpecification<TCandidate> _first;
    private final IJpqlSpecification<TCandidate> _second;


    public OrJpqlSpecification(IJpqlSpecification<TCandidate> first, IJpqlSpecification<TCandidate> second) {
        _first = first;
        _second = second;
    }


    @Override
    public String getClause() {
        return String.format("(%s) OR (%s)", _first.getClause(), _second.getClause());
    }
}
