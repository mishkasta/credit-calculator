import React from 'react';
import PropTypes from 'prop-types'
import { Button } from 'react-bootstrap';

import styles from './SuccessfulRegistrationDialog.scss';
import { localized } from '../../../shared/localization/LocalizationContext';

const SuccessfulRegistrationDialog = props => {
    const { messages, close } = props;

    return (
        <div className={`${styles['common-form']} ${styles['successful-registration-dialog']}`}>
            <h3 className={styles['common-form__caption']}>{messages.signUpSuccess}</h3>
            <div className={styles['common-form__content']}>{messages.weDidIt}</div>
            <div className={styles['common-form__bottom-centered']}>
                <Button onClick={() => close()}>{messages.gotIt}</Button>
            </div>
        </div>
    );
};

SuccessfulRegistrationDialog.propTypes = {
    messages: PropTypes.shape().isRequired,
    close: PropTypes.func.isRequired
};

export default localized(SuccessfulRegistrationDialog);
