package by.bsuir.CreditCalculator.Web.Validators;

import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Web.Constants.User.UserValidationError;
import by.bsuir.CreditCalculator.Web.DataContracts.User.RegisterNewUserDataContract;
import by.bsuir.CreditCalculator.Web.Interfaces.IUserValidator;

public class UserValidator implements IUserValidator {
    @Override
    public OperationResult<UserValidationError, User> validate(RegisterNewUserDataContract registerNewUserDataContract) {
        return OperationResult.createSuccessful();
    }
}
