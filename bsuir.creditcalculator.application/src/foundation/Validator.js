import ValidationConstants from '../constants/ValidationConstants';

class Validator {
    static validateNumber = number => {
        return ValidationConstants.NUMBER_REGEX.test(`${number}`);
    };

    static validateEmail = email => {
        return ValidationConstants.EMAIL_REGEX.test(email);
    };

    static validatePassword = password => {
        return password.length >= ValidationConstants.PASSWORD_LENGTH;
    };

    static validateCreditName = name => {
        return name.length >= ValidationConstants.CREDIT_NAME_LENGTH;
    };

    static validateUsername = username => {
        return username.length >= ValidationConstants.USERNAME_LENGTH;
    };
}

export default Validator;
