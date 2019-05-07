import jwtDecode from 'jwt-decode';

import HttpUtility from '../shared/utilities/httpUtility';
import LocalStorageUtility from '../shared/utilities/LocalStorageUtility';
import ApiRoutes from '../shared/ApiRoutes';
import { ContentTypeApplicationJson } from '../shared/utilities/HttpHeaders';

const onUserAuthenticatedCallbacks = [];
let isUserAuthenticated = false;
let authenticatedUsername = '';
let isProcessingSignIn = false;

const checkIfUserAuthenticated = () => {

};

const notifyAuthenticationChanged = () => {
    onUserAuthenticatedCallbacks.forEach(callback => callback());
};

const processAccessTokenRetrieved = response => {
    const {
        refresh_token: refreshToken,
        access_token: accessToken,
        token_type: tokenType,
    } = response.data;
    LocalStorageUtility.setAccessToken(accessToken);
    LocalStorageUtility.setRefreshToken(refreshToken);
    LocalStorageUtility.setTokentType(tokenType);

    updateAuthenticationInfo(accessToken);

    notifyAuthenticationChanged();
};

const updateAuthenticationInfo = accessToken => {
    const decodedAccessToken = jwtDecode(accessToken);
    authenticatedUsername = decodedAccessToken.sub;
    isUserAuthenticated = true;
};

class AuthenticationService {
    static checkIfAuthenticated = () => {
        const accessToken = LocalStorageUtility.getAccessToken();
        if (accessToken === null || accessToken === undefined || accessToken === '') {
            return false;
        }

        updateAuthenticationInfo(accessToken);

        return true;
    };

    static getAuthenticatedUserName = () => authenticatedUsername;

    static signOut = () => {
        LocalStorageUtility.clearAuthenticationData();
        isUserAuthenticated = false;
        authenticatedUsername = '';

        notifyAuthenticationChanged();
    };

    static refreshAccessToken = onRefreshedToken => {
        if (isProcessingSignIn) {
            return;
        }
        isProcessingSignIn = true;
        const refreshToken = LocalStorageUtility.getRefreshToken();
        const requestBody = {
            grant_type: 'refresh_token',
            refresh_token: refreshToken,
        };
        HttpUtility.post(
            ApiRoutes.LOGIN,
            { ContentTypeApplicationJson },
            requestBody,
            response => {
                processAccessTokenRetrieved(response);
                isProcessingSignIn = false;
                onRefreshedToken();
            },
            error => {
                AuthenticationService.signOut();
                isProcessingSignIn = false;
            });
    };

    static signIn = (userName, password, onSignInSuccess, onSignInError) => {
        if (isProcessingSignIn) {
            return;
        }
        isProcessingSignIn = true;
        const authenticationDataContract = {
            grant_type: 'password',
            username: userName,
            password,
        };
        HttpUtility.post(
            ApiRoutes.LOGIN,
            {
                ContentTypeApplicationJson,
            },
            authenticationDataContract,
            response => {
                processAccessTokenRetrieved(response);
                onSignInSuccess();
                isProcessingSignIn = false;
            },
            response => {
                onSignInError(response.error);
                isProcessingSignIn = false;
            });
    };

    static signUp = (username, email, password, onSignUpSuccess, onSignUpError) => {
        const registrationDataContract = {
            username,
            email,
            password,
        };
        HttpUtility.post(
            ApiRoutes.REGISTRATION,
            {
                ContentTypeApplicationJson,
            },
            registrationDataContract,
        ).then(() => onSignUpSuccess())
            .catch(error => onSignUpError(error));
    };

    static onAuthenticationChanged = callback => {
        onUserAuthenticatedCallbacks.push(callback);
    };
}

export default AuthenticationService;
