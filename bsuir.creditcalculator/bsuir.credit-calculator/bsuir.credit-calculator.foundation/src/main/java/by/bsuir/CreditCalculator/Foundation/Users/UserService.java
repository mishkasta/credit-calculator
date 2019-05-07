package by.bsuir.CreditCalculator.Foundation.Users;

import by.bsuir.Common.Interfaces.*;
import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.CreditCalculator.DomainModel.Role;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IUserService;
import by.bsuir.CreditCalculator.Foundation.Specifications.Role.RoleNameSpecification;
import by.bsuir.CreditCalculator.Foundation.Specifications.User.EmailSpecification;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditCalculatorUnitOfWorkFactory;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditCalulatorUnitOfWork;

import java.util.Date;
import java.util.List;

public class UserService implements IUserService {
    private final ICreditCalculatorUnitOfWorkFactory _unitOfWorkFactory;
    private final IEncoder _encoder;


    public UserService(ICreditCalculatorUnitOfWorkFactory unitOfWorkFactory, IEncoder encoder) {
        _unitOfWorkFactory = unitOfWorkFactory;
        _encoder = encoder;
    }


    @Override
    public OperationResult<CreateUserError, User> createUser(User user) throws Exception {
        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            IJpqlRepository<User> userRepository = unitOfWork.getRepository(User.class);
            User dbUser = userRepository.getSingleOrDefault(new EmailSpecification(user.getEmail()));
            if (dbUser != null) {
                return OperationResult.createUnsuccessful(CreateUserError.EMAIL_NOT_UNIQUE);
            }

            IJpqlRepository<Role> roleRepository = unitOfWork.getRepository(Role.class);
            IJpqlSpecification<Role> rolesSpecification = getRoleSpecification(user);
            List<Role> dbRoles = roleRepository.getWhere(rolesSpecification);
            if (dbRoles.size() != user.getRoles().size()) {
                return OperationResult.createUnsuccessful(CreateUserError.ROLE_NOT_FOUND);
            }
            user.setRoles(dbRoles);

            user.setCreateDate(new Date());
            String encodedPassword = _encoder.encode(user.getPassword(), user.getEmail());
            user.setPassword(encodedPassword);

            userRepository.add(user);
            unitOfWork.saveChanges();

            return OperationResult.createSuccessful();
        }
    }

    @Override
    public OperationResult<UpdateUserError, User> updateUser(User user) {
        return null;
    }

    @Override
    public OperationResult<DeleteUserError, User> deleteUser(User user) {
        return null;
    }

    @Override
    public OperationResult<GetUserError, User> getByEmail(String email) throws Exception {
        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            IJpqlRepository<User> userRepository = unitOfWork.getRepository(User.class);
            User user = userRepository.getSingleOrDefault(new EmailSpecification(email));
            if (user == null) {
                return OperationResult.createUnsuccessful(GetUserError.USER_NOT_FOUND);
            }

            return OperationResult.createSuccessful(user);
        }
    }

    @Override
    public List<User> getAll() throws Exception {
        try (ICreditCalulatorUnitOfWork unitOfWork = _unitOfWorkFactory.create()) {
            IRepository<User> userRepository = unitOfWork.getRepository(User.class);
            List<User> users = userRepository.getAll();

            return users;
        }
    }


    private IJpqlSpecification<Role> getRoleSpecification(User user) {
        IJpqlSpecification<Role> rolesSpecification = null;
        for (Role role : user.getRoles()) {
            if (rolesSpecification == null) {
                rolesSpecification = new RoleNameSpecification(role.getName());
            } else {
                rolesSpecification = rolesSpecification.or(new RoleNameSpecification(role.getName()));
            }
        }

        return rolesSpecification;
    }
}
