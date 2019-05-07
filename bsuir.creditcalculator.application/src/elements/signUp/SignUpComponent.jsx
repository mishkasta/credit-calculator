import React from "react";
import PropTypes from "prop-types";
import ReactRouterPropTypes from "react-router-prop-types";
import { withRouter } from "react-router-dom";
import { Form, Button } from "react-bootstrap";

import SuccessfulRegistrationDialog from "./successfulRegistrationDialog/SuccessfulRegistrationDialog";
import PopupWrapper from "../popupWrapper/PopupWrapper";
import Routes from "../../shared/Routes";
import { localized } from "../../shared/localization/LocalizationContext";
import { withLoadingIndication } from "../../shared/loadingIndication/LoadingIndicationStore";
import Validator from "../../foundation/Validator";
import { withAds } from "../ads/ads";

import styles from "./SignUpComponent.scss";

class SignUpComponent extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      username: "",
      email: "",
      password: "",
      confirmPassword: "",
      shouldShowSuccessfulSignUpDialog: false,
      isUsernameValid: true,
      isEmailValid: true,
      isPasswordValid: true,
      isConfirmPasswordValid: true,
      isEmailNotUnique: false
    };

    this.shouldValidate = false;

    const { showLoadingIndication, hideLoadingIndication } = props;
    this.showLoading = showLoadingIndication;
    this.hideLoading = hideLoadingIndication;
  }

  onUsernameChange = event => {
    const { value } = event.target;
    const isUsernameValid = this.validateUsername(value);

    this.setState({
      username: value,
      isUsernameValid
    });
  };

  onEmailChange = event => {
    const { value } = event.target;
    const isEmailValid = this.validateEmail(value);

    this.setState({
      email: value,
      isEmailValid
    });
  };

  onPasswordChange = event => {
    const { value } = event.target;
    const isPasswordValid = this.validatePassword(value);

    this.setState({
      password: value,
      isPasswordValid
    });
  };

  onConfirmPasswordChange = event => {
    const { value } = event.target;
    const isConfirmPasswordValid = this.validateConfirmPassword(value);

    this.setState({
      confirmPassword: value,
      isConfirmPasswordValid
    });
  };

  validateUsername = username => {
    return !this.shouldValidate || Validator.validateUsername(username);
  };

  validateEmail = email => {
    return !this.shouldValidate || Validator.validateEmail(email);
  };

  validatePassword = password => {
    return !this.shouldValidate || Validator.validatePassword(password);
  };

  validateConfirmPassword = confirmPassword => {
    const { password } = this.state;

    return (
      !this.shouldValidate ||
      (confirmPassword !== "" && confirmPassword === password)
    );
  };

  onSignUpSuccess = () => {
    this.hideLoading();

    this.setState({
      shouldShowSuccessfulSignUpDialog: true
    });
  };

  onSignUpError = () => {
    this.hideLoading();
    this.setState({
      isEmailNotUnique: true,
      isEmailValid: true
    });
  };

  onSuccessfulSignUpDialogHide = () => {
    const { history } = this.props;
    history.push(Routes.DEFAULT);

    this.setState({
      shouldShowSuccessfulSignUpDialog: false
    });
  };

  signUp = () => {
    const { signUp } = this.props;
    const { username, email, password, confirmPassword } = this.state;

    this.shouldValidate = true;
    const isUsernameValid = this.validateUsername(username);
    const isEmailValid = this.validateEmail(email);
    const isPasswordValid = this.validatePassword(password);
    const isConfirmPasswordValid = this.validateConfirmPassword(
      confirmPassword
    );
    const isFormValid =
      isUsernameValid &&
      isEmailValid &&
      isPasswordValid &&
      isConfirmPasswordValid;

    this.setState({
      isUsernameValid,
      isEmailValid,
      isPasswordValid,
      isConfirmPasswordValid
    });
    if (isFormValid) {
      this.showLoading();
      signUp(
        username,
        email,
        password,
        this.onSignUpSuccess,
        this.onSignUpError
      );
    }
  };

  render() {
    const { messages } = this.props;

    const {
      registration,
      username,
      colon,
      enterUsername,
      emailAddress,
      enterEmail,
      password,
      enterPassword,
      confirmPassword,
      confirmYourPassword,
      signUp,
      pleaseEnterValidEmail,
      passwordRule,
      usernameRule,
      confirmPasswordRule,
      thisEmailIsAlreadyRegistered,
      sendMeBanksSuggestions
    } = messages;

    const {
      shouldShowSuccessfulSignUpDialog,
      isUsernameValid,
      isEmailValid,
      isPasswordValid,
      isConfirmPasswordValid,
      isEmailNotUnique
    } = this.state;

    const isFormValid =
      !this.shouldValidate ||
      (isUsernameValid &&
        isEmailValid &&
        isPasswordValid &&
        isConfirmPasswordValid);

    return (
      <Form className={styles["common-form"]}>
        <PopupWrapper
          isVisible={shouldShowSuccessfulSignUpDialog}
          onClose={this.onSuccessfulSignUpDialogHide}
        >
          <SuccessfulRegistrationDialog
            close={this.onSuccessfulSignUpDialogHide}
          />
        </PopupWrapper>

        <h2 className={styles["common-form__caption"]}>{registration}</h2>
        <div className={styles["common-form__content"]}>
          <div className={styles["common-form__content-inputs"]}>
            <Form.Group controlId="username">
              <Form.Label>{`${username}${colon}`}</Form.Label>
              <Form.Control
                type="text"
                isValid={this.shouldValidate && isUsernameValid}
                isInvalid={!isUsernameValid}
                placeholder={enterUsername}
                onChange={this.onUsernameChange}
              />
              {this.shouldValidate && !isUsernameValid && (
                <Form.Text
                  className={styles["common-form__input-feedback_error"]}
                >
                  {usernameRule}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group controlId="email">
              <Form.Label>{`${emailAddress}${colon}`}</Form.Label>
              <Form.Control
                type="email"
                isValid={this.shouldValidate && isEmailValid}
                isInvalid={!isEmailValid}
                placeholder={enterEmail}
                onChange={this.onEmailChange}
              />
              {this.shouldValidate && !isEmailNotUnique && !isEmailValid && (
                <Form.Text
                  className={styles["common-form__input-feedback_error"]}
                >
                  {pleaseEnterValidEmail}
                </Form.Text>
              )}
              {isEmailNotUnique && (
                <Form.Text
                  className={styles["common-form__input-feedback_error"]}
                >
                  {thisEmailIsAlreadyRegistered}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group controlId="password">
              <Form.Label>{`${password}${colon}`}</Form.Label>
              <Form.Control
                type="password"
                isValid={this.shouldValidate && isPasswordValid}
                isInvalid={!isPasswordValid}
                placeholder={enterPassword}
                onChange={this.onPasswordChange}
              />
              {this.shouldValidate && !isPasswordValid && (
                <Form.Text
                  className={styles["common-form__input-feedback_error"]}
                >
                  {passwordRule}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group controlId="confirmPassword">
              <Form.Label>{`${confirmPassword}${colon}`}</Form.Label>
              <Form.Control
                type="password"
                isValid={this.shouldValidate && isConfirmPasswordValid}
                isInvalid={!isConfirmPasswordValid}
                placeholder={confirmYourPassword}
                onChange={this.onConfirmPasswordChange}
              />
              {this.shouldValidate && !isConfirmPasswordValid && (
                <Form.Text
                  className={styles["common-form__input-feedback_error"]}
                >
                  {confirmPasswordRule}
                </Form.Text>
              )}
            </Form.Group>
            <Form.Group controlId="shouldSubscribeToAds">
              <Form.Check
                custom
                label={sendMeBanksSuggestions}
                defaultChecked
                checked
                type="checkbox"
              />
            </Form.Group>
          </div>
        </div>
        <div className={styles["common-form__bottom-centered"]}>
          <Button
            disabled={!isFormValid && !isEmailNotUnique}
            variant="primary"
            onClick={() => this.signUp()}
          >
            {signUp}
          </Button>
        </div>
      </Form>
    );
  }
}

SignUpComponent.propTypes = {
  signUp: PropTypes.func.isRequired,
  history: ReactRouterPropTypes.history.isRequired,
  messages: PropTypes.shape().isRequired,
  showLoadingIndication: PropTypes.func.isRequired,
  hideLoadingIndication: PropTypes.func.isRequired
};

export default localized(
  withLoadingIndication(withAds(withRouter(SignUpComponent)))
);
