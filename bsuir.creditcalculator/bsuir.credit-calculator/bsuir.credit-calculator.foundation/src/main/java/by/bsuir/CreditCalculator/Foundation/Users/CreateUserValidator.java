package by.bsuir.CreditCalculator.Foundation.Users;

import by.bsuir.CreditCalculator.Foundation.Interfaces.ICreateUserValidator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class CreateUserValidator implements ICreateUserValidator {
    private static final int MINIMAL_PASSWORD_LENGTH = 6;
    private static final int MINIMAL_NAME_LENGTH = 2;


    @Override
    public boolean checkIfEmailIsValid(String email) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean checkIfPasswordIsValid(String password) {
        return password.length() >= MINIMAL_PASSWORD_LENGTH;
    }

    @Override
    public boolean checkIfFirstNameIsValid(String firstName) {
        return firstName.length() >= MINIMAL_NAME_LENGTH;
    }

    @Override
    public boolean checkIfSecondNameIsValid(String secondName) {
        return secondName.length() >= MINIMAL_NAME_LENGTH;
    }
}
