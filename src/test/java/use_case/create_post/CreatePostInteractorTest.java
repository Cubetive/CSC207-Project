package use_case.create_post;

import data_access.FilePostDataAccessObject;
import data_access.InMemorySessionRepository;
import entities.User;
import interface_adapter.create_post.CreatePostPresenter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import use_case.create_post_use_case.CreatePostDataAccessInterface;
import use_case.create_post_use_case.CreatePostInputData;
import use_case.create_post_use_case.CreatePostInteractor;
import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.session.SessionRepository;

public class CreatePostInteractorTest {

    @Test
    public void testCreatePostSuccess() {
        CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("blah", "blah", "blah", "blah"));
        CreatePostInteractor createPostInteractor = new CreatePostInteractor(filePostAccess, presenter, repository);
        CreatePostInputData input = new CreatePostInputData("new", "new");
        createPostInteractor.execute(input);
        assertTrue(createPostInteractor.isSuccess());
        createPostInteractor.resetSuccess();
        assertFalse(createPostInteractor.isSuccess());
    }

    @Test
    public void testCreatePostEmptyTitle() {
        CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("blah", "blah", "blah", "blah"));
        CreatePostInteractor createPostInteractor = new CreatePostInteractor(filePostAccess, presenter, repository);
        CreatePostInputData input = new CreatePostInputData("", "new");
        createPostInteractor.execute(input);
        assertFalse(createPostInteractor.isSuccess());
    }

    @Test
    public void testCreatePostEmptyContent() {
        CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("blah", "blah", "blah", "blah"));
        CreatePostInteractor createPostInteractor = new CreatePostInteractor(filePostAccess, presenter, repository);
        CreatePostInputData input = new CreatePostInputData("new", "");
        createPostInteractor.execute(input);
        assertFalse(createPostInteractor.isSuccess());
    }

}
