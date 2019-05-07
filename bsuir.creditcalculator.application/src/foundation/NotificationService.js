const onNewErrorCallbacks = [];
const onNewWarningCallbacks = [];

const notifyAboutError = error => {
    onNewErrorCallbacks.forEach(callback => callback(error));
};

const notifyAboutWarning = warning => {
    onNewWarningCallbacks.forEach(callback => callback(warning));
};

class NotificationService {
    static notifyError = error => {
        notifyAboutError(error);
    };

    static notifyWarning = warning => {
        notifyAboutWarning(warning);
    };

    static onNewError = callback => {
        onNewErrorCallbacks.push(callback);
    };

    static onNewWarning = callback => {
        onNewWarningCallbacks.push(callback);
    };
}

export default NotificationService;
