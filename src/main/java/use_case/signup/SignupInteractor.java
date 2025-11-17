package use_case.signup;

import entities.User;
import entities.UserFactory;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupDataAccessInterface signupDataAccessInterface,
                           SignupOutputBoundary signupOutputBoundary,
                           UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        // Validate that username doesn't already exist
        if (userDataAccessObject.existsByUsername(signupInputData.getUsername())) {
            userPresenter.prepareFailView("Username already exists.");
            return;
        }

        // Validate that email doesn't already exist
        if (userDataAccessObject.existsByEmail(signupInputData.getEmail())) {
            userPresenter.prepareFailView("Email already exists.");
            return;
        }

        // Validate that passwords match
        if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
            userPresenter.prepareFailView("Passwords don't match.");
            return;
        }

        // Validate password strength (at least 6 characters)
        if (signupInputData.getPassword().length() < 6) {
            userPresenter.prepareFailView("Password must be at least 6 characters long.");
            return;
        }

        // Validate email format
        if (!isValidEmail(signupInputData.getEmail())) {
            userPresenter.prepareFailView("Invalid email format.");
            return;
        }

        // Validate that all fields are non-empty
        if (signupInputData.getFullName().trim().isEmpty()) {
            userPresenter.prepareFailView("Full name cannot be empty.");
            return;
        }

        if (signupInputData.getUsername().trim().isEmpty()) {
            userPresenter.prepareFailView("Username cannot be empty.");
            return;
        }

        // Create and save the new user
        final User user = userFactory.create(
            signupInputData.getFullName(),
            signupInputData.getUsername(),
            signupInputData.getEmail(),
            signupInputData.getPassword()
        );
        userDataAccessObject.save(user);

        final SignupOutputData signupOutputData = new SignupOutputData(
            user.getUsername(),
            user.getFullName(),
            false
        );
        userPresenter.prepareSuccessView(signupOutputData);
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }

    /**
     * Validates email format using a simple regex pattern.
     * @param email the email to validate
     * @return true if email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
