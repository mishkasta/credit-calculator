package by.bsuir.Common.Interfaces;

public interface ISpecification<TCandidate> {
    ISpecification and(ISpecification specification);

    ISpecification or(ISpecification specification);

    ISpecification not(ISpecification specification);

    boolean isSatisfiedBy(TCandidate candidate);
}
