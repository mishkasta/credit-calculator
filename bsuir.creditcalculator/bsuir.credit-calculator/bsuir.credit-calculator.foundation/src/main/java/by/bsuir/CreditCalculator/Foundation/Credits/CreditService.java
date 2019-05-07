package by.bsuir.CreditCalculator.Foundation.Credits;

import by.bsuir.Common.Interfaces.IJpqlRepository;
import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.Common.SortOrder;
import by.bsuir.CreditCalculator.DomainModel.Credit;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Foundation.Interfaces.ICreditService;
import by.bsuir.CreditCalculator.Foundation.Specifications.Credit.GetCreditByNameForUserSpecification;
import by.bsuir.CreditCalculator.Foundation.Specifications.User.EmailSpecification;
import by.bsuir.CreditCalculator.Repositories.Implementations.Credit.SortField;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditCalculatorUnitOfWorkFactory;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditCalulatorUnitOfWork;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditRepository;

import java.util.Date;
import java.util.List;

public class CreditService implements ICreditService {
    private static final double MONTHS_IN_YEAR = 12;

    private final ICreditCalculatorUnitOfWorkFactory _unitOfWorkFactory;


    public CreditService(ICreditCalculatorUnitOfWorkFactory unitOfWorkFactory) {
        _unitOfWorkFactory = unitOfWorkFactory;
    }


    @Override
    public Credit calculateCredit(Credit credit) {
        double totalSum = calculateTotalSum(
                credit.getDesiredSum(),
                credit.getMonthsCount(),
                credit.getInterestRate());
        double monthlyCharge = calculateMonthlyCharge(totalSum, credit.getMonthsCount());

        credit.setMonthlyCharge(monthlyCharge);
        credit.setTotalSum(totalSum);

        return credit;
    }

    @Override
    public List<Credit> getCredits(
            String ownerEmail,
            int page,
            int pageSize,
            SortField sortField,
            SortOrder sortOrder,
            String filter) throws Exception {
        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            ICreditRepository creditRepository = unitOfWork.getCreditRepository();
            List<Credit> credits = creditRepository.getCredits(
                    ownerEmail,
                    page * pageSize,
                    pageSize,
                    sortOrder,
                    sortField,
                    filter);

            return credits;
        }
    }

    @Override
    public OperationResult<CreateCreditError, Credit> createCredit(Credit credit, String ownerEmail) throws Exception {
        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            IJpqlRepository<User> userRepository = unitOfWork.getRepository(User.class);
            User dbUser = userRepository.getSingleOrDefault(new EmailSpecification(ownerEmail));
            if (dbUser == null) {
                return OperationResult.createUnsuccessful(CreateCreditError.USER_NOT_FOUND);
            }

            ICreditRepository creditRepository = unitOfWork.getCreditRepository();
            Credit dbCredit = creditRepository.getSingleOrDefaultFor(
                    new GetCreditByNameForUserSpecification(credit.getName()),
                    ownerEmail);
            if (dbCredit != null) {
                return OperationResult.createUnsuccessful(CreateCreditError.NAME_IS_NOT_UNIQUE);
            }

            credit.setUser(dbUser);
            credit.setCreatedDate(new Date());

            creditRepository.add(credit);

            unitOfWork.saveChanges();

            return OperationResult.createSuccessful();
        }
    }

    @Override
    public OperationResult<UpdateCreditError, Credit> updateCredit(Credit credit, String oldName, String ownerEmail)
            throws Exception {
        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            ICreditRepository creditRepository = unitOfWork.getCreditRepository();
            Credit dbCreditWithOldName = creditRepository.getSingleOrDefaultFor(
                    new GetCreditByNameForUserSpecification(credit.getName()),
                    ownerEmail);
            if (dbCreditWithOldName != null) {
                return OperationResult.createUnsuccessful(UpdateCreditError.NEW_NAME_ALREADY_USED);
            }
            Credit dbCredit = creditRepository.getSingleOrDefaultFor(
                    new GetCreditByNameForUserSpecification(oldName),
                    ownerEmail);
            if (dbCredit == null) {
                return OperationResult.createUnsuccessful(UpdateCreditError.CREDIT_NOT_FOUND);
            }

            dbCredit.setName(credit.getName());
            dbCredit.setTotalSum(credit.getTotalSum());
            dbCredit.setMonthlyCharge(credit.getMonthlyCharge());
            dbCredit.setDesiredSum(credit.getDesiredSum());
            dbCredit.setInterestRate(credit.getInterestRate());
            dbCredit.setMonthsCount(credit.getMonthsCount());

            creditRepository.update(dbCredit);
            unitOfWork.saveChanges();

            return OperationResult.createSuccessful();
        }
    }

    @Override
    public OperationResult<DeleteCreditError, Credit> deleteCredit(String name, String ownerEmail) throws Exception {
        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            ICreditRepository creditRepository = unitOfWork.getCreditRepository();
            Credit dbCredit = creditRepository.getSingleOrDefaultFor(
                    new GetCreditByNameForUserSpecification(name),
                    ownerEmail);
            if (dbCredit == null) {
                return OperationResult.createUnsuccessful(DeleteCreditError.CREDIT_NOT_FOUND);
            }

            creditRepository.delete(dbCredit);
            unitOfWork.saveChanges();

            return OperationResult.createSuccessful();
        }
    }

    @Override
    public long countCredits(String ownerEmail, String filter) throws Exception {
        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            ICreditRepository creditRepository = unitOfWork.getCreditRepository();
            long creditsCount = creditRepository.countCredits(ownerEmail, filter);

            return creditsCount;
        }
    }


    private double calculateTotalSum(double desiredSum, int monthsCount, double interestRate) {
        interestRate = interestRate / 100;
        double totalInterestRate = (monthsCount / MONTHS_IN_YEAR) * interestRate;

        return desiredSum * totalInterestRate + desiredSum;
    }

    private double calculateMonthlyCharge(double totalSum, int monthsCount) {
        return totalSum / monthsCount;
    }
}
