package by.bsuir.CreditCalculator.Foundation.Interfaces;

import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Foundation.Users.CreateUserError;
import by.bsuir.CreditCalculator.Foundation.Users.DeleteUserError;
import by.bsuir.CreditCalculator.Foundation.Users.GetUserError;
import by.bsuir.CreditCalculator.Foundation.Users.UpdateUserError;

import java.util.List;

public interface IUserService {
    OperationResult<CreateUserError, User> createUser(User user) throws Exception;

    OperationResult<UpdateUserError, User> updateUser(User user);

    OperationResult<DeleteUserError, User> deleteUser(User user);

    OperationResult<GetUserError, User> getByEmail(String email) throws Exception;

    List<User> getAll() throws Exception;
}
