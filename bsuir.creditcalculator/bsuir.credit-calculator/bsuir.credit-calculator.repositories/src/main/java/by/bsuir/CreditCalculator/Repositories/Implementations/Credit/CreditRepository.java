package by.bsuir.CreditCalculator.Repositories.Implementations.Credit;

import by.bsuir.Common.Interfaces.IJpqlQueryBuilder;
import by.bsuir.Common.Interfaces.IJpqlRepository;
import by.bsuir.Common.Interfaces.IJpqlSpecification;
import by.bsuir.Common.JpqlQueryBuilder.JoinType;
import by.bsuir.Common.JpqlQueryBuilder.JpqlQueryBuilder;
import by.bsuir.Common.Repository.JpaRepository;
import by.bsuir.Common.SortOrder;
import by.bsuir.Common.Specifications.JpqlSpecifications.CompositeJpqlSpecification;
import by.bsuir.CreditCalculator.DomainModel.Credit;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class CreditRepository extends JpaRepository<Credit> implements ICreditRepository, IJpqlRepository<Credit> {
    private static final String USER_JOIN = "e.user u";


    public CreditRepository(EntityManager entityManager) {
        super(entityManager, Credit.class);
    }


    @Override
    public Credit getSingleOrDefaultFor(IJpqlSpecification<Credit> specification, String ownerEmail) {
        Query query = getJoinedQuery(specification.and(new UserEmailSpecification(ownerEmail)),USER_JOIN);
        try {
            List<Credit> credits = query.getResultList();

            return credits.get(0);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Credit> getCredits(
            String ownerEmail,
            int skippedCount,
            int count,
            SortOrder sortOrder,
            SortField sortField,
            String filter) {
        IJpqlSpecification<Credit> specification = new NameFilterSpecification(filter).and(new UserEmailSpecification(ownerEmail));
        CreditQueryBuilder builder = new CreditQueryBuilder();
        builder.joinUser();
        builder.addWhere(specification);
        builder.addOrderBy(sortOrder, sortField);

        String queryString = builder.build();
        Query query = getQuery(queryString);

        List<Credit> credits = query.setFirstResult(skippedCount).setMaxResults(count).getResultList();

        return credits;
    }

    @Override
    public long countCredits(String ownerEmail, String filter) {
        IJpqlSpecification<Credit> specification = new NameFilterSpecification(filter).and(new UserEmailSpecification(ownerEmail));
        CreditQueryBuilder builder = new CreditQueryBuilder();
        builder.count();
        builder.joinUser();
        builder.addWhere(specification);

        String queryString = builder.build();
        Query query = getQuery(queryString);

        long creditsCount = (long) query.getSingleResult();

        return creditsCount;
    }


    private class CreditQueryBuilder extends JpqlQueryBuilder<Credit> {
        CreditQueryBuilder() {
            super(Credit.class.getName(), "c");
        }

        void joinUser() {
            addJoin(JoinType.INNER, "user u");
        }

        void addOrderBy(SortOrder sortOrder, SortField sortField) {
            String sortFieldString = getSortField(sortField);
            addOrderBy(sortOrder, sortFieldString);
        }


        private String getSortField(SortField sortField) {
            switch (sortField) {
                case MONTHLY_CHARGE:
                    return "c.monthlyCharge";
                case INTEREST_RATE:
                    return "c.interestRate";
                case DESIRED_SUM:
                    return "c.desiredSum";
                case TOTAL_SUM:
                    return "c.totalSum";
                case CREATED_DATE:
                    return "c.createdDate";
                case MONTHS_COUNT:
                    return "c.monthsCount";
                case NAME:
                default:
                    return "c.name";
            }
        }
    }



    private class UserEmailSpecification extends CompositeJpqlSpecification<Credit> {
        private String _email;


        UserEmailSpecification(String email) {
            _email = email;
        }


        @Override
        public String getClause() {
            return String.format("u.email = '%s'", _email);
        }
    }



    private class NameFilterSpecification extends CompositeJpqlSpecification<Credit> {
        private final String _filter;


        public NameFilterSpecification(String filter) {
            _filter = filter;
        }


        @Override
        public String getClause() {
            return String.format("c.name LIKE '%%%s%%'", _filter);
        }
    }
}
