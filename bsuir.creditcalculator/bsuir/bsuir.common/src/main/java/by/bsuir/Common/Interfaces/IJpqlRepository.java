package by.bsuir.Common.Interfaces;

import java.util.List;

public interface IJpqlRepository<TEntity extends IEntity> extends IRepository<TEntity> {
    TEntity getSingleOrDefault(IJpqlSpecification<TEntity> specification);

    TEntity getFirstOrDefault(IJpqlSpecification<TEntity> specification);

    List<TEntity> getWhere(IJpqlSpecification<TEntity> specification);
}
