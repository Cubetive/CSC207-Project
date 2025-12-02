package use_case.logout;

import entities.User;

/**
 * Data access interface for logout operations.
 */
public interface LogoutDataAccessInterface {
    /**
     * Gets the current user in the session.
     *
     * @return the current user of the session
     */
    User getCurrentUser();

    /**
     * Clears the user of the current session.
     */
    void clearSession();
}
