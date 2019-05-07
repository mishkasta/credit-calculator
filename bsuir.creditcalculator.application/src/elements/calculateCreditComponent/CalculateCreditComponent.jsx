import React from 'react';
import PropTypes from 'prop-types';
import { Form, Button } from 'react-bootstrap';

import PopupWrapper from '../popupWrapper/PopupWrapper';
import SaveCreditComponent from './saveCredit/SaveCreditComponent';
import NumberFormatter from '../../foundation/NumberFormatter';
import CreditService from '../../foundation/CreditService';
import { localized } from '../../shared/localization/LocalizationContext';
import { withLoadingIndication } from '../../shared/loadingIndication/LoadingIndicationStore';
import { withAds } from '../ads/ads';
import Validator from '../../foundation/Validator';

import styles from './CalculateCreditComponent.scss';

class CalculateCreditComponent extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            desiredSum: '',
            monthsCount: '',
            interestRate: '',
            monthlyCharge: '',
            totalSum: '',
            isCreditCalculated: false,
            shouldShowSaveCreditDialog: false,
            isDesiredSumValid: false,
            isMonthsCountValid: false,
            isInterestRateValid: false,
            isNameValid: true,
        };

        this.shouldValidate = false;

        const { showLoadingIndication, hideLoadingIndication,  } = props;
        this.showLoading = showLoadingIndication;
        this.hideLoading = hideLoadingIndication;
    }

    onDesiredSumChange = event => {
        const { value } = event.target;
        const isDesiredSumValid = this.validateDesiredSum(value);

        this.setState({
            desiredSum: value,
            isDesiredSumValid,
        });
    };

    onMonthsCountChange = event => {
        const { value } = event.target;
        const isMonthsCountValid = this.validateMonthsCount(value);

        this.setState({
            monthsCount: value,
            isMonthsCountValid,
        });
    };

    onInterestRateChange = event => {
        const { value } = event.target;
        const isInterestRateValid = this.validateInterestRate(value);

        this.setState({
            interestRate: value,
            isInterestRateValid,
        });
    };

    validateDesiredSum = desiredSum => {
        return !this.shouldValidate ||
            Validator.validateNumber(desiredSum);
    };

    validateMonthsCount = monthsCount => {
        return !this.shouldValidate ||
            Validator.validateNumber(monthsCount);
    };

    validateInterestRate = interestRate => {
        return !this.shouldValidate ||
            Validator.validateNumber(interestRate);
    };

    onCalculateCreditSuccess = calculatedCredit => {
        const { totalSum, monthlyCharge } = calculatedCredit;

        this.shouldValidate = false;

        this.hideLoading();

        this.setState({
            totalSum,
            monthlyCharge,
            isCreditCalculated: true,
        });
    };

    calculateCredit = () => {
        const { desiredSum, monthsCount, interestRate } = this.state;

        this.shouldValidate = true;
        const isDesiredSumValid = this.validateDesiredSum(desiredSum);
        const isMonthsCountValid = this.validateMonthsCount(monthsCount);
        const isInterestRateValid = this.validateInterestRate(interestRate);
        const isFormValid = isDesiredSumValid &&
            isMonthsCountValid &&
            isInterestRateValid;

        this.setState({
            isDesiredSumValid,
            isMonthsCountValid,
            isInterestRateValid,
        });

        if (!isFormValid) {
            return;
        }

        this.showLoading();

        const normalizedDesiredSum = Math.abs(desiredSum);
        const normalizedMonthsCount = Math.abs(monthsCount);
        const normalizedInterestRate = Math.abs(interestRate);
        CreditService.calculateCredit(
            normalizedDesiredSum,
            normalizedMonthsCount,
            normalizedInterestRate,
            this.onCalculateCreditSuccess);
    };

    onSuccessfulCreditSave = () => {
        this.hideLoading();

        this.setState({
            desiredSum: '',
            monthsCount: '',
            interestRate: '',
            monthlyCharge: '',
            totalSum: '',
            isCreditCalculated: false,
            shouldShowSaveCreditDialog: false,
        });
    };

    onCreditSaveError = () => {
        this.hideLoading();

        this.setState({
            isNameValid: false,
        });
    };

    saveCredit = (creditName) => {
        const {
            desiredSum,
            monthsCount,
            interestRate,
            totalSum,
            monthlyCharge,
        } = this.state;

        const credit = {
            name: creditName,
            desiredSum,
            monthsCount,
            interestRate,
            totalSum,
            monthlyCharge,
        };

        this.showLoading();

        CreditService.saveCreditAsync(
            credit,
            this.onSuccessfulCreditSave,
            this.onCreditSaveError);
    };

    showSaveCreditDialog = () => {
        this.setState({
            shouldShowSaveCreditDialog: true,
        });
    };

    closeSaveCreditDialog = () => {
        this.setState({
            shouldShowSaveCreditDialog: false,
        });
    };

    render() {
        const { isUserAuthenticated, messages } = this.props;
        const {
            creditCalculator,
            colon,
            desiredSum: desiredSumMessage,
            enterDesiredSum,
            monthsCount: monthsCountMessage,
            enterMonthsCount,
            interestRate: interestRateMessage,
            enterInterestRate,
            calculate,
            save,
            monthlyCharge: monthlyChargeMessage,
            totalSum: totalSumMessage,
            pleaseEnterValidDesiredSum,
            pleaseEnterValidMonthsCount,
            pleaseEnterValidInterestRate,
        } = messages;

        const {
            isCreditCalculated,
            monthlyCharge,
            totalSum,
            shouldShowSaveCreditDialog,
            isDesiredSumValid,
            isMonthsCountValid,
            isInterestRateValid,
            isNameValid,
        } = this.state;

        const isFormValid = !this.shouldValidate ||
            isDesiredSumValid &&
            isMonthsCountValid &&
            isInterestRateValid;

        return (
            <Form className={`${styles['common-form']} ${styles['calculate-credit-form']}`}>
                <PopupWrapper onClose={this.closeSaveCreditDialog} isVisible={shouldShowSaveCreditDialog}>
                    <SaveCreditComponent submit={this.saveCredit} isNameValid={isNameValid} />
                </PopupWrapper>
                <h2 className={styles['common-form__caption']}>
                    {creditCalculator}
                </h2>
                <div className={styles['common-form__content']}>
                    <div className={styles['common-form__content-inputs']}>
                        <Form.Group controlId="desiredSum">
                            <Form.Label>{`${desiredSumMessage}${colon}`}</Form.Label>
                            <Form.Control
                                type="text"
                                isInvalid={this.shouldValidate && !isDesiredSumValid}
                                placeholder={enterDesiredSum}
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
                                placeholder={enterMonthsCount}
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
                                placeholder={enterInterestRate}
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
                <div
                    className={isCreditCalculated ? styles['common-form__bottom'] : styles['common-form__bottom-centered']}
                >
                    <div className={styles['calculate-credit-form__button-block']}>
                        <Button
                            variant="primary"
                            disabled={!isFormValid}
                            onClick={() => this.calculateCredit()}
                        >
                            {calculate}
                        </Button>
                        {isUserAuthenticated && isCreditCalculated && (
                            <Button
                                className={styles['save-button']}
                                variant="primary"
                                disabled={!isFormValid}
                                onClick={() => this.showSaveCreditDialog()}
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
                                {NumberFormatter.format(monthlyCharge)}
                            </div>
                            <div className={styles['calculate-credit-form__result-caption']}>
                                {`${totalSumMessage}${colon}`}
                            </div>
                            <div className={styles['calculate-credit-form__result']}>
                                {NumberFormatter.format(totalSum)}
                            </div>
                        </div>
                    )}
                </div>
            </Form>
        );
    }
}

CalculateCreditComponent.propTypes = {
    isUserAuthenticated: PropTypes.bool.isRequired,
    messages: PropTypes.shape().isRequired,
    showLoadingIndication: PropTypes.func.isRequired,
    hideLoadingIndication: PropTypes.func.isRequired,
};

export default localized(
    withLoadingIndication(
        withAds(
            CalculateCreditComponent
        )
    )
);
