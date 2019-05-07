package by.bsuir.Common.RestControllers;

import by.bsuir.Common.DataContracts.ErrorDataContract;
import by.bsuir.Common.Interfaces.IErrorMessageProvider;
import by.bsuir.Common.OperationResult.OperationResult;

import java.util.List;
import java.util.stream.Collectors;

public abstract class RestControllerBase {
    private final IErrorMessageProvider _errorMessageProvider;


    public RestControllerBase(IErrorMessageProvider errorMessageProvider) {
        _errorMessageProvider = errorMessageProvider;
    }


    protected <TError extends Enum, TResult> ErrorDataContract createErrorDataContract(
        OperationResult<TError, TResult> unsuccessfulOperationResult,
        Class<TError> errorType) {
        return createErrorDataContract(unsuccessfulOperationResult.getErrors(), errorType);
    }

    protected <TError extends Enum> ErrorDataContract createErrorDataContract(
            List<TError> errors,
            Class<TError> errorType) {
        List<String> errorMessages = errors
                .stream()
                .map(e -> _errorMessageProvider.provide(e, errorType))
                .collect(Collectors.toList());

        ErrorDataContract errorDataContract = new ErrorDataContract();
        errorDataContract.setErrors(errorMessages);

        return errorDataContract;
    }
}
