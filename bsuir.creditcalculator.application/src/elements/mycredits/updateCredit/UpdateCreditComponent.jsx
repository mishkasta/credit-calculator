import React from 'react';
import PropTypes from 'prop-types';
import { Button, Form } from 'react-bootstrap';

import PopupWrapper from '../../popupWrapper/PopupWrapper';
import CreditService from '../../../foundation/CreditService';
import { localized } from '../../../shared/localization/LocalizationContext';
import { withLoadingIndication } from '../../../shared/loadingIndication/LoadingIndicationStore';
import Validator from '../../../foundation/Validator';

import styles from './UpdateCreditComponent.scss';

const initialState = {
    desiredSum: 0,
    monthsCount: 0,
    interestRate: 0,
    newCreditName: '',
    monthlyCharge: 0,
    totalSum: 0,
    isCreditCalculated: false,
    isDesiredSumValid: true,
    isMonthsCountValid: true,
    isInterestRateValid: true,
    isNewCreditNameValid: true,
};

class UpdateCreditComponent extends React.Component {
    constructor(props) {
        super(props);

        this.state = initialState;

        this.shouldValidate = false;

        const { showLoadingIndication, hideLoadingIndication } = this.props;
        this.showLoading = showLoadingIndication;
        this.hideLoading = hideLoadingIndication;
    }

    onDesiredSumChange = event => {
        const { value } = event.target;
        const normalizedValue = this.normalizeValue(value);
        const isDesiredSumValid = this.validateDesiredSum(normalizedValue);
        const { isCreditCalculated } = this.state;

        this.setState({
            desiredSum: normalizedValue,
            isDesiredSumValid,
            isCreditCalculated: isCreditCalculated ? normalizedValue !== 0 : isCreditCalculated,
        });
    };

    onMonthsCountChange = event => {
        const { value } = event.target;
        const normalizedValue = this.normalizeValue(value);
        const isMonthsCountValid = this.validateMonthsCount(normalizedValue);
        const { isCreditCalculated } = this.state;

        this.setState({
            monthsCount: normalizedValue,
            isMonthsCountValid,
            isCreditCalculated: isCreditCalculated ? normalizedValue !== 0 : isCreditCalculated,
        });
    };

    onInterestRateChange = event => {
        const { value } = event.target;
        const normalizedValue = this.normalizeValue(value);
        const isInterestRateValid = this.validateInterestRate(normalizedValue);
        const { isCreditCalculated } = this.state;

        this.setState({
            interestRate: normalizedValue,
            isInterestRateValid,
            isCreditCalculated: isCreditCalculated ? normalizedValue !== 0 : isCreditCalculated,
        });
    };

    onCreditNameChange = event => {
        const { value } = event.target;
        const isNewCreditNameValid = this.validateCreditName(value);

        this.setState({
            newCreditName: value,
            isNewCreditNameValid,
        });
    };

    normalizeValue = value => {
        return value === '' ? 0 : value;
    };

    validateDesiredSum = desiredSum => {
        return !this.shouldValidate ||
            desiredSum === 0 ||
            Validator.validateNumber(desiredSum);
    };

    validateMonthsCount = monthsCount => {
        return !this.shouldValidate ||
            monthsCount === 0 ||
            Validator.validateNumber(monthsCount);
    };

    validateInterestRate = interestRate => {
        return !this.shouldValidate ||
            interestRate === 0 ||
            Validator.validateNumber(interestRate);
    };

    validateCreditName = creditName => {
        return !this.shouldValidate ||
            creditName === '' ||
            Validator.validateCreditName(creditName);
    };

    onCalculateCreditSuccess = calculatedCredit => {
        const { totalSum, monthlyCharge } = calculatedCredit;

        this.shouldValidate = false;

        this.hideLoading();

        this.setState({
            totalSum,
            monthlyCharge,
            isCreditCalculated: true,
            isNewNameAlreadyInUse: false,
        });
    };

    calculateCredit = () => {
        const { desiredSum, monthsCount, interestRate } = this.getCredit();

        this.shouldValidate = true;
        const isDesiredSumValid = this.validateDesiredSum(desiredSum);
        const isMonthsCountValid = this.validateMonthsCount(monthsCount);
        const isInterestRateValid = this.validateInterestRate(interestRate);

        if (isDesiredSumValid && isMonthsCountValid && isInterestRateValid) {
            this.showLoading();
            CreditService.calculateCredit(desiredSum, monthsCount, interestRate, this.onCalculateCreditSuccess);
        }

        this.setState({
            isDesiredSumValid,
            isMonthsCountValid,
            isInterestRateValid,
        });
    };

    saveCredit = () => {
        const {
            newCreditName,
        } = this.state;

        this.shouldValidate = true;
        const isNewCreditNameValid = this.validateCreditName(newCreditName);
        if (isNewCreditNameValid) {
            this.showLoading();
            const { creditName } = this.props;
            const credit = this.getCredit();

            CreditService.updateCredit(credit, creditName, this.onUpdateCreditSuccess, this.onUpdateCreditError);
        }

        this.setState({
            isNewCreditNameValid,
        });
    };

    onUpdateCreditSuccess = () => {
        this.hideLoading();
        this.close();
    };

    onUpdateCreditError = () => {
        this.hideLoading();
        this.setState({
            isNewNameAlreadyInUse: true,
        });
    };

    close = () => {
        const { onClose } = this.props;
        this.setState({
            ...initialState,
        });

        onClose();
    };

    getCredit = () => {
        const {
            desiredSum: newDesiredSum,
            monthsCount: newMonthsCount,
            interestRate: newInterestRate,
            newCreditName,
            monthlyCharge,
            totalSum,
        } = this.state;
        const { creditName, desiredSum, monthsCount, interestRate } = this.props;

        return {
            desiredSum: newDesiredSum !== 0 ? newDesiredSum : desiredSum,
            monthsCount: newMonthsCount !== 0 ? newMonthsCount : monthsCount,
            interestRate: newInterestRate !== 0 ? newInterestRate : interestRate,
            name: newCreditName !== '' ? newCreditName : creditName,
            monthlyCharge,
            totalSum,
        };
    };

    checkIfShouldShowCalculateButton = () => {
        const {
            desiredSum,
            monthsCount,
            interestRate,
        } = this.state;

        return desiredSum !== 0 ||
            monthsCount !== 0 ||
            interestRate !== 0;
    };

    render() {
        const {
            creditName,
            desiredSum,
            monthsCount,
            interestRate,
            isShowing,
            messages,
        } = this.props;

        const {
            recalculateCredit,
            newCreditName: newCreditNameMessage,
            colon,
            desiredSum: desiredSumMessage,
            monthsCount: monthsCountMessage,
            interestRate: interestRateMessage,
            calculate,
            save,
            monthlyCharge: monthlyChargeMessage,
            totalSum: totalSumMessage,
            pleaseEnterValidCreditName,
            pleaseEnterValidDesiredSum,
            pleaseEnterValidMonthsCount,
            pleaseEnterValidInterestRate,
            thisNameIsAlreadyInUse,
        } = messages;

        const {
            monthlyCharge,
            totalSum,
            isCreditCalculated,
            isDesiredSumValid,
            isMonthsCountValid,
            isInterestRateValid,
            isNewCreditNameValid,
            isNewNameAlreadyInUse
        } = this.state;

        const canCalculate = !this.shouldValidate ||
            isDesiredSumValid &&
            isMonthsCountValid &&
            isInterestRateValid;
        const canSave = canCalculate && isNewCreditNameValid;

        const shouldShowCalculateButton = this.checkIfShouldShowCalculateButton();

        return (
            <PopupWrapper isVisible={isShowing} onClose={() => this.close()}>
                <Form className={styles['common-form']}>
                    <h2 className={styles['common-form__caption']}>
                        {recalculateCredit}
                    </h2>
                    <div className={styles['common-form__content']}>
                        <div className={styles['common-form__content-inputs']}>
                            <Form.Group controlId="creditName">
                                <Form.Label>{`${newCreditNameMessage}${colon}`}</Form.Label>
                                <Form.Control
                                    type="text"
                                    isInvalid={this.shouldValidate && !isNewCreditNameValid}
                                    placeholder={creditName}
                                    onChange={this.onCreditNameChange}
                                />
                                {this.shouldValidate && !isNewNameAlreadyInUse && !isNewCreditNameValid && (
                                    <Form.Text className={styles['common-form__input-feedback_error']}>
                                        {pleaseEnterValidCreditName}
                                    </Form.Text>
)}
                                {this.shouldValidate && isNewNameAlreadyInUse && (
                                    <Form.Text className={styles['common-form__input-feedback_error']}>
                                        {thisNameIsAlreadyInUse}
                                    </Form.Text>
)}
                            </Form.Group>
                            <Form.Group controlId="desiredSum">
                                <Form.Label>{`${desiredSumMessage}${colon}`}</Form.Label>
                                <Form.Control
                                    type="text"
                                    isInvalid={this.shouldValidate && !isDesiredSumValid}
                                    placeholder={desiredSum}
                                    onChange={this.onDesiredSumChange}
                                />
                                {this.shouldValidate && !isDesiredSumValid && (
                                    <Form.Text className={styles['common-form__input-feedback_error']}>
                                        {pleaseEnterValidDesiredSum}
                                    </Form.Text>
)}
                            </Form.Group>
                            <Form.Group controlId="monthsCount">
                                <Form.Label>{`${monthsCountMessage}${colon}`}</Form.Label>
                                <Form.Control
                                    type="text"
                                    isInvalid={this.shouldValidate && !isMonthsCountValid}
                                    placeholder={monthsCount}
                                    onChange={this.onMonthsCountChange}
                                />
                                {this.shouldValidate && !isMonthsCountValid && (
                                    <Form.Text className={styles['common-form__input-feedback_error']}>
                                        {pleaseEnterValidMonthsCount}
                                    </Form.Text>
)}
                            </Form.Group>
                            <Form.Group controlId="interestRate">
                                <Form.Label>{`${interestRateMessage}${colon}`}</Form.Label>
                                <Form.Control
                                    type="text"
                                    isInvalid={this.shouldValidate && !isInterestRateValid}
                                    placeholder={interestRate}
                                    onChange={this.onInterestRateChange}
                                />
                                {this.shouldValidate && !isInterestRateValid && (
                                    <Form.Text className={styles['common-form__input-feedback_error']}>
                                        {pleaseEnterValidInterestRate}
                                    </Form.Text>
)}
                            </Form.Group>
                        </div>
                    </div>
                    {shouldShowCalculateButton && (
                    <div className={isCreditCalculated ? styles['common-form__bottom'] : styles['common-form__bottom-centered']}>
                        <div className={styles['calculate-credit-form__button-block']}>
                            <Button
                                variant="primary"
                                disabled={!canCalculate}
                                onClick={() => this.calculateCredit()}
                            >
                                {calculate}
                            </Button>
                            {isCreditCalculated && (
                            <Button
                                className={styles['save-button']}
                                variant="primary"
                                disabled={!canSave}
                                onClick={() => this.saveCredit()}
                            >
                                {save}
                            </Button>
)}
                        </div>
                        {isCreditCalculated && (
                        <div className={styles['calculate-credit-form__result-block']}>
                            <div className={styles['calculate-credit-form__result-caption']}>
                                {`${monthlyChargeMessage}${colon}`}
                            </div>
                            <div className={styles['calculate-credit-form__result']}>
                                {monthlyCharge}
                            </div>
                            <div className={styles['calculate-credit-form__result-caption']}>
                                {`${totalSumMessage}${colon}`}
                            </div>
                            <div className={styles['calculate-credit-form__result']}>
                                {totalSum}
                            </div>
                        </div>
                        )}
                    </div>
)}
                </Form>
            </PopupWrapper>
        );
    }
}

UpdateCreditComponent.defaultProps = {
    creditName: '',
    desiredSum: '',
    monthsCount: '',
    interestRate: ''
};

UpdateCreditComponent.propTypes = {
    creditName: PropTypes.string,
    desiredSum: PropTypes.number,
    monthsCount: PropTypes.number,
    interestRate: PropTypes.number,
    isShowing: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    showLoadingIndication: PropTypes.func.isRequired,
    hideLoadingIndication: PropTypes.func.isRequired,
    messages: PropTypes.shape().isRequired
};

export default localized(
    withLoadingIndication(
        UpdateCreditComponent,
    ),
);
