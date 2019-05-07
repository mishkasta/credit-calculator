package by.bsuir.Common.Interfaces;

import by.bsuir.Common.JpqlQueryBuilder.JoinType;
import by.bsuir.Common.SortOrder;

public interface IJpqlQueryBuilder<TEntity> {
    void count();

    void addJoin(JoinType joinType, String join);

    void addWhere(IJpqlSpecification<TEntity> specification);

    void addOrderBy(SortOrder sortOrder, String field);

    String build();
}
