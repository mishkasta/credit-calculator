package by.bsuir.Common.DataContracts;

import by.bsuir.Common.Interfaces.IDataContract;

import java.util.Arrays;
import java.util.List;

public class ErrorDataContract implements IDataContract {
    private List<String> _errors;


    public void setErrors(List<String> errors) {
        _errors = errors;
    }

    public void setErrors(String... errors) {
        _errors = Arrays.asList(errors);
    }

    public List<String> getErrors() {
        return _errors;
    }
}
