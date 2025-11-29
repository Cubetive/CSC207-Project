package data_access;

import entities.User;
import use_case.session.SessionRepository;

/**
 * In-memory implementation of SessionRepository.
 */
public class InMemorySessionRepository implements SessionRepository {
    private User currentUser;

    public InMemorySessionRepository() {
        this.currentUser = null;
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    @Override
    public void clearSession() {
        this.currentUser = null;
    }
}
