package by.bsuir.CreditCalculator.Repositories.Interfaces;

import by.bsuir.Common.Interfaces.IJpqlRepository;
import by.bsuir.Common.Interfaces.IJpqlSpecification;
import by.bsuir.Common.Interfaces.IRepository;
import by.bsuir.Common.SortOrder;
import by.bsuir.CreditCalculator.DomainModel.Credit;
import by.bsuir.CreditCalculator.Repositories.Implementations.Credit.SortField;

import java.util.List;

public interface ICreditRepository extends IJpqlRepository<Credit>, IRepository<Credit> {
    Credit getSingleOrDefaultFor(IJpqlSpecification<Credit> specification, String ownerEmail);

    List<Credit> getCredits(String ownerEmail, int skippedCount, int count, SortOrder sortOrder, SortField sortField, String filter);

    long countCredits(String ownerEmail, String filter);
}
