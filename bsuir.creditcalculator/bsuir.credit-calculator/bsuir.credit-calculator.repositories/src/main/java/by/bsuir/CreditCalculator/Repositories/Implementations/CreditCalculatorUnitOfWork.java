package by.bsuir.CreditCalculator.Repositories.Implementations;

import by.bsuir.Common.UnitOfWork.JpaUnitOfWork;
import by.bsuir.CreditCalculator.DomainModel.Credit;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Repositories.Implementations.Credit.CreditRepository;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditCalulatorUnitOfWork;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditRepository;
import by.bsuir.CreditCalculator.Repositories.Interfaces.IUserRepository;

import javax.persistence.EntityManager;

public class CreditCalculatorUnitOfWork extends JpaUnitOfWork implements ICreditCalulatorUnitOfWork {
    public CreditCalculatorUnitOfWork(EntityManager entityManager) {
        super(entityManager);
        registerCustomRepositoryType(User.class, UserRepository.class);
        registerCustomRepositoryType(Credit.class, CreditRepository.class);
    }


    @Override
    public IUserRepository getUserRepository() {
        return (IUserRepository) getRepository(User.class);
    }

    @Override
    public ICreditRepository getCreditRepository() {
        return (ICreditRepository) getRepository(Credit.class);
    }
}
