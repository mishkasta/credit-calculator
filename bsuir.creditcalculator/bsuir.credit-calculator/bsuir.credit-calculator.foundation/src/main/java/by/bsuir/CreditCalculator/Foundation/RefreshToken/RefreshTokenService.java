package by.bsuir.CreditCalculator.Foundation.RefreshToken;

import by.bsuir.Common.Interfaces.IJpqlRepository;
import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.CreditCalculator.DomainModel.RefreshToken;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IRefreshTokenService;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IUserService;
import by.bsuir.CreditCalculator.Foundation.Specifications.RefreshToken.ValueSpecification;
import by.bsuir.CreditCalculator.Foundation.Users.GetUserError;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditCalculatorUnitOfWorkFactory;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditCalulatorUnitOfWork;

public class RefreshTokenService implements IRefreshTokenService {
    private final IUserService _userService;
    private final ICreditCalculatorUnitOfWorkFactory _unitOfWorkFactory;


    public RefreshTokenService(IUserService userService, ICreditCalculatorUnitOfWorkFactory unitOfWorkFactory) {
        _userService = userService;
        _unitOfWorkFactory = unitOfWorkFactory;
    }


    @Override
    public RefreshToken getRefreshToken(String refreshTokenValue) throws Exception {
        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            IJpqlRepository<RefreshToken> refreshTokenRepository = unitOfWork.getRepository(RefreshToken.class);
            RefreshToken dbRefreshToken = refreshTokenRepository.getFirstOrDefault(new ValueSpecification(refreshTokenValue));

            return dbRefreshToken;
        }
    }

    @Override
    public OperationResult<CreateRefreshTokenError, RefreshToken> createRefreshToken(
            RefreshToken refreshToken,
            String email) throws Exception {
        OperationResult<GetUserError, User> getUserResult = _userService.getByEmail(email);
        if (!getUserResult.isSuccessful()) {
            return OperationResult.createUnsuccessful(CreateRefreshTokenError.USER_NOT_FOUND);
        }

        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            IJpqlRepository<RefreshToken> refreshTokenRepository = unitOfWork.getRepository(RefreshToken.class);
            RefreshToken dbRefreshToken = refreshTokenRepository.getFirstOrDefault(new ValueSpecification(refreshToken.getValue()));
            if (dbRefreshToken != null) {
                return OperationResult.createUnsuccessful(CreateRefreshTokenError.TOKEN_ALREADY_EXISTS);
            }

            dbRefreshToken = new RefreshToken();

            dbRefreshToken.setExpirationDate(refreshToken.getExpirationDate());
            dbRefreshToken.setValue(refreshToken.getValue());
            dbRefreshToken.setUser(getUserResult.getResult());

            refreshTokenRepository.add(dbRefreshToken);
            unitOfWork.saveChanges();

            return OperationResult.createSuccessful();
        }
    }

    @Override
    public OperationResult<DeleteRefreshTokenError, RefreshToken> deleteRefreshToken(RefreshToken refreshToken) throws Exception {
        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            IJpqlRepository<RefreshToken> refreshTokenRepository = unitOfWork.getRepository(RefreshToken.class);
            RefreshToken dbRefreshToken = refreshTokenRepository.getFirstOrDefault(new ValueSpecification(refreshToken.getValue()));
            if (dbRefreshToken == null) {
                return OperationResult.createUnsuccessful(DeleteRefreshTokenError.TOKEN_NOT_FOUND);
            }

            refreshTokenRepository.delete(dbRefreshToken);
            unitOfWork.saveChanges();

            return OperationResult.createSuccessful();
        }
    }
}
