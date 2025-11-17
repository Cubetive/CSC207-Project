package entities;

/**
 * Factory for creating User objects.
 */
public interface UserFactory {

    /**
     * Creates a new User.
     * @param fullName the full name of the new user
     * @param username the username of the new user
     * @param email the email of the new user
     * @param password the password of the new user
     * @return the new user
     */
    User create(String fullName, String username, String email, String password);
}
