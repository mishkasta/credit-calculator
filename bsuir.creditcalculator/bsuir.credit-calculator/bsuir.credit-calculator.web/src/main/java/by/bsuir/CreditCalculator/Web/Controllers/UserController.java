package by.bsuir.CreditCalculator.Web.Controllers;

import by.bsuir.Common.DataContracts.ErrorDataContract;
import by.bsuir.Common.Interfaces.IDataContract;
import by.bsuir.Common.Interfaces.IErrorMessageProvider;
import by.bsuir.Common.OperationResult.OperationResult;
import by.bsuir.Common.RestControllers.RestControllerBase;
import by.bsuir.CreditCalculator.DomainModel.Role;
import by.bsuir.CreditCalculator.DomainModel.User;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IUserService;
import by.bsuir.CreditCalculator.Foundation.Users.CreateUserError;
import by.bsuir.CreditCalculator.Web.Constants.Routes;
import by.bsuir.CreditCalculator.Web.Constants.User.UserValidationError;
import by.bsuir.CreditCalculator.Web.DataContracts.User.RegisterNewUserDataContract;
import by.bsuir.CreditCalculator.Web.Interfaces.IUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = Routes.USERS)
public class UserController extends RestControllerBase {
    private final IUserService _userService;
    private final IUserValidator _userValidator;


    @Autowired
    public UserController(
            IUserService userService,
            IUserValidator userValidator,
            IErrorMessageProvider errorMessageProvider) {
        super(errorMessageProvider);
        _userService = userService;
        _userValidator = userValidator;
    }


    @RequestMapping(value = Routes.USER_REGISTRATION, method = RequestMethod.POST)
    public ResponseEntity<IDataContract> registerNewUser(
            @RequestBody RegisterNewUserDataContract registerNewUserDataContract) throws Exception {
        OperationResult<UserValidationError, User> validationResult = _userValidator.validate(registerNewUserDataContract);
        if (!validationResult.isSuccessful()) {
            ErrorDataContract errorDataContract = createErrorDataContract(validationResult, UserValidationError.class);

            return new ResponseEntity<>(errorDataContract, HttpStatus.BAD_REQUEST);
        }

        User user = createFrom(registerNewUserDataContract);
        OperationResult<CreateUserError, User> createUserResult = _userService.createUser(user);
        if (!createUserResult.isSuccessful()) {
            ErrorDataContract errorDataContract = createErrorDataContract(createUserResult, CreateUserError.class);

            return new ResponseEntity<>(errorDataContract, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private static User createFrom(RegisterNewUserDataContract registerNewUserDataContract) {
        User user = new User();
        Role userRole = new Role();
        userRole.setName(Role.BuiltIn.USER);
        List<Role> roles = Collections.singletonList(userRole);
        user.setRoles(roles);
        user.setUsername(registerNewUserDataContract.getUsername());
        user.setEmail(registerNewUserDataContract.getEmail());
        user.setPassword(registerNewUserDataContract.getPassword());

        return user;
    }
}
