package by.bsuir.CreditCalculator.Repositories.Implementations;

import by.bsuir.Common.Interfaces.IJpqlRepository;
import by.bsuir.Common.Repository.JpaRepository;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Repositories.Interfaces.IUserRepository;

import javax.persistence.EntityManager;

public class UserRepository extends JpaRepository<User> implements IUserRepository, IJpqlRepository<User> {
    public UserRepository(EntityManager entityManager) {
        super(entityManager, User.class);
    }
}
