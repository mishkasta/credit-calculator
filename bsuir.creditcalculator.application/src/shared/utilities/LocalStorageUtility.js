const ACCESS_TOKEN_LOCALSTORAGE_KEY = 'access_token';
const REFRESH_TOKEN_LOCALSTORAGE_KEY = 'refresh_token';
const TOKEN_TYPE_LOCALSTORAGE_KEY = 'token_type';

export default class LocalStorageUtility {
    static setAccessToken = token => {
        localStorage.setItem(ACCESS_TOKEN_LOCALSTORAGE_KEY, token);
    };

    static getAccessToken = () => {
        return localStorage.getItem(ACCESS_TOKEN_LOCALSTORAGE_KEY);
    };

    static setRefreshToken = token => {
        localStorage.setItem(REFRESH_TOKEN_LOCALSTORAGE_KEY, token);
    };

    static getRefreshToken = () => {
        return localStorage.getItem(REFRESH_TOKEN_LOCALSTORAGE_KEY);
    };

    static setTokentType = type => {
        localStorage.setItem(TOKEN_TYPE_LOCALSTORAGE_KEY, type);
    };

    static getTokenType = () => {
        return localStorage.getItem(TOKEN_TYPE_LOCALSTORAGE_KEY);
    };

    static clearAuthenticationData = () => {
        localStorage.removeItem(TOKEN_TYPE_LOCALSTORAGE_KEY);
        localStorage.removeItem(ACCESS_TOKEN_LOCALSTORAGE_KEY);
        localStorage.removeItem(REFRESH_TOKEN_LOCALSTORAGE_KEY);
    }
};
