package by.bsuir.CreditCalculator.Foundation.Interfaces;

import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.CreditCalculator.DomainModel.RefreshToken;
import by.bsuir.CreditCalculator.Foundation.RefreshToken.CreateRefreshTokenError;
import by.bsuir.CreditCalculator.Foundation.RefreshToken.DeleteRefreshTokenError;

public interface IRefreshTokenService {
    RefreshToken getRefreshToken(String refreshTokenValue) throws Exception;

    OperationResult<CreateRefreshTokenError, RefreshToken> createRefreshToken(RefreshToken refreshToken, String email) throws Exception;

    OperationResult<DeleteRefreshTokenError, RefreshToken> deleteRefreshToken(RefreshToken refreshToken) throws Exception;
}
