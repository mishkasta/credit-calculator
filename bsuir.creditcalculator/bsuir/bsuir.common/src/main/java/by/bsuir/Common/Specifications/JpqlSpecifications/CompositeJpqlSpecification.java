package by.bsuir.Common.Specifications.JpqlSpecifications;

import by.bsuir.Common.Interfaces.IJpqlSpecification;

public abstract class CompositeJpqlSpecification<TCandidate> implements IJpqlSpecification<TCandidate> {
    @Override
    public IJpqlSpecification<TCandidate> and(IJpqlSpecification<TCandidate> other) {
        return new AndJpqlSpecification<>(this, other);
    }

    @Override
    public IJpqlSpecification<TCandidate> or(IJpqlSpecification<TCandidate> other) {
        return new OrJpqlSpecification<>(this, other);
    }

    @Override
    public IJpqlSpecification<TCandidate> not() {
        return new NotJpqlSpecification<>(this);
    }
}
