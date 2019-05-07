package by.bsuir.CreditCalculator.Web.Interfaces;

import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Web.Constants.User.UserValidationError;
import by.bsuir.CreditCalculator.Web.DataContracts.User.RegisterNewUserDataContract;

public interface IUserValidator {
    OperationResult<UserValidationError, User> validate(RegisterNewUserDataContract registerNewUserDataContract);
}
