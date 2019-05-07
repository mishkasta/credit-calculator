package by.bsuir.Common.TestBoot;

import by.bsuir.Common.Interfaces.IJpqlQueryBuilder;
import by.bsuir.Common.JpqlQueryBuilder.JoinType;
import by.bsuir.Common.JpqlQueryBuilder.JpqlQueryBuilder;
import by.bsuir.Common.SortOrder;
import by.bsuir.Common.Specifications.JpqlSpecifications.CompositeJpqlSpecification;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ALL")
public class TestBootstrapper {
    public static void main(String[] args) {
        IJpqlQueryBuilder<TestBootstrapper> builder1 = new JpqlQueryBuilder<>("User", "u");
        builder1.addJoin(JoinType.INNER, "chlen c");
        builder1.addJoin(JoinType.LEFT, "zhopa z");
        builder1.addJoin(JoinType.OUTER, "podborodok p");
        builder1.addWhere(new SomeSpecification());
        builder1.addOrderBy(SortOrder.ASCENDING, "p.razmer");
        builder1.addOrderBy(SortOrder.DESCENDING, "z.diameter");

        IJpqlQueryBuilder<TestBootstrapper> builder2 = new JpqlQueryBuilder<>("User", "u");
        builder2.addWhere(new SomeSpecification());
        builder2.addOrderBy(SortOrder.ASCENDING, "p.razmer");
        builder2.addOrderBy(SortOrder.DESCENDING, "z.diameter");

        IJpqlQueryBuilder<TestBootstrapper> builder3 = new JpqlQueryBuilder<>("User", "u");
        builder3.addJoin(JoinType.INNER, "chlen c");
        builder3.addJoin(JoinType.LEFT, "zhopa z");
        builder3.addJoin(JoinType.OUTER, "podborodok p");
        builder3.addOrderBy(SortOrder.ASCENDING, "p.razmer");
        builder3.addOrderBy(SortOrder.DESCENDING, "z.diameter");

        IJpqlQueryBuilder<TestBootstrapper> builder4 = new JpqlQueryBuilder<>("User", "u");
        builder4.addJoin(JoinType.INNER, "chlen c");
        builder4.addJoin(JoinType.LEFT, "zhopa z");
        builder4.addJoin(JoinType.OUTER, "podborodok p");
        builder2.addWhere(new SomeSpecification());

        IJpqlQueryBuilder<TestBootstrapper> builder5 = new JpqlQueryBuilder<>("User", "u");

        String query1 = builder1.build();
        String query2 = builder2.build();
        String query3 = builder3.build();
        String query4 = builder4.build();
        String query5 = builder5.build();

        System.out.printf("end");
    }



    private static class SomeSpecification extends CompositeJpqlSpecification<TestBootstrapper> {

        @Override
        public String getClause() {
            return "c.dlina";
        }
    }
}
