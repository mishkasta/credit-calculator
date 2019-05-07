import SortFields from './sortFields';
import SortOrders from '../../shared/sortOrders';

const Constants = {
    CREDITS_PER_PAGE: 8,
    DEFAULT_SORT_FIELD: SortFields.NAME,
    DEFAULT_SORT_ORDER: SortOrders.ASCENDING,
};

export default Object.freeze(Constants);
