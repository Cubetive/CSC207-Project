package app;

import view.BrowsePostsView;
import view.PostReadingView;
import view.SignupView;
import view.ViewManager;
import data_access.FilePostDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.TranslationDataAccessObject;
import entities.CommonUserFactory;
import entities.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_posts.BrowsePostsController;
import interface_adapter.browse_posts.BrowsePostsPresenter;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.read_post.ReadPostController;
import interface_adapter.read_post.ReadPostPresenter;
import interface_adapter.read_post.ReadPostViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.translate.TranslationController;
import interface_adapter.translate.TranslationPresenter;
import interface_adapter.translate.TranslationViewModel;
import use_case.browse_posts.BrowsePostsInputBoundary;
import use_case.browse_posts.BrowsePostsInteractor;
import use_case.browse_posts.BrowsePostsOutputBoundary;
import use_case.read_post.ReadPostInputBoundary;
import use_case.read_post.ReadPostInteractor;
import use_case.read_post.ReadPostOutputBoundary;
import use_case.read_post.ReadPostDataAccessInterface;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.translate.TranslationInputBoundary;
import use_case.translate.TranslationInteractor;
import use_case.translate.TranslationOutputBoundary;

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
    final TranslationDataAccessObject translationDataAccessObject = new TranslationDataAccessObject();
    // View models
    private SignupViewModel signupViewModel;
    private BrowsePostsViewModel browsePostsViewModel;
    private ReadPostViewModel readPostViewModel;
    private TranslationViewModel translationViewModel; // NEW

    // Views
    private SignupView signupView;
    private BrowsePostsView browsePostsView;
    private PostReadingView postReadingView;

    // Translation Controller (needed for post reading view)
    private TranslationController translationController; // NEW

    // For setting up TranslationInteractor
    private ReadPostDataAccessInterface readPostDataAccessInterface;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        this.translationViewModel = new TranslationViewModel();

        viewManagerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName())) {
                    final String viewName = (String) evt.getNewValue();
                    // Load posts when browse posts view becomes active
                    if ("browse posts".equals(viewName) && browsePostsView != null) {
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
        postReadingView = new PostReadingView(readPostViewModel, translationViewModel); // NEW added new param
        cardPanel.add(postReadingView, postReadingView.getViewName());
        return this;
    }
    /**
     * Adds the Translation Use Case to the application.
     * This is where the real TranslationDataAccessObject is instantiated and injected.
     * @return this builder
     */
    public AppBuilder addTranslationUseCase() {
        final TranslationOutputBoundary translationOutputBoundary =
                new TranslationPresenter(translationViewModel, viewManagerModel);

        final TranslationInputBoundary translationInteractor =
                new TranslationInteractor(postDataAccessObject, this.translationDataAccessObject,
                        translationOutputBoundary);

        translationController = new TranslationController(translationInteractor);

        if (postReadingView != null) {
            postReadingView.setTranslationController(translationController);
        }
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
                userDataAccessObject, signupOutputBoundary, userFactory);

        final SignupController controller = new SignupController(signupInteractor);
        signupView.setSignupController(controller);
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

        // Set initial view
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
