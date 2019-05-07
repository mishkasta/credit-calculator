import React from 'react';
import PropTypes from 'prop-types';
import { Form, Button } from 'react-bootstrap';

import Validator from '../../../foundation/Validator';
import { localized } from '../../../shared/localization/LocalizationContext';

import styles from './SaveCreditComponent.scss';

class SaveCreditComponent extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            creditName: '',
            isCreditNameValid: '',
            shouldSendViaEmail: true,
            isServerRejectedName: false,
        };

        this.shouldValidate = false;
    }

    static getDerivedStateFromProps = (nextProps, previousState) => {
        const { wasSaveRequestSent } = previousState;
        const { isNameValid } = nextProps;
        if (wasSaveRequestSent && !isNameValid) {
            return { isServerRejectedName: true }
        }

        return {};
    };

    onCreditNameChange = event => {
        const { value } = event.target;
        const isCreditNameValid = this.validateCreditName(value);

        this.setState({
            creditName: value,
            isCreditNameValid,
            isServerRejectedName: false,
        });
    };

    onShouldSendToEmailToggle = () => {
        const { shouldSendViaEmail } = this.state;

        this.setState({
            shouldSendViaEmail: !shouldSendViaEmail,
        })
    };

    validateCreditName = creditName => {
        return !this.shouldValidate || Validator.validateCreditName(creditName);
    };

    saveCredit = () => {
        const { submit } = this.props;
        const { creditName, shouldSendViaEmail } = this.state;
        this.shouldValidate = true;
        const isCreditNameValid = this.validateCreditName(creditName);

        this.setState({
            isCreditNameValid,
        });

        if (isCreditNameValid) {
            submit(creditName, shouldSendViaEmail);
        }

        this.shouldValidate = false;
    };

    render() {
        const {
            isCreditNameValid,
            shouldSendViaEmail,
            isServerRejectedName } = this.state;
        const { messages } = this.props;

        const {
            saveCredit,
            enterCreditName,
            creditName: creditNameMessage,
            save,
            colon,
            sendItToMyEmail,
            pleaseEnterValidCreditName,
            thisNameIsAlreadyUsedByYou
        } = messages;

        const isFormValid = isServerRejectedName || !this.shouldValidate || isCreditNameValid;

        return (
            <div className={`${styles['common-form']} ${styles['save-credit-component']}`}>
                <h3 className={styles['common-form__caption']}>
                    {saveCredit}
                </h3>
                <div className={styles['common-form__content']}>
                    <div className={styles['common-form__content-inputs']}>
                        <Form.Group>
                            <Form.Label>{`${creditNameMessage}${colon}`}</Form.Label>
                            <Form.Control
                                isInvalid={isServerRejectedName || this.shouldValidate && !isCreditNameValid}
                                type="text"
                                placeholder={enterCreditName}
                                onChange={this.onCreditNameChange}
                            />
                            {!isServerRejectedName && this.shouldValidate && !isCreditNameValid && (
                                <Form.Text className={styles['common-form__input-feedback_error']}>
                                    {pleaseEnterValidCreditName}
                                </Form.Text>
)}
                            {isServerRejectedName && (
                                <Form.Text className={styles['common-form__input-feedback_error']}>
                                    {thisNameIsAlreadyUsedByYou}
                                </Form.Text>
)}
                        </Form.Group>
                        <Form.Group controlId="shouldSendViaEmail">
                            <Form.Check
                                custom
                                label={sendItToMyEmail}
                                type="checkbox"
                                checked={shouldSendViaEmail}
                                onChange={this.onShouldSendToEmailToggle}
                            />
                        </Form.Group>
                    </div>
                </div>
                <div className={styles['common-form__bottom-centered']}>
                    <Button disabled={!isFormValid} onClick={() => this.saveCredit()}>
                        {save}
                    </Button>
                </div>
            </div>
        );
    }
}

SaveCreditComponent.propTypes = {
    submit: PropTypes.func.isRequired,
    messages: PropTypes.shape().isRequired,
};

export default localized(SaveCreditComponent);
