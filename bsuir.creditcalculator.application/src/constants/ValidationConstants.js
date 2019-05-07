const ValidationConstants = {
    PASSWORD_LENGTH: 6,
    EMAIL_REGEX: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/i,
    NUMBER_REGEX: /^[+-]?\d+([.]\d+)?$/,
    CREDIT_NAME_LENGTH: 1,
    USERNAME_LENGTH: 1,
};

export default ValidationConstants;
