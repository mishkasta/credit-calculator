package by.bsuir.CreditCalculator.Repositories.Implementations;

import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditCalculatorUnitOfWorkFactory;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditCalulatorUnitOfWork;

import javax.persistence.EntityManagerFactory;

public class CreditCalculatorUnitOfWorkFactory implements ICreditCalculatorUnitOfWorkFactory {
    private final EntityManagerFactory _entityManagerFactory;


    public CreditCalculatorUnitOfWorkFactory(EntityManagerFactory entityManagerFactory) {
        _entityManagerFactory = entityManagerFactory;
    }


    @Override
    public ICreditCalulatorUnitOfWork create() {
        return new CreditCalculatorUnitOfWork(_entityManagerFactory.createEntityManager());
    }
}
