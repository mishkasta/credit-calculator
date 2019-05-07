import React from "react";
import PropTypes from "prop-types";
import { Form, Button } from "react-bootstrap";
import { withRouter, Link } from "react-router-dom";
import ReactRouterPropTypes from "react-router-prop-types";
import Routes from "../../shared/Routes";

import Validator from "../../foundation/Validator";
import { localized } from "../../shared/localization/LocalizationContext";
import { withLoadingIndication } from "../../shared/loadingIndication/LoadingIndicationStore";
import { withAds } from "../ads/ads";

import styles from "./SignInComponent.scss";

class SignInComponent extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      email: "",
      password: "",
      isEmailValid: true,
      isPasswordValid: true,
      isSignInFailed: false
    };

    this.shouldValidate = false;

    const { showLoadingIndication, hideLoadingIndication } = props;
    this.showLoading = showLoadingIndication;
    this.hideLoading = hideLoadingIndication;
  }

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

  validateEmail = email => {
    return this.shouldValidate ? Validator.validateEmail(email) : true;
  };

  validatePassword = password => {
    return this.shouldValidate ? Validator.validatePassword(password) : true;
  };

  onSignInSuccess = () => {
    this.hideLoading();
    const { history } = this.props;

    history.push(Routes.DEFAULT);
  };

  onSignInFailure = errors => {
    this.hideLoading();
    const isEmailValid = !errors.contains('INVALID_EMAIL');
    const isPasswordValid = !errors.contains('INVALID_PASSWORD');

    this.setState({
      isSignInFailed: true,
      isEmailValid,
      isPasswordValid,
    });
  };

  signIn = () => {
    const { signIn } = this.props;
    const { email, password } = this.state;

    this.shouldValidate = true;
    const isEmailValid = this.validateEmail(email);
    const isPasswordValid = this.validatePassword(password);

    this.setState({
      isEmailValid,
      isPasswordValid
    });

    if (isEmailValid && isPasswordValid) {
      this.showLoading();
      signIn(email, password, this.onSignInSuccess, this.onSignInFailure);
    }
  };

  render() {
    const { isEmailValid, isPasswordValid, isSignInFailed } = this.state;
    const { messages } = this.props;
    const {
      signIn,
      emailAddress,
      colon,
      enterEmail,
      pleaseEnterValidEmail,
      invalidUsernameOrPassword,
      enterPassword,
      password: passwordMessage,
      passwordRule,
      registration
    } = messages;
    const isFormValid = isEmailValid && isPasswordValid;

    return (
      <Form className={styles["common-form"]}>
        <h2 className={styles["common-form__caption"]}>{signIn}</h2>
        <div className={styles["common-form__content"]}>
          <div className={styles["common-form__content-inputs"]}>
            <Form.Group controlId="email">
              <Form.Label>{`${emailAddress}${colon}`}</Form.Label>
              <Form.Control
                isValid={this.shouldValidate && isEmailValid}
                isInvalid={!isEmailValid}
                type="email"
                placeholder={enterEmail}
                onChange={this.onEmailChange}
              />
              {this.shouldValidate && !isSignInFailed && !isEmailValid && (
                <Form.Text
                  className={styles["common-form__input-feedback_error"]}
                >
                  {pleaseEnterValidEmail}
                </Form.Text>
              )}
              {isSignInFailed && (
                <Form.Text
                  className={styles["common-form__input-feedback_error"]}
                >
                  {invalidUsernameOrPassword}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group controlId="password">
              <Form.Label>{`${passwordMessage}${colon}`}</Form.Label>
              <Form.Control
                isValid={this.shouldValidate && isPasswordValid}
                isInvalid={!isPasswordValid}
                type="password"
                placeholder={enterPassword}
                onChange={this.onPasswordChange}
              />
              {this.shouldValidate && !isSignInFailed && !isPasswordValid && (
                <Form.Text
                  className={styles["common-form__input-feedback_error"]}
                >
                  {passwordRule}
                </Form.Text>
              )}
            </Form.Group>
            <div className={styles["sign-in-form__registration-link"]}>
              <Link to={Routes.SIGN_UP}>{registration}</Link>
            </div>
          </div>
        </div>
        <div className={styles["common-form__bottom-centered"]}>
          <Button
            variant="primary"
            onClick={() => this.signIn()}
            disabled={!isFormValid && !isSignInFailed}
          >
            {signIn}
          </Button>
        </div>
      </Form>
    );
  }
}

SignInComponent.propTypes = {
  signIn: PropTypes.func.isRequired,
  history: ReactRouterPropTypes.history.isRequired,
  messages: PropTypes.shape().isRequired,
  showLoadingIndication: PropTypes.func.isRequired,
  hideLoadingIndication: PropTypes.func.isRequired
};

export default localized(
  withLoadingIndication(withAds(withRouter(SignInComponent)))
);
