package by.bsuir.Common.Repository;

import by.bsuir.Common.Interfaces.*;
import by.bsuir.Common.Specifications.JpqlSpecifications.CompositeJpqlSpecification;
import by.bsuir.Common.Specifications.JpqlSpecifications.TrueJpqlSpecification;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class JpaRepository<TEntity extends IEntity> implements IJpqlRepository<TEntity>, IRepository<TEntity> {
    private static final String QUERY_PATTERN = "SELECT e FROM %s e";

    private final EntityManager _entityManager;

    private final String _query;


    public JpaRepository(EntityManager entityManager, Class<TEntity> entityType) {
        _entityManager = entityManager;

        _query = String.format(QUERY_PATTERN, entityType.getName());
    }


    @Override
    public TEntity getById(long id) {
        Query query = getQuery(new IdSpecification(id));
        TEntity entity = (TEntity) query.getSingleResult();

        return entity;
    }

    @Override
    public void add(TEntity entity) {
        _entityManager.persist(entity);
    }

    @Override
    public void update(TEntity entity) {
        _entityManager.merge(entity);
    }

    @Override
    public void delete(TEntity entity) {
        _entityManager.remove(entity);
    }

    @Override
    public List<TEntity> getAll() {
        Query query = getQuery(new TrueJpqlSpecification<>());
        List<TEntity> entities = query.getResultList();

        return entities;
    }

    @Override
    public TEntity getSingleOrDefault(IJpqlSpecification<TEntity> specification) {
        Query query = getQuery(specification);
        try {
            TEntity entity = (TEntity) query.getSingleResult();

            return entity;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public TEntity getFirstOrDefault(IJpqlSpecification<TEntity> specification) {
        List<TEntity> entities = getWhere(specification);
        try {
            TEntity entity = entities.get(0);

            return entity;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<TEntity> getWhere(IJpqlSpecification<TEntity> specification) {
        Query query = getQuery(specification);
        List<TEntity> entities = query.getResultList();

        return entities;
    }


    protected Query getQuery(IJpqlSpecification<TEntity> specification) {
        String queryString = String.format("%s %s", _query, getWhereClause(specification));

        return getQuery(queryString);
    }

    protected Query getQuery(String queryString) {
        return _entityManager.createQuery(queryString);
    }

    protected Query getJoinedQuery(IJpqlSpecification<TEntity> specification, String... joins) {
        String joinsString = getJoins(joins);
        String whereClause = getWhereClause(specification);
        String queryString = String.format("%s %s %s", _query, joinsString, whereClause);

        return getQuery(queryString);
    }


    private String getWhereClause(IJpqlSpecification<TEntity> specification) {
        return String.format("WHERE %s", specification.getClause());
    }

    private String getJoins(String... joins) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String join : joins) {
            stringBuilder.append(String.format("JOIN %s ", join));
        }

        return stringBuilder.toString();
    }



    private class IdSpecification extends CompositeJpqlSpecification<TEntity> {
        private long _id;


        public IdSpecification(long id) {
            _id = id;
        }


        @Override
        public String getClause() {
            return String.format("id = %s", _id);
        }
    }
}
