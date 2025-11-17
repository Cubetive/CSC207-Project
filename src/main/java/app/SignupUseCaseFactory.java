package app;

import data_access.InMemoryUserDataAccessObject;
import entities.CommonUserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;

/**
 * Factory for creating the Signup Use Case.
 */
public class SignupUseCaseFactory {

    /**
     * Prevents instantiation.
     */
    private SignupUseCaseFactory() {
    }

    /**
     * Creates a SignupController with all necessary dependencies.
     * @param viewManagerModel the view manager model
     * @param signupViewModel the signup view model
     * @param userDataAccessObject the data access object for users
     * @return a new SignupController
     */
    public static SignupController createSignupUseCase(
            ViewManagerModel viewManagerModel,
            SignupViewModel signupViewModel,
            InMemoryUserDataAccessObject userDataAccessObject) {

        // Create the output boundary (presenter)
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(
                signupViewModel, viewManagerModel);

        // Create the user factory
        final CommonUserFactory userFactory = new CommonUserFactory();

        // Create the input boundary (interactor)
        final SignupInputBoundary signupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        // Create and return the controller
        return new SignupController(signupInteractor);
    }
}
