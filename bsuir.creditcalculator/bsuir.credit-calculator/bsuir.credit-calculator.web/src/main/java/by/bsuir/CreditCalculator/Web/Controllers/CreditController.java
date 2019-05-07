package by.bsuir.CreditCalculator.Web.Controllers;

import by.bsuir.Common.DataContracts.ErrorDataContract;
import by.bsuir.Common.Interfaces.IDataContract;
import by.bsuir.Common.Interfaces.IErrorMessageProvider;
import by.bsuir.Common.Interfaces.INumberConverter;
import by.bsuir.Common.NumberConverter.ConvertNumberError;
import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.Common.RestControllers.RestControllerBase;
import by.bsuir.Common.SortOrder;
import by.bsuir.CreditCalculator.DomainModel.Credit;
import by.bsuir.CreditCalculator.Foundation.Credits.CreateCreditError;
import by.bsuir.CreditCalculator.Foundation.Credits.DeleteCreditError;
import by.bsuir.CreditCalculator.Foundation.Credits.UpdateCreditError;
import by.bsuir.CreditCalculator.Foundation.Interfaces.ICreditService;
import by.bsuir.CreditCalculator.Repositories.Implementations.Credit.SortField;
import by.bsuir.CreditCalculator.Web.Constants.Credits.CalculateCreditValidationError;
import by.bsuir.CreditCalculator.Web.Constants.Routes;
import by.bsuir.CreditCalculator.Web.DataContracts.Credit.*;
import by.bsuir.CreditCalculator.Web.Security.AuthorizationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Routes.CREDITS)
public class CreditController extends RestControllerBase {
    private final ICreditService _creditService;
    private final INumberConverter _numberConverter;


    @Autowired
    public CreditController(
            IErrorMessageProvider errorMessageProvider,
            ICreditService creditService,
            INumberConverter numberConverter) {
        super(errorMessageProvider);

        _creditService = creditService;
        _numberConverter = numberConverter;
    }


    @RequestMapping(value = Routes.CALCULATE_CREDIT, method = RequestMethod.GET)
    public ResponseEntity<IDataContract> calculateCredit(
            @RequestParam String desiredSum,
            @RequestParam String monthsCount,
            @RequestParam String interestRate) {
        OperationResult<CalculateCreditValidationError, Credit> createCreditResult = createFrom(
                desiredSum,
                monthsCount,
                interestRate);
        if (!createCreditResult.isSuccessful()) {
            return createBadRequestResponseEntityFrom(createCreditResult.getErrors());
        }

        Credit calculatedCredit = _creditService.calculateCredit(createCreditResult.getResult());
        CalculateCreditDataContract calculateCreditDataContract = createFrom(calculatedCredit);

        return new ResponseEntity<>(calculateCreditDataContract, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<IDataContract> getCredits(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "25") Integer pageSize,
            @RequestParam(required = false, defaultValue = CreditSortFields.NAME) String sortField,
            @RequestParam(required = false, defaultValue = SortOrders.ASCENDING) String sortOrder,
            @RequestParam(required = false, defaultValue = "") String filter
    ) throws Exception {
        SortField sortingField = getSortFieldFrom(sortField);
        SortOrder sortingOrder = getSortOrderFrom(sortOrder);
                String ownerEmail = AuthorizationContext.getAuthenticatedUserEmail();
        List<Credit> credits = _creditService.getCredits(ownerEmail, page, pageSize, sortingField, sortingOrder, filter);
        long creditsCount = _creditService.countCredits(ownerEmail, filter);

        GetCreditsDataContract getCreditsDataContract = createFrom(credits, creditsCount);

        return new ResponseEntity<>(getCreditsDataContract, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<IDataContract> saveCredit(@RequestBody SaveCreditDataContract saveCreditDataContract) throws Exception {
        Credit credit = createFrom(saveCreditDataContract);
        String ownerEmail = AuthorizationContext.getAuthenticatedUserEmail();
        if (ownerEmail == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        OperationResult<CreateCreditError, Credit> createCreditResult = _creditService.createCredit(credit, ownerEmail);
        if (!createCreditResult.isSuccessful()) {
            ErrorDataContract errorDataContract = createErrorDataContract(createCreditResult, CreateCreditError.class);

            return new ResponseEntity<>(errorDataContract, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<IDataContract> updateCredit(@RequestBody UpdateCreditDataContract updateCreditDataContract)
            throws Exception {
        String ownerEmail = AuthorizationContext.getAuthenticatedUserEmail();
        String oldName = updateCreditDataContract.getOldName();
        Credit credit = createFrom(updateCreditDataContract);

        OperationResult<UpdateCreditError, Credit> updateCreditResult = _creditService.updateCredit(
                credit,
                oldName,
                ownerEmail);
        if (updateCreditResult.isSuccessful()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        ErrorDataContract errorDataContract = createErrorDataContract(updateCreditResult, UpdateCreditError.class);

        return new ResponseEntity<>(errorDataContract, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<IDataContract> deleteCredit(@RequestParam String name) throws Exception {
        String ownerEmail = AuthorizationContext.getAuthenticatedUserEmail();
        OperationResult<DeleteCreditError, Credit> deleteCreditResult = _creditService.deleteCredit(name, ownerEmail);
        if (deleteCreditResult.isSuccessful()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        ErrorDataContract errorDataContract = createErrorDataContract(deleteCreditResult, DeleteCreditError.class);

        return new ResponseEntity<>(errorDataContract, HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<IDataContract> createBadRequestResponseEntityFrom(
            List<CalculateCreditValidationError> calculateCreditValidationErrors) {
        ErrorDataContract errorDataContract = createErrorDataContract(
                calculateCreditValidationErrors,
                CalculateCreditValidationError.class);

        return new ResponseEntity<>(errorDataContract, HttpStatus.BAD_REQUEST);
    }

    private OperationResult<CalculateCreditValidationError, Credit> createFrom(
            String desiredSumString,
            String monthsCountString,
            String interestRateString) {
        List<CalculateCreditValidationError> errors = new ArrayList<>();
        OperationResult<ConvertNumberError, Double> convertDesiredSumResult = _numberConverter.convertToDouble(
                desiredSumString);
        if (!convertDesiredSumResult.isSuccessful() || convertDesiredSumResult.getResult() <= 0) {
            errors.add(CalculateCreditValidationError.INVALID_DESIRED_SUM);
        }
        OperationResult<ConvertNumberError, Double> convertMonthsCountResult = _numberConverter.convertToDouble(
                monthsCountString);
        if (!convertMonthsCountResult.isSuccessful() || convertMonthsCountResult.getResult() <= 0) {
            errors.add(CalculateCreditValidationError.INVALID_MONTHS_COUNT);
        }
        OperationResult<ConvertNumberError, Double> convertInterestRateResult = _numberConverter.convertToDouble(
                interestRateString);
        if (!convertInterestRateResult.isSuccessful() || convertInterestRateResult.getResult() <= 0) {
            errors.add(CalculateCreditValidationError.INVALID_INTEREST_RATE);
        }
        if (errors.size() != 0) {
            return OperationResult.createUnsuccessful(errors);
        }

        Credit credit = new Credit();
        credit.setDesiredSum(convertDesiredSumResult.getResult());
        credit.setInterestRate(convertInterestRateResult.getResult());
        credit.setMonthsCount(convertMonthsCountResult.getResult().intValue());

        return OperationResult.createSuccessful(credit);
    }

    private static CalculateCreditDataContract createFrom(Credit credit) {
        return new CalculateCreditDataContract(
                credit.getDesiredSum(),
                credit.getMonthsCount(),
                credit.getInterestRate(),
                credit.getMonthlyCharge(),
                credit.getTotalSum());
    }

    private static Credit createFrom(SaveCreditDataContract saveCreditDataContract) {
        return createFrom(
                saveCreditDataContract.getName(),
                saveCreditDataContract.getDesiredSum(),
                saveCreditDataContract.getMonthsCount(),
                saveCreditDataContract.getInterestRate(),
                saveCreditDataContract.getMonthlyCharge(),
                saveCreditDataContract.getTotalSum());
    }

    private static Credit createFrom(UpdateCreditDataContract updateCreditDataContract) {
        return createFrom(
                updateCreditDataContract.getName(),
                updateCreditDataContract.getDesiredSum(),
                updateCreditDataContract.getMonthsCount(),
                updateCreditDataContract.getInterestRate(),
                updateCreditDataContract.getMonthlyCharge(),
                updateCreditDataContract.getTotalSum());
    }

    private static Credit createFrom(
            String name,
            double desiredSum,
            int monthsCount,
            double interestRate,
            double monthlyCharge,
            double totalSum) {
        Credit credit = new Credit();
        credit.setName(name);
        credit.setDesiredSum(desiredSum);
        credit.setMonthsCount(monthsCount);
        credit.setInterestRate(interestRate);
        credit.setMonthlyCharge(monthlyCharge);
        credit.setTotalSum(totalSum);

        return credit;
    }

    private static SortField getSortFieldFrom(String sortField) {
        switch (sortField) {
            case CreditSortFields.NAME:
                return SortField.NAME;
            case CreditSortFields.CREATE_DATE:
                return SortField.CREATED_DATE;
            case CreditSortFields.DESIRED_SUM:
                return SortField.DESIRED_SUM;
            case CreditSortFields.INTEREST_RATE:
                return SortField.INTEREST_RATE;
            case CreditSortFields.MONTHLY_CHARGE:
                return SortField.MONTHLY_CHARGE;
            case CreditSortFields.MONTHS_COUNT:
                return SortField.MONTHS_COUNT;
            case CreditSortFields.TOTAL_SUM:
                return SortField.TOTAL_SUM;
            default:
                return SortField.NAME;
        }
    }

    private static SortOrder getSortOrderFrom(String sortOrder) {
        switch (sortOrder) {
            case SortOrders.ASCENDING:
                return SortOrder.ASCENDING;
            case SortOrders.DESCENDING:
                return SortOrder.DESCENDING;
            default:
                return SortOrder.ASCENDING;
        }
    }

    private GetCreditsDataContract createFrom(List<Credit> credits, long creditsCount) {
        List<GetCreditsCreditDataContract> creditDataContracts = credits.stream()
                .map(this::createCreditDataContractFrom)
                .collect(Collectors.toList());
        GetCreditsDataContract getCreditsDataContract = new GetCreditsDataContract();
        getCreditsDataContract.setCredits(creditDataContracts);
        getCreditsDataContract.setCreditsCount(creditsCount);

        return getCreditsDataContract;
    }

    private GetCreditsCreditDataContract createCreditDataContractFrom(Credit credit) {
        GetCreditsCreditDataContract getCreditsCreditDataContract = new GetCreditsCreditDataContract();
        getCreditsCreditDataContract.setCreateDate(credit.getCreatedDate());
        getCreditsCreditDataContract.setDesiredSum(credit.getDesiredSum());
        getCreditsCreditDataContract.setInterestRate(credit.getInterestRate());
        getCreditsCreditDataContract.setMonthlyCharge(credit.getMonthlyCharge());
        getCreditsCreditDataContract.setMonthsCount(credit.getMonthsCount());
        getCreditsCreditDataContract.setName(credit.getName());
        getCreditsCreditDataContract.setTotalSum(credit.getTotalSum());

        return getCreditsCreditDataContract;
    }


    private static class CreditSortFields {
        static final String NAME = "name";
        static final String CREATE_DATE = "createDate";
        static final String DESIRED_SUM = "desiredSum";
        static final String MONTHS_COUNT = "monthsCount";
        static final String INTEREST_RATE = "interestRate";
        static final String MONTHLY_CHARGE = "monthlyCharge";
        static final String TOTAL_SUM = "totalSum";
    }


    private static class SortOrders {
        static final String ASCENDING = "asc";
        static final String DESCENDING = "desc";
    }
}
