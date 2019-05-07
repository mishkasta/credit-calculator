package by.bsuir.Common.Specifications.JpqlSpecifications;

import by.bsuir.Common.Interfaces.IJpqlSpecification;

public class TrueJpqlSpecification<TCandidate>
        extends CompositeJpqlSpecification<TCandidate>
        implements IJpqlSpecification<TCandidate> {
    @Override
    public String getClause() {
        return "1 = 1";
    }
}
