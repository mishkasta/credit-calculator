import React from 'react';
import PropTypes from 'prop-types';
import { Form, Button } from 'react-bootstrap';

import PopupWrapper from '../../popupWrapper/PopupWrapper';
import { localized } from '../../../shared/localization/LocalizationContext';
import { withLoadingIndication } from '../../../shared/loadingIndication/LoadingIndicationStore';
import CreditService from '../../../foundation/CreditService';

import styles from './DeleteCreditComponent.scss';

class DeleteCreditComponent extends React.Component {
    constructor(props) {
        super(props);

        const { showLoadingIndication, hideLoadingIndication } = this.props;
        this.showLoading = showLoadingIndication;
        this.hideLoading = hideLoadingIndication;
    }

    deleteCredit = () => {
        const { creditName } = this.props;

        this.showLoading();
        CreditService.deleteCredit(creditName, this.onDeleteCreditSuccess, this.onDeleteCreditFailed);
    };

    onDeleteCreditSuccess = () => {
        const { close } = this.props;
        this.hideLoading();
        close();
    };

    onDeleteCreditFailed = () => {
        this.hideLoading();
    };

    render() {
        const {
            close,
            isShowing,
            messages
        } = this.props;
        const {
            confirmTheAction,
            areYouSureYouWantToDeleteThisCredit,
            deleteMessage,
            cancel
        } = messages;

        return (
            <PopupWrapper isVisible={isShowing} onClose={() => close()}>
                <Form className={styles['common-form']}>
                    <h3 className={styles['common-form__caption']}>
                        {confirmTheAction}
                    </h3>
                    <Form.Group className={styles['common-form__content']}>
                        {areYouSureYouWantToDeleteThisCredit}
                    </Form.Group>
                    <div className={styles['common-form__bottom-right']}>
                        <Button variant="primary" onClick={() => this.deleteCredit()}>
                            {deleteMessage}
                        </Button>
                        <div className={styles['common-form__button-divider']} />
                        <Button variant="secondary" onClick={() => close()}>
                            {cancel}
                        </Button>
                    </div>
                </Form>
            </PopupWrapper>
        );
    }
}

DeleteCreditComponent.defaultProps = {
    creditName: ''
};

DeleteCreditComponent.propTypes = {
    isShowing: PropTypes.bool.isRequired,
    close: PropTypes.func.isRequired,
    creditName: PropTypes.string,
    messages: PropTypes.shape().isRequired,
    showLoadingIndication: PropTypes.func.isRequired,
    hideLoadingIndication: PropTypes.func.isRequired,
};

export default localized(
    withLoadingIndication(
        DeleteCreditComponent
    )
);
