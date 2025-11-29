package app;

import interface_adapter.reply_post.ReplyPostController;
import interface_adapter.reply_post.ReplyPostPresenter;
import use_case.reply_post.ReplyPostInputBoundary;
import use_case.reply_post.ReplyPostInteractor;
import use_case.reply_post.ReplyPostOutputBoundary;
import view.BrowsePostsView;
import view.LoginView;
import view.PostReadingView;
import view.SignupView;
import view.ViewManager;
import data_access.FilePostDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.InMemorySessionRepository;
import entities.CommonUserFactory;
import entities.UserFactory;
import use_case.session.SessionRepository;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_posts.BrowsePostsController;
import interface_adapter.browse_posts.BrowsePostsPresenter;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.read_post.ReadPostController;
import interface_adapter.read_post.ReadPostPresenter;
import interface_adapter.read_post.ReadPostViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.upvote_downvote.VoteController; // NEW
import interface_adapter.upvote_downvote.VotePresenter; // NEW
import interface_adapter.upvote_downvote.VoteViewModel; // NEW
import use_case.browse_posts.BrowsePostsInputBoundary;
import use_case.browse_posts.BrowsePostsInteractor;
import use_case.browse_posts.BrowsePostsOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.read_post.ReadPostInputBoundary;
import use_case.read_post.ReadPostInteractor;
import use_case.read_post.ReadPostOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.upvote_downvote.VoteDataAccessInterface; // NEW
import use_case.upvote_downvote.VoteInputBoundary; // NEW
import use_case.upvote_downvote.VoteInteractor; // NEW
import use_case.upvote_downvote.VoteOutputBoundary; // NEW

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The AppBuilder class is responsible for putting together the pieces of
 * the application. It uses the builder pattern to construct the application.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new CommonUserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // Data access objects
    final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.csv");
    final FilePostDataAccessObject postDataAccessObject =
            new FilePostDataAccessObject("posts.json");
    final SessionRepository sessionRepository = new InMemorySessionRepository();

    // View models
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private BrowsePostsViewModel browsePostsViewModel;
    private ReadPostViewModel readPostViewModel;
    private VoteViewModel voteViewModel; // NEW: Vote ViewModel

    // Views
    private SignupView signupView;
    private LoginView loginView;
    private BrowsePostsView browsePostsView;
    private PostReadingView postReadingView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        // Add property change listener to load posts when browse posts view becomes active
        viewManagerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName())) {
                    final String viewName = (String) evt.getNewValue();

                    // 🔥 DEBUG LINE 1: See what view is actually being requested
                    System.out.println("APP BUILDER DEBUG: View switching to: [" + viewName + "]");

                    // Load posts when browse posts view becomes active
                    if ("browse posts".equals(viewName) && browsePostsView != null) {
                        browsePostsView.loadPosts();
                    }

                    if ("browse posts".equals(viewName) && browsePostsView != null) {
                        System.out.println("APP BUILDER DEBUG: Triggering loadPosts()..."); // 🔥 DEBUG LINE 2
                        browsePostsView.loadPosts();
                    }
                }
            }
        });
    }

    /**
     * Adds the Signup View to the application.
     * @return this builder
     */
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    /**
     * Adds the Login View to the application.
     * @return this builder
     */
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    /**
     * Adds the Browse Posts View to the application.
     * @return this builder
     */
    public AppBuilder addBrowsePostsView() {
        browsePostsViewModel = new BrowsePostsViewModel();
        browsePostsView = new BrowsePostsView(browsePostsViewModel);
        cardPanel.add(browsePostsView, browsePostsView.getViewName());
        return this;
    }

    /**
     * Adds the Read Post View to the application.
     * @return this builder
     */
    public AppBuilder addReadPostView() {
        readPostViewModel = new ReadPostViewModel();
        postReadingView = new PostReadingView(readPostViewModel);
        cardPanel.add(postReadingView, postReadingView.getViewName());
        return this;
    }

    /**
     * Adds the Signup Use Case to the application.
     * @return this builder
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(
                signupViewModel, viewManagerModel);
        final SignupInputBoundary signupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory, sessionRepository);

        final SignupController controller = new SignupController(signupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    /**
     * Adds the Login Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(
                loginViewModel, viewManagerModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary, sessionRepository);

        final LoginController controller = new LoginController(loginInteractor);
        loginView.setLoginController(controller);
        return this;
    }

    /**
     * Adds the Browse Posts Use Case to the application.
     * @return this builder
     */
    public AppBuilder addBrowsePostsUseCase() {
        final BrowsePostsOutputBoundary browsePostsOutputBoundary =
                new BrowsePostsPresenter(browsePostsViewModel);
        final BrowsePostsInputBoundary browsePostsInteractor =
                new BrowsePostsInteractor(postDataAccessObject, browsePostsOutputBoundary);

        final BrowsePostsController controller = new BrowsePostsController(browsePostsInteractor);
        browsePostsView.setController(controller);

        // Set up post click listener to navigate to read post view
        browsePostsView.setPostClickListener(postId -> {
            if (postReadingView != null) {
                viewManagerModel.setState(postReadingView.getViewName());
                viewManagerModel.firePropertyChanged();
                postReadingView.loadPost(postId);
            }
        });

        return this;
    }

    /**
     * Adds the Read Post Use Case to the application.
     * @return this builder
     */
    public AppBuilder addReadPostUseCase() {
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

    public AppBuilder addReplyPostUseCase() {
        final ReplyPostOutputBoundary replyPostOutputBoundary =
                new ReplyPostPresenter(readPostViewModel);
        final ReplyPostInputBoundary replyPostInteractor =
                new ReplyPostInteractor(postDataAccessObject, replyPostOutputBoundary, sessionRepository);

        final ReplyPostController replyController = new ReplyPostController(replyPostInteractor);
        postReadingView.setReplyController(replyController);

        return this;
    }

    /**
     * Gets the session repository for use cases that need to access session state.
     * @return the session repository
     */
    public SessionRepository getSessionRepository() {
        return sessionRepository;
    }

    /**
     * Adds the Vote Use Case (Upvote/Downvote) to the application.
     * @return this builder
     */
    public AppBuilder addVoteUseCase() {
        // 1. Create the Presenter (It updates the existing ReadPostViewModel)
        // We don't need a separate VoteViewModel because the result (new numbers)
        // is just an update to the post state.
        final VoteOutputBoundary voteOutputBoundary =
                new VotePresenter(readPostViewModel);

        // 2. Create the Interactor
        // Casting postDataAccessObject because it implements VoteDataAccessInterface
        final VoteInputBoundary voteInteractor = new VoteInteractor(
                postDataAccessObject,
                voteOutputBoundary
        );

        // 3. Create the Controller
        final VoteController voteController = new VoteController(voteInteractor);

        // 4. Inject the Controller into the View
        if (postReadingView != null) {
            postReadingView.setVoteController(voteController);
        }

        return this;
    }
    /**
     * Builds and returns the application JFrame.
     * @return the application JFrame
     */
    public JFrame build() {
        final JFrame application = new JFrame("Forum Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);
        application.setSize(800, 600);
        application.setLocationRelativeTo(null);

        // Set initial view to login
        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
