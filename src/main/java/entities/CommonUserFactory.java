package entities;

/**
 * Factory for creating User objects.
 */
public class CommonUserFactory implements UserFactory {

    @Override
    public User create(String fullName, String username, String email, String password) {
        return new User(fullName, username, email, password);
    }
}
