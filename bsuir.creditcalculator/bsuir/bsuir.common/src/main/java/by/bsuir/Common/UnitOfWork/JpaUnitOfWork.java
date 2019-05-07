package by.bsuir.Common.UnitOfWork;

import by.bsuir.Common.Interfaces.IEntity;
import by.bsuir.Common.Interfaces.IJpqlRepository;
import by.bsuir.Common.Interfaces.IJpqlUnitOfWork;
import by.bsuir.Common.Interfaces.IRepository;
import by.bsuir.Common.Repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class JpaUnitOfWork implements IJpqlUnitOfWork {
    @PersistenceContext
    private final EntityManager _entityManager;

    private final Map<Class<? extends IEntity>, IJpqlRepository<?>> _repositories;
    private final Map<Class<? extends IEntity>, Class<? extends IRepository<?>>> _customRepositoryTypes;

    private final EntityTransaction _currentTransaction;


    public JpaUnitOfWork(EntityManager entityManager) {
        _entityManager = entityManager;

        _repositories = new HashMap<>();
        _customRepositoryTypes = new HashMap<>();

        _currentTransaction = entityManager.getTransaction();
        _currentTransaction.begin();
    }


    @SuppressWarnings("unchecked")
    @Override
    public <TEntity extends IEntity> IJpqlRepository<TEntity> getRepository(Class<TEntity> entityClass) {
        IJpqlRepository<TEntity> repository = (IJpqlRepository<TEntity>) _repositories.get(entityClass);
        if (repository != null) {
            return repository;
        }

        Class<? extends IRepository<?>> customRepositoryClass = _customRepositoryTypes.get(entityClass);
        if (customRepositoryClass == null) {
            repository = new JpaRepository<>(_entityManager, entityClass);
            _repositories.put(entityClass, repository);

            return repository;
        }

        try {
            Constructor<?> constructor = constructor = customRepositoryClass.getDeclaredConstructor(EntityManager.class);
            repository = (IJpqlRepository<TEntity>) constructor.newInstance(_entityManager);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        _repositories.put(entityClass, repository);

        return repository;
    }

    @Override
    public void saveChanges() {
        if (_currentTransaction.isActive()) {
            _currentTransaction.commit();
        }
    }


    protected <TEntity extends IEntity, TRepository extends IRepository<TEntity>> void registerCustomRepositoryType(
            Class<TEntity> entityClass,
            Class<TRepository> repositoryClass) {
        _customRepositoryTypes.put(entityClass, repositoryClass);
    }

    @Override
    public void close() {
        saveChanges();
        _entityManager.close();
    }
}
