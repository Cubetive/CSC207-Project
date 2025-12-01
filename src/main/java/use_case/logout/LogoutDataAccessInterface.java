package use_case.logout;

import entities.User;

public interface LogoutDataAccessInterface {
    /**
     * Get the current user in the session.
     * @return the current user of the session
     */
    User getCurrentUser();

    /**
     * Clear the user of the current session.
     */
    void clearSession();
}
