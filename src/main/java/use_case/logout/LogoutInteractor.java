package use_case.logout;

import entities.User;
import use_case.session.SessionRepository;

public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutDataAccessInterface logoutDataAccess;
    private final LogoutOutputBoundary logoutOutputBoundary;

    public LogoutInteractor(LogoutDataAccessInterface logoutDataAccess,
                            LogoutOutputBoundary logoutOutputBoundary) {
        this.logoutDataAccess = logoutDataAccess;
        this.logoutOutputBoundary = logoutOutputBoundary;
    }

    @Override
    public void execute() {
        // Logout use case can only be accessed when the user is already logged in.

        // Get username of the current user.
        final User currentUser = logoutDataAccess.getCurrentUser();
        final String username = currentUser.getUsername();

        // Clear session and prepare success view
        logoutDataAccess.clearSession();
        final LogoutOutputData outputData = new LogoutOutputData(username);
        logoutOutputBoundary.prepareSuccessView(outputData);
    }
}
