package by.bsuir.CreditCalculator.Foundation.Interfaces;

import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.Common.SortOrder;
import by.bsuir.CreditCalculator.DomainModel.Credit;
import by.bsuir.CreditCalculator.Foundation.Credits.CreateCreditError;
import by.bsuir.CreditCalculator.Foundation.Credits.DeleteCreditError;
import by.bsuir.CreditCalculator.Foundation.Credits.UpdateCreditError;
import by.bsuir.CreditCalculator.Repositories.Implementations.Credit.SortField;

import java.util.List;

public interface ICreditService {
    Credit calculateCredit(Credit credit);

    List<Credit> getCredits(String ownerEmail, int page, int pageSize, SortField sortField, SortOrder sortOrder, String filter) throws Exception;

    OperationResult<CreateCreditError, Credit> createCredit(Credit credit, String ownerName) throws Exception;

    OperationResult<UpdateCreditError, Credit> updateCredit(Credit credit, String oldName, String ownerEmail) throws Exception;

    OperationResult<DeleteCreditError, Credit> deleteCredit(String name, String ownerEmail) throws Exception;

    long countCredits(String ownerEmail, String filter) throws Exception;
}
