package by.bsuir.Common.Specifications;

import by.bsuir.Common.Interfaces.ISpecification;

public abstract class CompositeSpecification<TCandidate> implements ISpecification<TCandidate> {
    @Override
    public ISpecification and(ISpecification specification) {
        return null;
    }

    @Override
    public ISpecification or(ISpecification specification) {
        return null;
    }

    @Override
    public ISpecification not(ISpecification specification) {
        return null;
    }
}
