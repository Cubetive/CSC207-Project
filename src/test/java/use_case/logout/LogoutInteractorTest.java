package use_case.logout;

import data_access.InMemorySessionRepository;
import entities.CommonUserFactory;
import entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutInteractorTest {
    @Test
    void successLogoutTest() {
        InMemorySessionRepository sessionRepository = new InMemorySessionRepository();

        // Create "logged in" user for our post
        CommonUserFactory commonUserFactory = new CommonUserFactory();
        User user = commonUserFactory.create("Elysia", "elysia",
                "misspinkelf@gmail.com", "mlleelferose1111");
        sessionRepository.setCurrentUser(user);

        // Successful logout presenter
        LogoutOutputBoundary successPresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData outputData) {
                assertEquals("elysia", outputData.getUsername());
            }
        };

        LogoutInputBoundary interactor = new LogoutInteractor(sessionRepository, successPresenter);
        interactor.execute();
        assertNull(sessionRepository.getCurrentUser());
    }
}
