import React from 'react';
import PropTypes from 'prop-types';
import { Form, Table } from 'react-bootstrap';
import Moment from 'react-moment';

import SortOrder from '../../shared/sortOrders';
import Constants from './constants';
import Paginator from '../../shared/pagination/Paginator';
import PagingComponent from '../pagingComponent/PagingComponent';
import NumberFormatter from '../../foundation/NumberFormatter';
import CreditService from '../../foundation/CreditService';
import { DeleteGlyph, EditGlyph, SortGlyph } from '../glyphs/Glyphs';
import SortFields from './sortFields';
import GlyphButton from '../glyphButtons/GlyphButton';
import DeleteCreditComponent from './deleteCredit/DeleteCreditComponent';
import UpdateCreditComponent from './updateCredit/UpdateCreditComponent';
import { withLoadingIndication } from '../../shared/loadingIndication/LoadingIndicationStore';
import { localized } from '../../shared/localization/LocalizationContext';

import styles from './MyCreditsComponent.scss';

class MyCreditsComponent extends React.Component {
    constructor(props) {
        super(props);

        this.paginator = new Paginator();
        this.paginator.itemsPerPage = Constants.CREDITS_PER_PAGE;

        this.state = {
            credits: [],
            creditsCount: 0,
            creditToUpdate: {},
            shouldShowEditCreditDialog: false,
            creditToDelete: {},
            shouldShowDeleteCreditDialog: false,
        };

        this.sortOrder = Constants.DEFAULT_SORT_ORDER;
        this.sortField = Constants.DEFAULT_SORT_FIELD;
        this.filter = '';
        this.isInitialRender = true;

        const { showLoadingIndication, hideLoadingIndication } = this.props;
        this.showLoading = showLoadingIndication;
        this.hideLoading = hideLoadingIndication;

        this.updateCredits();
    }

    onFilterChanged = event => {
        const { value } = event.target;

        this.filter = value;

        this.updateCredits();
    };

    showUpdateCreditDialog = creditToUpdate => {
        this.setState({
            creditToUpdate,
            shouldShowEditCreditDialog: true,
        });
    };

    hideUpdateCreditDialog = () => {
        this.setState({
            shouldShowEditCreditDialog: false,
        });
        setTimeout(this.updateCredits, 0);
    };

    showRemoveCreditDialog = creditToDelete => {
        this.setState({
            creditToDelete,
            shouldShowDeleteCreditDialog: true,
        });
    };

    hideRemoveCreditDialog = () => {
        this.setState({
            shouldShowDeleteCreditDialog: false,
        });
        setTimeout(this.updateCredits, 0);
    };

    updateCredits = () => {
        const { currentPage, itemsPerPage } = this.paginator;

        this.showLoading();

        CreditService.getCreditsAsync(
            currentPage,
            itemsPerPage,
            this.filter,
            this.sortOrder,
            this.sortField,
        ).then(getCreditsResult => {
            const { credits, creditsCount } = getCreditsResult;

            this.hideLoading();

            this.paginator.updateMaximalPage(creditsCount);

            this.isInitialRender = false;

            this.setState({
                credits,
                creditsCount,
            });
        });
    };

    toggleSort = sortField => {
        this.sortOrder = this.sortOrder === SortOrder.ASCENDING
            ? SortOrder.DESCENDING
            : SortOrder.ASCENDING;
        this.sortField = sortField;

        this.updateCredits();
    };

    pageLeft = () => {
        this.paginator.pageLeft();

        this.updateCredits();
    };

    pageRight = () => {
        this.paginator.pageRight();

        this.updateCredits();
    };

    pageStart = () => {
        this.paginator.pageStart();

        this.updateCredits();
    };

    pageEnd = () => {
        this.paginator.pageEnd();

        this.updateCredits();
    };

    render() {
        const {
            credits,
            creditsCount,
            shouldShowDeleteCreditDialog,
            shouldShowEditCreditDialog,
            creditToUpdate,
            creditToDelete,
        } = this.state;
        const { messages } = this.props;
        const {
            myCredits,
            search,
            creditName,
            createDate,
            desiredSum,
            monthsCount,
            interestRate,
            monthlyCharge,
            totalSum,
            actions,
            totalCreditsCount,
            colon,
            noCredits,
            loading,
        } = messages;
        const { currentPage, minimalPage, maximalPage } = this.paginator;
        const shouldShowNoCreditsMessage = creditsCount === 0;

        return (
            <div className={`${styles['common-form']} ${styles['my-credits-component']}`}>
                <DeleteCreditComponent
                    isShowing={shouldShowDeleteCreditDialog}
                    creditName={creditToDelete.name}
                    close={() => this.hideRemoveCreditDialog()}
                />
                <UpdateCreditComponent
                    creditName={creditToUpdate.name}
                    desiredSum={creditToUpdate.desiredSum}
                    monthsCount={creditToUpdate.monthsCount}
                    interestRate={creditToUpdate.interestRate}
                    isShowing={shouldShowEditCreditDialog}
                    onClose={() => this.hideUpdateCreditDialog()}
                />
                <div className={styles['my-credits-component__header']}>
                    <h2 className={styles['my-credits-component__caption']}>
                        {myCredits}
                    </h2>
                    <Form.Control
                        className={styles['my-credits-component__search-input']}
                        type="text"
                        placeholder={search}
                        onChange={this.onFilterChanged}
                    />
                </div>
                {shouldShowNoCreditsMessage ? (
                    <div className={styles['my-credits-component__no-credits-component']}>
                        {this.isInitialRender ? loading : noCredits}
                    </div>
): (
    <div>
        <div className={styles['my-credits-component__table-area']}>
            <Table
                className={styles['my-credits-component-table']}
                bordered
                hover
                size="sm"
            >
                <thead>
                    <tr>
                        <th className={styles['my-credits-component-table__name-column']}>
                            <div className={styles['my-credits-component-table__header']}>
                                <button
                                    type="button"
                                    className={styles['my-credits-component-table__toggle-sort-order-button']}
                                    onClick={() => this.toggleSort(SortFields.NAME)}
                                >
                                    <div>{creditName}</div>
                                    <SortGlyph />
                                </button>
                            </div>
                        </th>
                        <th className={styles['my-credits-component-table__create-date-column']}>
                            <div className={styles['my-credits-component-table__header']}>
                                <button
                                    type="button"
                                    className={styles['my-credits-component-table__toggle-sort-order-button']}
                                    onClick={() => this.toggleSort(SortFields.CREATE_DATE)}
                                >
                                    <div>{createDate}</div>
                                    <SortGlyph />
                                </button>
                            </div>
                        </th>
                        <th className={styles['my-credits-component-table__desired-sum-column']}>
                            <div className={styles['my-credits-component-table__header']}>
                                <button
                                    type="button"
                                    className={styles['my-credits-component-table__toggle-sort-order-button']}
                                    onClick={() => this.toggleSort(SortFields.DESIRED_SUM)}
                                >
                                    <div>{desiredSum}</div>
                                    <SortGlyph />
                                </button>
                            </div>
                        </th>
                        <th className={styles['my-credits-component-table__months-count-column']}>
                            <div className={styles['my-credits-component-table__header']}>
                                <button
                                    type="button"
                                    className={styles['my-credits-component-table__toggle-sort-order-button']}
                                    onClick={() => this.toggleSort(SortFields.MONTHS_COUNT)}
                                >
                                    <div>{monthsCount}</div>
                                    <SortGlyph />
                                </button>
                            </div>
                        </th>
                        <th className={styles['my-credits-component-table__interest-rate-column']}>
                            <div className={styles['my-credits-component-table__header']}>
                                <button
                                    type="button"
                                    className={styles['my-credits-component-table__toggle-sort-order-button']}
                                    onClick={() => this.toggleSort(SortFields.INTEREST_RATE)}
                                >
                                    <div>{interestRate}</div>
                                    <SortGlyph />
                                </button>
                            </div>
                        </th>
                        <th className={styles['my-credits-component-table__monthly-charge-column']}>
                            <div className={styles['my-credits-component-table__header']}>
                                <button
                                    type="button"
                                    className={styles['my-credits-component-table__toggle-sort-order-button']}
                                    onClick={() => this.toggleSort(SortFields.MONTHLY_CHARGE)}
                                >
                                    <div>{monthlyCharge}</div>
                                    <SortGlyph />
                                </button>
                            </div>
                        </th>
                        <th className={styles['my-credits-component-table__total-sum-column']}>
                            <div className={styles['my-credits-component-table__header']}>
                                <button
                                    type="button"
                                    className={styles['my-credits-component-table__toggle-sort-order-button']}
                                    onClick={() => this.toggleSort(SortFields.TOTAL_SUM)}
                                >
                                    <div>{totalSum}</div>
                                    <SortGlyph />
                                </button>
                            </div>
                        </th>
                        <th className={styles['my-credits-component-table__actions-column']}>
                            {actions}
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {credits.map((c) => (
                        <tr key={c.id}>
                            <td>{c.name}</td>
                            <td className={styles['my-credits-component-table__create-date-column']}>
                                <Moment local format="DD.MM.YYYY HH:mm">
                                    {c.createDate}
                                </Moment>
                            </td>
                            <td className={styles['my-credits-component-table__desired-sum-column']}>
                                {NumberFormatter.format(c.desiredSum)}
                            </td>
                            <td className={styles['my-credits-component-table__months-count-column']}>
                                {c.monthsCount}
                            </td>
                            <td className={styles['my-credits-component-table__interest-rate-column']}>
                                {c.interestRate}
                            </td>
                            <td className={styles['my-credits-component-table__monthly-charge-column']}>
                                {NumberFormatter.format(c.monthlyCharge)}
                            </td>
                            <td className={styles['my-credits-component-table__total-sum-column']}>
                                {NumberFormatter.format(c.totalSum)}
                            </td>
                            <td className={styles['my-credits-component-table__actions-column']}>
                                <GlyphButton onClick={() => this.showUpdateCreditDialog(c)}>
                                    <EditGlyph />
                                </GlyphButton>
                                <GlyphButton onClick={() => this.showRemoveCreditDialog(c)}>
                                    <DeleteGlyph />
                                </GlyphButton>
                            </td>
                        </tr>
                                ))}
                </tbody>
            </Table>
        </div>
        <div className={styles['my-credits-component__footer']}>
            <div>{`${totalCreditsCount}${colon} ${creditsCount}`}</div>
            <div>
                <PagingComponent
                    pageLeft={() => this.pageLeft()}
                    pageStart={() => this.pageStart()}
                    pageRight={() => this.pageRight()}
                    pageEnd={() => this.pageEnd()}
                    currentPage={currentPage}
                    minimalPage={minimalPage}
                    maximalPage={maximalPage}
                />
            </div>
        </div>
    </div>
)}
            </div>
        );
    }
}

MyCreditsComponent.propTypes = {
    messages: PropTypes.shape().isRequired,
    showLoadingIndication: PropTypes.func.isRequired,
    hideLoadingIndication: PropTypes.func.isRequired,
};

export default localized(
    withLoadingIndication(
        MyCreditsComponent,
    ),
);
