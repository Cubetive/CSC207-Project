package use_case.session;

import entities.User;

/**
 * Interface for managing user session state.
 */
public interface SessionRepository {

    /**
     * Sets the currently logged-in user.
     * @param user the user to set as logged in
     */
    void setCurrentUser(User user);

    /**
     * Gets the currently logged-in user.
     * @return the current user, or null if no user is logged in
     */
    User getCurrentUser();

    /**
     * Checks if a user is currently logged in.
     * @return true if a user is logged in, false otherwise
     */
    boolean isLoggedIn();

    /**
     * Logs out the current user by clearing the session.
     */
    void clearSession();
}
