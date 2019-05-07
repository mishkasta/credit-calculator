package by.bsuir.Common.Specifications;

import by.bsuir.Common.Interfaces.ISpecification;

public class OrSpecification<TCandidate> extends CompositeSpecification<TCandidate> implements ISpecification<TCandidate> {
    private final ISpecification<TCandidate> _first;
    private final ISpecification<TCandidate> _second;


    public OrSpecification(ISpecification<TCandidate> first, ISpecification<TCandidate> second) {
        _first = first;
        _second = second;
    }


    @Override
    public boolean isSatisfiedBy(TCandidate candidate) {
        return _first.isSatisfiedBy(candidate) || _second.isSatisfiedBy(candidate);
    }
}
