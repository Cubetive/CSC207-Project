package use_case.create_post;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

import data_access.InMemorySessionRepository;
import entities.OriginalPost;
import entities.User;
import use_case.create_post_use_case.*;
import use_case.session.SessionRepository;

public class CreatePostInteractorTest {

    @Test
    public void createPostSuccessTest() {
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
    public void createPostEmptyTitleTest() {
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
    public void createPostEmptyContentTest() {
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

    // Also checks that Output data is stored correctly.
    @Test
    public void createPostBackTest() {
        final CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        final TestCreatePostPresenter presenter = new TestCreatePostPresenter();
        final SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("blah", "blah", "blah", "blah"));
        final CreatePostInteractor createPostInteractor =
                new CreatePostInteractor(filePostAccess, presenter, repository);
        createPostInteractor.switchToBrowseView();
        assertEquals("browse", presenter.isState());

        final OriginalPost post = new OriginalPost(999999999, "new", "new",
                "username", new Date(), 0, 0);
        final CreatePostOutputData createPostOutputData = new CreatePostOutputData(post);
        assertEquals(createPostOutputData.getOriginalPost().getTitle(), post.getTitle());
    }

    @Test
    public void createPostEmptyUsernameTest() {
        final CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        final CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        final SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("name", null, "blah", "blah"));
        final CreatePostInteractor createPostInteractor =
                new CreatePostInteractor(filePostAccess, presenter, repository);
        final CreatePostInputData input = new CreatePostInputData("new", "new", null);
        createPostInteractor.execute(input);
        assertFalse(createPostInteractor.isSuccess());
    }

    @Test
    public void createPostEmptyUserTest() {
        final CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        final CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        final SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(null);
        final CreatePostInteractor createPostInteractor =
                new CreatePostInteractor(filePostAccess, presenter, repository);
        final CreatePostInputData input = new CreatePostInputData("new", "new", null);
        createPostInteractor.execute(input);
        assertFalse(createPostInteractor.isSuccess());
    }

    @Test
    public void createPostEmptyReferenceIdTest() {
        final CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        final CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        final SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("name", "username", "blah", "blah"));
        final CreatePostInteractor createPostInteractor =
                new CreatePostInteractor(filePostAccess, presenter, repository);
        final CreatePostInputData input = new CreatePostInputData("new", "new", "");
        createPostInteractor.execute(input);
        assertTrue(createPostInteractor.isSuccess());
    }

    @Test
    public void createPostNonReferenceDatabaseTest() {
        final ExampleDatabaseObject2 filePostAccess = new ExampleDatabaseObject2("exampleposts.json");
        final CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        final SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("name", "username", "blah", "blah"));
        final CreatePostInteractor createPostInteractor =
                new CreatePostInteractor(filePostAccess, presenter, repository);
        final CreatePostInputData input = new CreatePostInputData("new", "new", "999");
        createPostInteractor.execute(input);
        assertTrue(createPostInteractor.isSuccess());
    }

    @Test
    public void createPostDatabaseReferenceFailTest() {
        final CreatePostDataAccessInterface filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        final CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        final SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("blah", "blah", "blah", "blah"));
        final CreatePostInteractor createPostInteractor =
                new CreatePostInteractor(filePostAccess, presenter, repository);
        final CreatePostInputData input = new CreatePostInputData("new", "new", "0");
        createPostInteractor.execute(input);
        assertFalse(createPostInteractor.isSuccess());
    }

    @Test
    public void createPostReferenceTest() {
        final ExampleDataBaseObject filePostAccess = new ExampleDataBaseObject("exampleposts.json");
        final CreatePostOutputBoundary presenter = new TestCreatePostPresenter();
        final SessionRepository repository = new InMemorySessionRepository();
        repository.setCurrentUser(new User("name", "username", "blah", "blah"));
        final CreatePostInteractor createPostInteractor =
                new CreatePostInteractor(filePostAccess, presenter, repository);
        final String id = "999";
        final CreatePostInputData input = new CreatePostInputData("new", "new", id);
        createPostInteractor.execute(input);
        assertTrue(createPostInteractor.isSuccess());
    }
}
