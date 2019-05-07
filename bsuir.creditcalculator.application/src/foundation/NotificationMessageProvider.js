import { Errors, Warnings } from '../constants/notifications';
import { getLocale } from '../shared/localization/LocalizationContext';

class NotificationMessageProvider {
    static provideErrorMessage = error => {
        const messages = getLocale();

        switch (error) {
            case Errors.AUTHENTICATION_FAILED:
                return messages.authenticationFailed;
            case Errors.AUTHORIZATION_FAILED:
                return messages.authorizationFailed;
            case Errors.CONNECTION_LOST:
                return messages.connectionLost;
            case Errors.INTERNAL_SERVER_ERROR:
                return messages.internalServerError;
            default:
                return messages.unknownError;
        }
    };

    static provideWarningMessage = warning => {
        const messages = getLocale();

        switch (warning) {
            case Warnings.DELETE_YOURSELF_PLEASE:
                return messages.pleaseDeleteYourself;
            case Warnings.SOMETHING_IS_GOING_WRONG:
                return messages.somethingGoingWrong;
            default:
                return messages.unknownError;
        }
    };
}

export default NotificationMessageProvider;
