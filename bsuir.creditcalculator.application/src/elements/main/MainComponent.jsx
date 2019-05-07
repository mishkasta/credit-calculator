import React from 'react';
import { Switch, Route, BrowserRouter } from 'react-router-dom';

import Header from '../header/Header';
import Footer from '../footer/Footer';
import CalculateCreditComponent from '../calculateCreditComponent/CalculateCreditComponent';
import SignInComponent from '../signIn/SignInComponent';
import SignUpComponent from '../signUp/SignUpComponent';
import MyCreditsComponent from '../mycredits/MyCreditsComponent';
import { ErrorAlert, WarningAlert } from '../alerts/Alerts';
import { LocalizationProvider, getLocale, setLocale } from '../../shared/localization/LocalizationContext';
import { LoadingIndicationStore } from '../../shared/loadingIndication/LoadingIndicationStore';
import Routes from '../../shared/Routes';
import NotificationMessageProvider from '../../foundation/NotificationMessageProvider';
import AuthenticationService from '../../foundation/AuthenticationService';
import NotificationService from '../../foundation/NotificationService';

import styles from './MainComponent.scss';

class MainComponent extends React.Component {
    constructor(props) {
        super(props);

        const isUserAuthenticated = AuthenticationService.checkIfAuthenticated();
        const authenticatedUsername = AuthenticationService.getAuthenticatedUserName();

        this.state = {
            isUserAuthenticated,
            authenticatedUsername,
            errors: [],
            warnings: [],
            localizationContext: {
                messages: getLocale(),
                onLanguageSelect: this.onLanguageSelect,
            },
        };
    }

    componentDidMount() {
        AuthenticationService.onAuthenticationChanged(() =>
            this.updateAuthenticationInfo(),
        );
        NotificationService.onNewError(error => this.notifyError(error));
        NotificationService.onNewWarning(warning => this.notifyWarning(warning));
    }

    updateAuthenticationInfo = () => {
        const { state } = this;
        const isUserAuthenticated = AuthenticationService.checkIfAuthenticated();
        const authenticatedUsername = AuthenticationService.getAuthenticatedUserName();

        this.setState({
            ...state,
            isUserAuthenticated,
            authenticatedUsername,
        });
    };

    onLanguageSelect = language => {
        setLocale(language);

        const { localizationContext } = this.state;
        localizationContext.messages = getLocale(language);

        this.setState({ localizationContext });
    };

    notifyError = error => {
        const { errors } = this.state;
        errors.push(error);

        this.setState({ errors });
    };

    notifyWarning = warning => {
        const { warnings } = this.state;
        warnings.push(warning);

        this.setState({ warnings });
    };

    render() {
        const {
            isUserAuthenticated,
            authenticatedUsername,
            errors,
            warnings,
            localizationContext,
        } = this.state;

        document.title = localizationContext.messages.applicationName;

        return (
            <BrowserRouter>
                <LocalizationProvider value={localizationContext}>
                    <LoadingIndicationStore>
                        <div className={styles.page}>
                            <Header
                                isAuthenticated={isUserAuthenticated}
                                authenticatedUsername={authenticatedUsername}
                                signOut={() => AuthenticationService.signOut()}
                            />
                            <div className={styles['notifications-container']}>
                                {errors.map((e) => (
                                    <ErrorAlert
                                        key={e.id}
                                        text={NotificationMessageProvider.provideErrorMessage(e)}
                                    />
))}
                                {warnings.map((w) => (
                                    <WarningAlert
                                        key={w.id}
                                        text={NotificationMessageProvider.provideWarningMessage(w)}
                                    />
))}
                            </div>
                            <main className={styles.main}>
                                <Switch>
                                    <Route
                                        path={Routes.MY_CREDITS}
                                        render={() => (
                                            <MyCreditsComponent />)}
                                    />
                                    <Route
                                        path={Routes.SIGN_IN}
                                        render={() => (<SignInComponent signIn={AuthenticationService.signIn} />)}
                                    />
                                    <Route
                                        path={Routes.SIGN_UP}
                                        render={() => (<SignUpComponent signUp={AuthenticationService.signUp} />)}
                                    />
                                    <Route
                                        path={Routes.CALCULATE_CREDIT}
                                        render={() => (
                                            <CalculateCreditComponent isUserAuthenticated={isUserAuthenticated} />)}
                                    />
                                    <Route
                                        path={Routes.DEFAULT}
                                        render={() => (
                                            <CalculateCreditComponent isUserAuthenticated={isUserAuthenticated} />)}
                                    />
                                </Switch>
                            </main>
                            <Footer />
                        </div>
                    </LoadingIndicationStore>
                </LocalizationProvider>
            </BrowserRouter>
        );
    }
}

export default MainComponent;
