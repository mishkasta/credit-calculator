package by.bsuir.CreditCalculator.Web.Constants;

import by.bsuir.Common.Interfaces.IErrorMessageProvider;
import by.bsuir.CreditCalculator.Foundation.Credits.CreateCreditError;
import by.bsuir.CreditCalculator.Foundation.Credits.DeleteCreditError;
import by.bsuir.CreditCalculator.Foundation.Credits.UpdateCreditError;
import by.bsuir.CreditCalculator.Foundation.Users.CreateUserError;
import by.bsuir.CreditCalculator.Web.Constants.Credits.*;
import by.bsuir.CreditCalculator.Web.Constants.User.CreateUserErrorConstants;
import by.bsuir.CreditCalculator.Web.Constants.User.UserValidationError;

import java.util.HashMap;
import java.util.Map;

import static by.bsuir.CreditCalculator.Web.Constants.User.UserValidationErrorConstants.*;

public class ErrorMessageProvider implements IErrorMessageProvider {
    private static final Map<Class<? extends Enum>, Map<? extends Enum, String>> ERRORS = createErrors();


    @Override
    public <TErrorType extends Enum> String provide(TErrorType error, Class<TErrorType> errorType) {
        Map<? extends Enum, String> errors = ERRORS.get(errorType);
        if (errors == null) {
            return CommonErrors.UNKNOWN;
        }
        String errorMessage = errors.get(error);
        if (errorMessage == null) {
            return CommonErrors.UNKNOWN;
        }

        return errorMessage;
    }


    private static Map<UserValidationError, String> createUserValidationErrors() {
        Map<UserValidationError, String> validationErrorToStrings = new HashMap<>();
        validationErrorToStrings.put(UserValidationError.INVALID_EMAIL, INVALID_EMAIL);
        validationErrorToStrings.put(UserValidationError.INVALID_PASSWORD, INVALID_PASSWORD);
        validationErrorToStrings.put(UserValidationError.INVALID_USERNAME, INVALID_USERNAME);

        return validationErrorToStrings;
    }

    private static Map<CreateUserError, String> createCreateUserValidationErrors() {
        Map<CreateUserError, String> map = new HashMap<>();
        map.put(CreateUserError.INVALID_PASSWORD, CreateUserErrorConstants.INVALID_PASSWORD);
        map.put(CreateUserError.EMAIL_NOT_UNIQUE, CreateUserErrorConstants.EMAIL_NOT_UNIQUE);
        map.put(CreateUserError.ROLE_NOT_FOUND, CreateUserErrorConstants.ROLE_NOT_FOUND);
        map.put(CreateUserError.INVALID_EMAIL, CreateUserErrorConstants.INVALID_EMAIL);
        map.put(CreateUserError.INVALID_FIRST_NAME, CreateUserErrorConstants.INVALID_USER_NAME);
        map.put(CreateUserError.INVALID_SECOND_NAME, CreateUserErrorConstants.INVALID_USER_NAME);

        return map;
    }

    private static Map<CalculateCreditValidationError, String> createCalculateCreditValidationErrors() {
        Map<CalculateCreditValidationError, String> map = new HashMap<>();
        map.put(CalculateCreditValidationError.INVALID_DESIRED_SUM, CreditValidationErrorConstants.INVALID_DESIRED_SUM);
        map.put(CalculateCreditValidationError.INVALID_MONTHS_COUNT, CreditValidationErrorConstants.INVALID_MONTHS_COUNT);
        map.put(CalculateCreditValidationError.INVALID_INTEREST_RATE, CreditValidationErrorConstants.INVALID_INTEREST_RATE);

        return map;
    }

    private static Map<CreateCreditError, String> createCreateCreditErrors() {
        Map<CreateCreditError, String> map = new HashMap<>();
        map.put(CreateCreditError.USER_NOT_FOUND, CreateCreditErrorConstants.USER_NOT_FOUND);
        map.put(CreateCreditError.NAME_IS_NOT_UNIQUE, CreateCreditErrorConstants.NAME_IS_NOT_UNIQUE);

        return map;
    }

    private static Map<UpdateCreditError, String> createUpdateCreditErrors() {
        Map<UpdateCreditError, String> map = new HashMap<>();
        map.put(UpdateCreditError.CREDIT_NOT_FOUND, UpdateCreditErrorConstants.CREDIT_NOT_FOUND);
        map.put(UpdateCreditError.NEW_NAME_ALREADY_USED, UpdateCreditErrorConstants.NEW_NAME_ALREADY_USED);

        return map;
    }

    private static Map<DeleteCreditError, String> createDeleteCreditErrors() {
        Map<DeleteCreditError, String> map = new HashMap<>();
        map.put(DeleteCreditError.CREDIT_NOT_FOUND, DeleteCreditErrorConstants.CREDIT_NOT_FOUND);

        return map;
    }

    private static Map<Class<? extends Enum>, Map<? extends Enum, String>> createErrors() {
        Map<Class<? extends Enum>, Map<? extends Enum, String>> map = new HashMap<>();
        map.put(UserValidationError.class, createUserValidationErrors());
        map.put(CreateUserError.class, createCreateUserValidationErrors());
        map.put(CalculateCreditValidationError.class, createCalculateCreditValidationErrors());
        map.put(CreateCreditError.class, createCreateCreditErrors());
        map.put(UpdateCreditError.class, createUpdateCreditErrors());
        map.put(DeleteCreditError.class, createDeleteCreditErrors());

        return map;
    }
}
