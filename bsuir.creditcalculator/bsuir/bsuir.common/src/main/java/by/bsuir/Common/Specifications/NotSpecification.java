package by.bsuir.Common.Specifications;

import by.bsuir.Common.Interfaces.ISpecification;

public class NotSpecification<TCandidate> extends CompositeSpecification<TCandidate> implements ISpecification<TCandidate> {
    private final ISpecification<TCandidate> _specification;


    public NotSpecification(ISpecification<TCandidate> specification) {
        _specification = specification;
    }


    @Override
    public boolean isSatisfiedBy(TCandidate candidate) {
        return !_specification.isSatisfiedBy(candidate);
    }
}
