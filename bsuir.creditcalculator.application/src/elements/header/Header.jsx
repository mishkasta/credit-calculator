import React from 'react';
import { Link, withRouter } from 'react-router-dom';
import PropTypes from 'prop-types';
import { Button, DropdownButton, Dropdown } from 'react-bootstrap';

import { LinearProgress } from '@material-ui/core';
import Routes from '../../shared/Routes';
import logoBug from '../../resources/logo_bug.svg';
import logoNormal from '../../resources/logo_normal.svg';
import { withLoadingIndication } from '../../shared/loadingIndication/LoadingIndicationStore';
import { Locales, localized } from '../../shared/localization/LocalizationContext';

import styles from './Header.scss';

const navigateTo = (history, path) => {
    history.push(path);
};

const getRandomLogo = () => {
    const shouldShowLogoBug = Math.random() >= 0.5;

    return shouldShowLogoBug ? logoBug : logoNormal;
};

class Header extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            logo: getRandomLogo()
        };
    }

    updateLogo = () => {
        const logo = getRandomLogo();

        this.setState({
            logo
        });
    };

    render() {
        const {
            isAuthenticated,
            authenticatedUsername,
            signOut,
            history,
            messages,
            onLanguageSelect,
            shouldShowLoadingIndication,
        } = this.props;
        const {
            helloUsername,
            myCredits,
            englishLanguage,
            belarussianLanguage,
            russianLanguage,
            calculateCredit,
            signOut: signOutMessage,
            signIn,
        } = messages;
        const { logo } = this.state;

        return (
            <div>
                <div className={styles.header}>
                    <Link to={Routes.DEFAULT}>
                        <img className={styles.header__item} src={logo} alt="LOGO" />
                    </Link>
                    <div className={styles.header__item}>
                        {(isAuthenticated ? (
                            <DropdownButton
                                alignRight
                                title={`${helloUsername}${authenticatedUsername}!`}
                            >
                                <Dropdown.Item onSelect={() => navigateTo(history, Routes.MY_CREDITS)}>
                                    {myCredits}
                                </Dropdown.Item>
                                <Dropdown.Item onSelect={() => navigateTo(history, Routes.CALCULATE_CREDIT)}>
                                    {calculateCredit}
                                </Dropdown.Item>
                                <Dropdown.Divider />
                                <Dropdown.Item onSelect={() => signOut()}>
                                    {signOutMessage}
                                </Dropdown.Item>
                            </DropdownButton>
                        ) : (
                            <Link to={Routes.SIGN_IN}>
                                <Button>{signIn}</Button>
                            </Link>
                        ))}
                        <div className={styles['header__item-divider']} />
                        <DropdownButton variant="secondary" title={messages.language} alignRight>
                            <Dropdown.Item onSelect={() => onLanguageSelect(Locales.ENGLISH)}>
                                {englishLanguage}
                            </Dropdown.Item>
                            <Dropdown.Item onSelect={() => onLanguageSelect(Locales.BELARUSSIAN)}>
                                {belarussianLanguage}
                            </Dropdown.Item>
                            <Dropdown.Item onSelect={() => onLanguageSelect(Locales.RUSSIAN)}>
                                {russianLanguage}
                            </Dropdown.Item>
                        </DropdownButton>
                    </div>
                </div>
                <LinearProgress className={shouldShowLoadingIndication ? '' : styles['loading-indicator_hidden']} />
            </div>
        );
    }
}

Header.propTypes = {
    history: PropTypes.shape().isRequired,
    isAuthenticated: PropTypes.bool.isRequired,
    authenticatedUsername: PropTypes.string.isRequired,
    signOut: PropTypes.func.isRequired,
    messages: PropTypes.shape().isRequired,
    onLanguageSelect: PropTypes.func.isRequired,
    shouldShowLoadingIndication: PropTypes.bool.isRequired,
};

export default withRouter(
    localized(
        withLoadingIndication(
            Header,
        ),
    ),
);
