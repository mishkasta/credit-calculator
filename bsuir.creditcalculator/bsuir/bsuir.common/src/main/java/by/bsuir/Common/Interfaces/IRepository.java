package by.bsuir.Common.Interfaces;

import java.util.List;

public interface IRepository<TEntity extends IEntity> {
    TEntity getById(long Id);

    void add(TEntity entity);

    void update(TEntity entity);

    void delete(TEntity entity);

    List<TEntity> getAll();
}
