package by.bsuir.Common.JpqlQueryBuilder;

import by.bsuir.Common.SortOrder;

class OrderBy {
    private final String _field;
    private final SortOrder _sortOrder;


    public OrderBy(String field, SortOrder sortOrder) {
        _field = field;
        _sortOrder = sortOrder;
    }


    public String stringify() {
        String sortOrderString = getSortOrderString(_sortOrder);

        return String.format("%s %s", _field, sortOrderString);
    }


    private static String getSortOrderString(SortOrder sortOrder) {
        switch (sortOrder) {
            case ASCENDING:
                return "ASC";
            case DESCENDING:
                return "DESC";
            default:
                return "ASC";
        }
    }
}
