package by.bsuir.Common.JpqlQueryBuilder;

import by.bsuir.Common.Interfaces.IJpqlQueryBuilder;
import by.bsuir.Common.Interfaces.IJpqlSpecification;
import by.bsuir.Common.SortOrder;

import java.util.ArrayList;
import java.util.List;

public class JpqlQueryBuilder<TEntity> implements IJpqlQueryBuilder<TEntity> {
    private static final String QUERY_PATTERN = "SELECT <select> FROM <entityType> <entityName> <joins> <where> <orderBy>";
    private static final String SELECT_PLACEHOLDER = "<select>";
    private static final String ENTITY_NAME_PLACEHOLDER = "<entityName>";
    private static final String ENTITY_TYPE_PLACEHOLDER = "<entityType>";
    private static final String JOINS_PLACEHOLDER = "<joins>";
    private static final String WHERE_PLACEHOLDER = "<where>";
    private static final String ORDER_BY_PLACEHOLDER = "<orderBy>";

    private final String _entityType;
    private final String _entityName;
    private boolean _isCountQuery;
    private IJpqlSpecification<TEntity> _specification;
    private List<Join> _joins;
    private List<OrderBy> _orderByClauses;


    public JpqlQueryBuilder(String entityType, String entityName) {
        _entityType = entityType;
        _entityName = entityName;

        _joins = new ArrayList<>();
        _orderByClauses = new ArrayList<>();
    }


    @Override
    public void count() {
        _isCountQuery = true;
    }

    @Override
    public void addJoin(JoinType joinType, String field) {
        String joinString = getField(field);
        Join join = new Join(joinType, joinString);
        _joins.add(join);
    }

    @Override
    public void addWhere(IJpqlSpecification<TEntity> specification) {
        _specification = specification;
    }

    @Override
    public void addOrderBy(SortOrder sortOrder, String field) {
        OrderBy orderBy = new OrderBy(field, sortOrder);
        _orderByClauses.add(orderBy);
    }

    @Override
    public String build() {
        String query = QUERY_PATTERN.replaceAll(ENTITY_TYPE_PLACEHOLDER, _entityType).replaceAll(ENTITY_NAME_PLACEHOLDER, _entityName);
        query = buildSelect(query);
        query = buildJoins(query);
        query = buildWhere(query);
        query = buildOrderBy(query);

        return query.trim();
    }


    private String buildSelect(String query) {
        String select = _isCountQuery ? String.format("COUNT(%s)", _entityName) : _entityName;

        return query.replaceAll(SELECT_PLACEHOLDER, select);
    }

    private String buildJoins(String query) {
        String joins = getJoins();
        if (joins != null) {
            query = query.replaceAll(JOINS_PLACEHOLDER, joins);
        } else {
            query = query.replaceAll(JOINS_PLACEHOLDER, "");
        }

        return query;
    }

    private String buildWhere(String query) {
        String where = getWhere();
        if (where != null) {
            query = query.replaceAll(WHERE_PLACEHOLDER, where);
        } else {
            query = query.replaceAll(WHERE_PLACEHOLDER, "");
        }

        return query;
    }

    private String buildOrderBy(String query) {
        String orderBy = getOrderBy();
        if (orderBy != null) {
            query = query.replaceAll(ORDER_BY_PLACEHOLDER, orderBy);
        } else {
            query = query.replaceAll(ORDER_BY_PLACEHOLDER, "");
        }

        return query;
    }

    private String getField(String field) {
        return String.format("%s.%s", _entityName, field);
    }

    private String getJoins() {
        String joins = _joins.stream()
                .map(Join::stringify)
                .reduce((previous, next) -> String.format("%s %s", previous, next)).orElse(null);

        return joins;
    }

    private String getWhere() {
        if (_specification == null) {
            return null;
        }

        String clause = _specification.getClause();

        return String.format("WHERE %s", clause);
    }

    private String getOrderBy() {
        String orderBy = _orderByClauses.stream()
                .map(OrderBy::stringify)
                .reduce((previous, next) -> String.format("%s, %s", previous, next)).orElse(null);

        return orderBy != null ? String.format("ORDER BY %s", orderBy) : null;
    }
}
