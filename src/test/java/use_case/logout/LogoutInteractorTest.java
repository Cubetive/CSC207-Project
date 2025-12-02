package use_case.logout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import data_access.InMemorySessionRepository;
import entities.CommonUserFactory;
import entities.User;

public class LogoutInteractorTest {
    @Test
    void successLogoutTest() {
        final InMemorySessionRepository sessionRepository = new InMemorySessionRepository();

        // Create "logged in" user for our post
        final CommonUserFactory commonUserFactory = new CommonUserFactory();
        final User user = commonUserFactory.create("Elysia", "elysia",
                "misspinkelf@gmail.com", "mlleelferose1111");
        sessionRepository.setCurrentUser(user);

        // Successful logout presenter
        final LogoutOutputBoundary successPresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData outputData) {
                assertEquals("elysia", outputData.getUsername());
            }
        };

        final LogoutInputBoundary interactor = new LogoutInteractor(sessionRepository, successPresenter);
        interactor.execute();
        assertNull(sessionRepository.getCurrentUser());
    }
}
