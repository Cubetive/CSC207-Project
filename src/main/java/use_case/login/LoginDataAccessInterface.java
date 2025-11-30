package use_case.login;

import entities.User;

/**
 * DAO for the Login Use Case.
 */
public interface LoginDataAccessInterface {
    /**
     * Gets a user by username.
     * @param username the username to search for
     * @return the user with the given username, or null if not found
     */
    User getUserByUsername(String username);

    /**
     * Gets a user by email.
     * @param email the email to search for
     * @return the user with the given email, or null if not found
     */
    User getUserByEmail(String email);
}
