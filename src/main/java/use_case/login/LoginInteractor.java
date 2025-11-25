package use_case.login;

import entities.User;
import use_case.session.SessionRepository;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;
    private final SessionRepository sessionRepository;

    public LoginInteractor(LoginDataAccessInterface loginDataAccessInterface,
                          LoginOutputBoundary loginOutputBoundary,
                          SessionRepository sessionRepository) {
        this.userDataAccessObject = loginDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        // Validate that fields are non-empty
        if (username == null || username.trim().isEmpty()) {
            loginPresenter.prepareFailView("Username cannot be empty.");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            loginPresenter.prepareFailView("Password cannot be empty.");
            return;
        }

        // Find user by username only
        final User user = userDataAccessObject.getUserByUsername(username);

        // Check if user exists
        if (user == null) {
            loginPresenter.prepareFailView("User not found.");
            return;
        }

        // Verify password
        if (!user.getPassword().equals(password)) {
            loginPresenter.prepareFailView("Incorrect password.");
            return;
        }

        // Login successful
        sessionRepository.setCurrentUser(user);

        final LoginOutputData loginOutputData = new LoginOutputData(
            user.getUsername(),
            user.getFullName(),
            false
        );
        loginPresenter.prepareSuccessView(loginOutputData);
    }

    @Override
    public void switchToSignupView() {
        loginPresenter.switchToSignupView();
    }
}
