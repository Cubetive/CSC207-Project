package use_case.create_post;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import data_access.InMemorySessionRepository;
import entities.User;
import use_case.create_post_use_case.CreatePostDataAccessInterface;
import use_case.create_post_use_case.CreatePostInputData;
import use_case.create_post_use_case.CreatePostInteractor;
import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.session.SessionRepository;

public class CreatePostInteractorTest {

    @Test
    public void testCreatePostSuccess() {
        final CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        final CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        final SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("blah", "blah", "blah", "blah"));
        final CreatePostInteractor createPostInteractor =
                new CreatePostInteractor(filePostAccess, presenter, repository);
        final CreatePostInputData input = new CreatePostInputData("new", "new");
        createPostInteractor.execute(input);
        assertTrue(createPostInteractor.isSuccess());
        createPostInteractor.resetSuccess();
        assertFalse(createPostInteractor.isSuccess());
    }

    @Test
    public void testCreatePostEmptyTitle() {
        final CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        final CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        final SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("blah", "blah", "blah", "blah"));
        final CreatePostInteractor createPostInteractor =
                new CreatePostInteractor(filePostAccess, presenter, repository);
        final CreatePostInputData input = new CreatePostInputData("", "new");
        createPostInteractor.execute(input);
        assertFalse(createPostInteractor.isSuccess());
    }

    @Test
    public void testCreatePostEmptyContent() {
        final CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        final CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        final SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("blah", "blah", "blah", "blah"));
        final CreatePostInteractor createPostInteractor =
                new CreatePostInteractor(filePostAccess, presenter, repository);
        final CreatePostInputData input = new CreatePostInputData("new", "");
        createPostInteractor.execute(input);
        assertFalse(createPostInteractor.isSuccess());
    }

}
