package by.bsuir.CreditCalculator.Foundation.Interfaces;

public interface ICreateUserValidator {
    boolean checkIfEmailIsValid(String email);

    boolean checkIfPasswordIsValid(String password);

    boolean checkIfFirstNameIsValid(String firstName);

    boolean checkIfSecondNameIsValid(String secondName);
}
