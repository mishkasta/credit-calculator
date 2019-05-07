import React from 'react';
import PropTypes from 'prop-types';

import { CloseGlyph } from '../glyphs/Glyphs';

import styles from './Alerts.scss';

class Alert extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            shouldShowYourself: true,
        };
    }

    close = () => {
        this.setState({ shouldShowYourself: false });
    };

    render() {
        const { content, alertStyle } = this.props;
        const { shouldShowYourself } = this.state;

        if (!shouldShowYourself) {
            return null;
        }

        return (
            <div className={`${styles.alert} ${alertStyle}`}>
                <div className={styles.alert__text}>{content.text}</div>
                <button className={styles.alert__button} type="button" onClick={() => this.close()}>
                    <CloseGlyph />
                </button>
            </div>
        );
    }
}

Alert.propTypes = {
    content: PropTypes.shape().isRequired,
    alertStyle: PropTypes.string.isRequired,
};

export const WarningAlert = text => (
    <Alert content={text} alertStyle={styles['alert-warning']} />
);

export const ErrorAlert = text => (
    <Alert content={text} alertStyle={styles['alert-error']} />
);
