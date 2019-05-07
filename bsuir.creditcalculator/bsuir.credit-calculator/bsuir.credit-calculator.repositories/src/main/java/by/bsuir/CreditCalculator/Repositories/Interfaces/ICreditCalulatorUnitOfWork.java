package by.bsuir.CreditCalculator.Repositories.Interfaces;

import by.bsuir.Common.Interfaces.IJpqlUnitOfWork;

public interface ICreditCalulatorUnitOfWork extends IJpqlUnitOfWork {
    IUserRepository getUserRepository();

    ICreditRepository getCreditRepository();
}
