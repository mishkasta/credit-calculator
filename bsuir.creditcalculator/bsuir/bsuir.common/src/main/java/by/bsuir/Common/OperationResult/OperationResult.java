package by.bsuir.Common.OperationResult;

import java.util.*;

public class OperationResult<TError, TResult> {
    private boolean _isSuccessful;
    private List<TError> _errors;
    private TResult _result;


    public boolean isSuccessful() {
        return _isSuccessful;
    }

    public List<TError> getErrors() {
        return _errors;
    }

    public TResult getResult() {
        return _result;
    }


    private OperationResult(boolean isSuccessful, List<TError> errors, TResult result) {
        _isSuccessful = isSuccessful;
        _errors = errors;
        _result = result;
    }


    public static OperationResult createSuccessful() {
        return new OperationResult(true, null, null);
    }

    public static <TError, TResult> OperationResult<TError, TResult> createSuccessful(TResult result) {
        return new OperationResult<>(true, null, result);
    }

    public static OperationResult createUnsuccessful() {
        return new OperationResult(false, null, null);
    }

    @SafeVarargs
    public static <TError, TResult> OperationResult<TError, TResult> createUnsuccessful(TError... errors) {
        return createUnsuccessful(Arrays.asList(errors));
    }

    public static <TError, TResult> OperationResult<TError, TResult> createUnsuccessful(List<TError> errors) {
        return new OperationResult<>(false, errors, null);
    }
}
