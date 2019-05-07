import HttpUtility from '../shared/utilities/httpUtility';
import ApiRoutes from '../shared/ApiRoutes';
import { ContentTypeApplicationJson } from '../shared/utilities/HttpHeaders';

class CreditService {
    static getCreditsAsync = (
        pageNumber,
        creditsPerPage,
        filter,
        sortOrder,
        sortField,
    ) => new Promise((fulfill, reject) => {
        HttpUtility.get(
            `${ApiRoutes.CREDITS}?page=${pageNumber}&pageSize=${creditsPerPage}&sortField=${sortField}&sortOrder=${sortOrder}&filter=${filter}`,
            {},
            response => {
                fulfill({
                    credits: response.data.credits,
                    creditsCount: response.data.creditsCount,
                });
            },
            () => reject());
    });

    static saveCreditAsync = (credit, onSaveCreditSuccess, onSaveCreditFailure) => {
        HttpUtility.post(
            ApiRoutes.CREDITS,
            { ContentTypeApplicationJson },
            credit,
            () => onSaveCreditSuccess(),
            error => onSaveCreditFailure(error));
    };

    static calculateCredit = (desiredSum, monthsCount, interestRate, onCalculateSuccess) => {
        HttpUtility.get(`${ApiRoutes.CALCULATE_CREDIT}?desiredSum=${desiredSum}&monthsCount=${monthsCount}&interestRate=${interestRate}`,
            {},
            response => {
                const { data: calculatedCredit } = response;
                onCalculateSuccess(calculatedCredit);
            });
    };

    static updateCredit = (credit, oldName, fulfill, reject) => {
        const updateCreditDataContract = {
            name: credit.name,
            oldName,
            interestRate: credit.interestRate,
            totalSum: credit.totalSum,
            monthlyCharge: credit.monthlyCharge,
            monthsCount: credit.monthsCount,
            desiredSum: credit.desiredSum,
        };

        HttpUtility.put(
            ApiRoutes.CREDITS,
            { ContentTypeApplicationJson },
            updateCreditDataContract,
            () => fulfill(),
            error => reject(error));
    };

    static deleteCredit = (name, fulfill, reject) => {
        HttpUtility.delete(
            `${ApiRoutes.CREDITS}?name=${name}`,
            {},
            () => fulfill(),
            error => reject(error));
    };
}

export default CreditService;
