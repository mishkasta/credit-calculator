import axios from 'axios';

import config from '../../../config/config.json';
import NotificationService from '../../foundation/NotificationService';
import { Errors } from '../../constants/notifications';
import LocalStorageUtility from './LocalStorageUtility';
import AuthenticationService from '../../foundation/AuthenticationService';

let instance;
const getAxiosInstance = () => {
    if (instance === undefined) {
        instance = axios.create({
            baseURL: config.baseUrl,
            timeout: 30000
        });
    }

    return instance;
};

const getAuthorizationHeader = () => {
    const accessToken = LocalStorageUtility.getAccessToken();
    const tokenType = LocalStorageUtility.getTokenType();

    return `${tokenType} ${accessToken}`;
};

const processError = (error, onRejectedCallback, onSolveError) => {
    if (error.response) {
        switch (error.response.status) {
            case 500: {
                NotificationService.notifyError(Errors.INTERNAL_SERVER_ERROR);

                break;
            }
            case 401:
            case 403: {
                AuthenticationService.refreshAccessToken(onSolveError);

                return;
            }
        }

        onRejectedCallback(error.response.data);
    } else if (error.request) {
        NotificationService.notifyError(Errors.CONNECTION_LOST);
    }
};

export default class HttpUtility {
    static get(
        url,
        header = {},
        onFulfilledCallback,
        onRejectedCallback) {
        const config = {
            headers: {
            ...header,
                'Authorization': getAuthorizationHeader(),
            }
        };

        return getAxiosInstance()
            .get(url, config)
            .then(onFulfilledCallback)
            .catch(error => processError(error, onRejectedCallback, () => {
                getAxiosInstance()
                    .get(url, config)
                    .then(onFulfilledCallback)
                    .catch(error => processError(error, onRejectedCallback));
            }));
    }

    static post(
        url,
        header = {},
        data = {},
        onFulfilledCallback,
        onRejectedCallback) {
        const config = {
            headers: {
                ...header,
                'Authorization': getAuthorizationHeader(),
            }
        };

        return getAxiosInstance()
            .post(url, data, config)
            .then(onFulfilledCallback)
            .catch(error => processError(error, onRejectedCallback, () => {
                getAxiosInstance()
                    .get(url, config)
                    .then(onFulfilledCallback)
                    .catch(error => processError(error, onRejectedCallback));
            }));
    }

    static put(
        url,
        header = {},
        data = {},
        onFulfilledCallback,
        onRejectedCallback) {
        const config = {
            headers: {
                ...header,
                'Authorization': getAuthorizationHeader(),
            }
        };

        return getAxiosInstance()
            .put(url, data, config)
            .then(onFulfilledCallback)
            .catch(error => processError(error, onRejectedCallback, () => {
                getAxiosInstance()
                    .get(url, config)
                    .then(onFulfilledCallback)
                    .catch(error => processError(error, onRejectedCallback));
            }));
    }

    static delete(
        url,
        header = {},
        onFulfilledCallback,
        onRejectedCallback) {
        const config = {
            headers: {
                ...header,
                'Authorization': getAuthorizationHeader(),
            }
        };

        return getAxiosInstance()
            .delete(url, config)
            .then(onFulfilledCallback)
            .catch(error => processError(error, onRejectedCallback, () => {
                getAxiosInstance()
                    .get(url, config)
                    .then(onFulfilledCallback)
                    .catch(error => processError(error, onRejectedCallback));
            }));
    }
}
