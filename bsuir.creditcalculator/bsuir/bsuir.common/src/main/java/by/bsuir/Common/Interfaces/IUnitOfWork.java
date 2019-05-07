package by.bsuir.Common.Interfaces;

public interface IUnitOfWork {
    <TEntity extends IEntity> IRepository<TEntity> getRepository(Class<TEntity> type);

    void saveChanges();
}
