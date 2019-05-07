package by.bsuir.CreditCalculator.Web.Configuration;

import by.bsuir.Common.Interfaces.IEncoder;
import by.bsuir.Common.Interfaces.IErrorMessageProvider;
import by.bsuir.Common.Interfaces.IJsonSerializer;
import by.bsuir.Common.Interfaces.INumberConverter;
import by.bsuir.Common.JsonSerializer.JacksonSerializer;
import by.bsuir.Common.NumberConverter.NumberConverter;
import by.bsuir.Common.Security.PasswordEncoderHasherAdapter;
import by.bsuir.CreditCalculator.Foundation.Credits.CreditService;
import by.bsuir.CreditCalculator.Foundation.Interfaces.ICreditService;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IRefreshTokenService;
import by.bsuir.CreditCalculator.Foundation.Interfaces.IUserService;
import by.bsuir.CreditCalculator.Foundation.RefreshToken.RefreshTokenService;
import by.bsuir.CreditCalculator.Foundation.Users.UserService;
import by.bsuir.CreditCalculator.Repositories.Implementations.CreditCalculatorUnitOfWorkFactory;
import by.bsuir.CreditCalculator.Repositories.Interfaces.ICreditCalculatorUnitOfWorkFactory;
import by.bsuir.CreditCalculator.Web.Constants.ErrorMessageProvider;
import by.bsuir.CreditCalculator.Web.Interfaces.IUserValidator;
import by.bsuir.CreditCalculator.Web.Security.UserDetailsServiceImpl;
import by.bsuir.CreditCalculator.Web.Validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
public class SpringConfig {
    @Bean
    public EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(Constants.PERSISTENCE_NAME);
    }

    @Bean
    public INumberConverter getNumberConverter() {
        return new NumberConverter();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IJsonSerializer getJsonSerializer() {
        return new JacksonSerializer();
    }

    @Bean
    @Autowired
    public IEncoder getEncoder(PasswordEncoder passwordEncoder) {
        return new PasswordEncoderHasherAdapter(passwordEncoder);
    }

    @Bean
    @Autowired
    public ICreditCalculatorUnitOfWorkFactory getCreditCalculatorUnitOfWorkFactory(
            EntityManagerFactory entityManagerFactory) {
        return new CreditCalculatorUnitOfWorkFactory(entityManagerFactory);
    }

    @Bean
    @Autowired
    public IUserService getUserService(ICreditCalculatorUnitOfWorkFactory unitOfWorkFactory, IEncoder encoder) {
        return new UserService(unitOfWorkFactory, encoder);
    }

    @Bean
    @Autowired
    public ICreditService getCreditService(ICreditCalculatorUnitOfWorkFactory unitOfWorkFactory) {
        return new CreditService(unitOfWorkFactory);
    }

    @Bean
    @Autowired
    public UserDetailsService getUserDetailsService(IUserService userService) {
        return new UserDetailsServiceImpl(userService);
    }

    @Bean
    @Autowired
    public IRefreshTokenService getRefreshTokenService(
            IUserService userService,
            ICreditCalculatorUnitOfWorkFactory creditCalculatorUnitOfWorkFactory) {
        return new RefreshTokenService(userService, creditCalculatorUnitOfWorkFactory);
    }

    @Bean
    public IUserValidator getUserValidator() {
        return new UserValidator();
    }

    @Bean
    public IErrorMessageProvider getErrorMessageProvider() {
        return new ErrorMessageProvider();
    }
}
