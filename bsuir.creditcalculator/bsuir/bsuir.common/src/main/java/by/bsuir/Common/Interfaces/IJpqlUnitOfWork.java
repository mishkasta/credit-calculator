package by.bsuir.Common.Interfaces;

public interface IJpqlUnitOfWork extends IUnitOfWork, AutoCloseable {
    <TEntity extends IEntity> IJpqlRepository<TEntity> getRepository(Class<TEntity> entityType);
}
