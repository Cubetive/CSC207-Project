package use_case.create_post;

import data_access.FilePostDataAccessObject;
import data_access.InMemorySessionRepository;
import entities.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.create_post.CreatePostController;
import interface_adapter.create_post.CreatePostPresenter;
import interface_adapter.create_post.CreatePostViewModel;
import interface_adapter.read_post.ReadPostController;
import interface_adapter.read_post.ReadPostPresenter;
import interface_adapter.read_post.ReadPostViewModel;
import interface_adapter.translate.TranslationViewModel;
import use_case.create_post_use_case.CreatePostInputBoundary;
import use_case.create_post_use_case.CreatePostInteractor;
import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.read_post.ReadPostInputBoundary;
import use_case.read_post.ReadPostInteractor;
import use_case.read_post.ReadPostOutputBoundary;
import use_case.session.SessionRepository;
import view.BrowsePostsView;
import view.CreatingPostView;
import view.PostReadingView;

import javax.swing.*;
import java.awt.*;

public class CreatePostTestBuilder {
    private final JPanel cardPanel = new JPanel();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private ReadPostViewModel readPostViewModel;
    private FilePostDataAccessObject postDataAccessObject;
    private PostReadingView postReadingView;
    private BrowsePostsViewModel browsePostsViewModel;
    private BrowsePostsView browsePostsView;
    private CreatePostViewModel createPostViewModel;
    private CreatingPostView creatingPostView;
    private final SessionRepository sessionRepository;

    public CreatePostTestBuilder() {
        this.cardPanel.setLayout(new CardLayout());
        this.postDataAccessObject = new ExampleDataBaseObject("exampleposts.json");
        this.sessionRepository = new InMemorySessionRepository();
        this.readPostViewModel = new ReadPostViewModel();
    }


    public CreatePostTestBuilder addReadPostUseCase() {
        final ReadPostOutputBoundary readPostOutputBoundary =
                new ReadPostPresenter(readPostViewModel);
        final ReadPostInputBoundary readPostInteractor =
                new ReadPostInteractor(postDataAccessObject, readPostOutputBoundary);

        final ReadPostController controller = new ReadPostController(readPostInteractor);
        postReadingView.setController(controller);

        // Set up back button to navigate back to browse posts
        postReadingView.setOnBackAction(() -> {
            viewManagerModel.setState(browsePostsView.getViewName());
            viewManagerModel.firePropertyChanged();
        });

        return this;
    }

    public CreatePostTestBuilder addCreatePostUseCase() {
        final CreatePostOutputBoundary createPostOutputBoundary =
                new CreatePostPresenter(createPostViewModel, viewManagerModel, readPostViewModel, browsePostsViewModel);
        final CreatePostInputBoundary createPostInteractor =
                new CreatePostInteractor(postDataAccessObject, createPostOutputBoundary, sessionRepository);
        sessionRepository.setCurrentUser(new User("Personable", "username", "bopbop@mail.com",
                "somethingsomething"));

        final CreatePostController controller = new CreatePostController(createPostInteractor);
        creatingPostView.setController(controller);
        createPostOutputBoundary.setPostReadingView(this.postReadingView);
        return this;
    }

    public CreatePostTestBuilder addReadPostView() {
        readPostViewModel = new ReadPostViewModel();
        TranslationViewModel translationViewModel = new TranslationViewModel();
        postReadingView = new PostReadingView(readPostViewModel, translationViewModel);
        cardPanel.add(postReadingView, postReadingView.getViewName());
        return this;
    }

    public CreatePostTestBuilder addCreatePostView() {
        this.createPostViewModel = new CreatePostViewModel();
        creatingPostView = new CreatingPostView(this.createPostViewModel);
        cardPanel.add(creatingPostView, creatingPostView.getViewName());
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Post Creation Tests");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);
        application.setSize(800, 600);
        application.setLocationRelativeTo(null);

        // Set initial view to creating post.
        viewManagerModel.setState(creatingPostView.getViewName());
        viewManagerModel.firePropertyChanged();



        return application;
    }
}
