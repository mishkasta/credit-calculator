package by.bsuir.Common.Interfaces;

public interface IJpqlSpecification<TCandidate> {
    IJpqlSpecification<TCandidate> and(IJpqlSpecification<TCandidate> other);

    IJpqlSpecification<TCandidate> or(IJpqlSpecification<TCandidate> other);

    IJpqlSpecification<TCandidate> not();

    String getClause();
}
