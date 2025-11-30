package app;

import data_access.InMemorySessionRepository;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.reply_post.ReplyPostController;
import interface_adapter.reply_post.ReplyPostPresenter;
import use_case.logout.LogoutDataAccessInterface;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.reply_post.ReplyPostInputBoundary;
import use_case.reply_post.ReplyPostInteractor;
import use_case.reply_post.ReplyPostOutputBoundary;
import view.BrowsePostsView;
import view.EditProfileView;
import view.LoginView;
import view.PostReadingView;
import view.SignupView;
import view.ViewManager;
import data_access.FilePostDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.TranslationDataAccessObject;
import entities.CommonUserFactory;
import entities.UserFactory;
import use_case.session.SessionRepository;
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
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.edit_profile.EditProfileController;
import interface_adapter.edit_profile.EditProfileViewModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import interface_adapter.upvote_downvote.VoteController; // NEW
import interface_adapter.upvote_downvote.VotePresenter; // NEW
import interface_adapter.upvote_downvote.VoteViewModel; // NEW
import interface_adapter.translate.TranslationController; // NEW IMPORT
import interface_adapter.translate.TranslationPresenter; // NEW IMPORT
import interface_adapter.translate.TranslationViewModel; // NEW
import use_case.browse_posts.BrowsePostsInputBoundary;
import use_case.browse_posts.BrowsePostsInteractor;
import use_case.browse_posts.BrowsePostsOutputBoundary;
import use_case.read_post.ReadPostInputBoundary;
import use_case.read_post.ReadPostInteractor;
import use_case.read_post.ReadPostOutputBoundary;
import use_case.read_post.ReadPostDataAccessInterface; //NEW for setting up TranslationInteractor
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.upvote_downvote.VoteInputBoundary; // NEW
import use_case.upvote_downvote.VoteInteractor; // NEW
import use_case.upvote_downvote.VoteOutputBoundary; // NEW
import use_case.translate.TranslationInputBoundary; // NEW IMPORT
import use_case.translate.TranslationInteractor; // NEW IMPORT
import use_case.translate.TranslationOutputBoundary; // NEW IMPORT
import use_case.translate.TranslationDataAccessInterface; // NEW IMPORT

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

    final SessionRepository sessionRepository = new InMemorySessionRepository();

    // View models
    private SignupViewModel signupViewModel;
    private BrowsePostsViewModel browsePostsViewModel;
    private ReadPostViewModel readPostViewModel;
    private TranslationViewModel translationViewModel; // NEW
    private LoginViewModel loginViewModel;
    private EditProfileViewModel editProfileViewModel;
    private interface_adapter.create_post.CreatePostViewModel createPostViewModel;
    private interface_adapter.reference_post.ReferencePostViewModel referencePostViewModel;

    // Views
    private SignupView signupView;
    private LoginView loginView;
    private BrowsePostsView browsePostsView;
    private PostReadingView postReadingView;
    private EditProfileView editProfileView;
    private view.CreatingPostView creatingPostView;
    private view.ReferencePostView referencePostView;

    // Translation Controller (needed for post reading view)
    private TranslationController translationController; // NEW
    private interface_adapter.reference_post.ReferencePostController referencePostController;

    // For setting up TranslationInteractor
    private ReadPostDataAccessInterface readPostDataAccessInterface;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        // --- NEW (FIX): Initialize ViewModel in constructor to guarantee it's not null ---
        this.translationViewModel = new TranslationViewModel();
        this.loginViewModel = new LoginViewModel();

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
     * Adds the Edit Profile View to the application.
     * @return this builder
     */
    public AppBuilder addEditProfileView() {
        editProfileViewModel = new EditProfileViewModel();
        editProfileView = new EditProfileView(editProfileViewModel);
        cardPanel.add(editProfileView, editProfileView.getViewName());
        return this;
    }

    // NEW: for translation.
    /**
     * Adds the Translation Use Case to the application.
     * This is where the real TranslationDataAccessObject is instantiated and injected.
     * @return this builder
     */
    public AppBuilder addTranslationUseCase() {
        // --- CRITICAL DEPENDENCY INJECTION STEP ---
        // --- FIX: Instantiates the TranslationViewModel to prevent NullPointerException ---
        // 1. Instantiate the REAL Data Access Object (using the Generative Language API)

        // 2. Setup the Output Boundary (Presenter)
        final TranslationOutputBoundary translationOutputBoundary =
                new TranslationPresenter(translationViewModel, viewManagerModel);

        // 3. Setup the Interactor (Use Case) //FIX: changed from readPostDataAccessInterface to postDataAccessObject
        final TranslationInputBoundary translationInteractor =
                new TranslationInteractor(postDataAccessObject, this.translationDataAccessObject,
                        translationOutputBoundary);

        // 4. Create the Controller
        translationController = new TranslationController(translationInteractor);

        // The controller is stored and will be passed to PostReadingView in addReadPostView().
        // FIX: Inject the Controller into the PostReadingView
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
                userDataAccessObject, signupOutputBoundary, userFactory, sessionRepository);

        final SignupController controller = new SignupController(signupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    /**
     * Adds the Login View to the application.
     * @return this builder
     */
    public AppBuilder addLoginView() {
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
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
        if (loginView != null) {
            loginView.setLoginController(controller);
        }
        return this;
    }

    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(
                loginViewModel, viewManagerModel);
        final LogoutInputBoundary logoutInteractor = new LogoutInteractor(
                (LogoutDataAccessInterface) sessionRepository, logoutOutputBoundary);
        final LogoutController controller = new LogoutController(logoutInteractor);

        // Set Logout button in browse posts view
        browsePostsView.setOnLogoutAction(() -> {
            controller.execute();
            // Update the username field with the previous session username as placeholder
            loginViewModel.firePropertyChange();
        });

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
     * Adds the Reply Post Use Case to the application.
     * @return this builder
     */
    public AppBuilder addReplyPostUseCase() {
        final ReplyPostOutputBoundary replyPostOutputBoundary =
                new ReplyPostPresenter(readPostViewModel);
        final ReplyPostInputBoundary replyPostInteractor =
                new ReplyPostInteractor(postDataAccessObject, replyPostOutputBoundary, sessionRepository);

        final ReplyPostController controller = new ReplyPostController(replyPostInteractor);
        if (postReadingView != null) {
            postReadingView.setReplyController(controller);
        }
        return this;
    }

    /**
     * Adds the Edit Profile Use Case to the application.
     * @return this builder
     */
    public AppBuilder addEditProfileUseCase() {
        final EditProfileController controller = EditProfileUseCaseFactory.create(
                viewManagerModel,
                editProfileViewModel,
                userDataAccessObject,
                sessionRepository
        );
        
        if (editProfileView != null) {
            editProfileView.setEditProfileController(controller);
        }

        // Set up edit profile button click handler in browse posts view
        if (browsePostsView != null) {
            browsePostsView.setOnEditProfileClick(() -> {
                viewManagerModel.setState(editProfileView.getViewName());
                viewManagerModel.firePropertyChanged();
            });
        }

        // Set up cancel button to go back to browse posts
        if (editProfileView != null) {
            editProfileView.setOnCancelAction(() -> {
                viewManagerModel.setState(browsePostsView.getViewName());
                viewManagerModel.firePropertyChanged();
            });
        }

        return this;
    }
    
    /**
     * Adds the Reference Post View to the application.
     * @return this builder
     */
    public AppBuilder addReferencePostView() {
        referencePostViewModel = new interface_adapter.reference_post.ReferencePostViewModel();
        referencePostView = new view.ReferencePostView(referencePostViewModel);
        cardPanel.add(referencePostView, referencePostView.getViewName());
        return this;
    }
    
    /**
     * Adds the Reference Post Use Case to the application.
     * @return this builder
     */
    public AppBuilder addReferencePostUseCase() {
        // Setup presenter
        final use_case.reference_post.ReferencePostOutputBoundary referencePostOutputBoundary =
                new interface_adapter.reference_post.ReferencePostPresenter(
                        referencePostViewModel, viewManagerModel);
        
        // Setup interactor
        final use_case.reference_post.ReferencePostInputBoundary referencePostInteractor =
                new use_case.reference_post.ReferencePostInteractor(
                        postDataAccessObject, referencePostOutputBoundary);
        
        // Create controller
        referencePostController = new interface_adapter.reference_post.ReferencePostController(
                referencePostInteractor);
        
        // Set controller in view
        if (referencePostView != null) {
            referencePostView.setController(referencePostController);
        }
        
        // Set up navigation from reference post view back to create post view
        if (referencePostView != null && creatingPostView != null) {
            referencePostView.setOnCancelAction(() -> {
                viewManagerModel.setState(creatingPostView.getViewName());
                viewManagerModel.firePropertyChanged();
            });
            
            // When a post is selected, update the create post state and return
            referencePostView.setOnReferenceSelected(() -> {
                final use_case.reference_post.ReferencePostOutputData.PostSearchResult selected =
                        referencePostView.getSelectedReferencedPost();
                if (selected != null && createPostViewModel != null) {
                    final interface_adapter.create_post.CreatePostState state =
                            createPostViewModel.getState();
                    state.setReferencedPostId(selected.getPostId());
                    createPostViewModel.setState(state);
                    
                    // Update the display in CreatingPostView
                    creatingPostView.setReferencedPost(
                            selected.getTitle().isEmpty() ? "No Title" : selected.getTitle(),
                            selected.getContent()
                    );
                    
                    // Return to create post view
                    viewManagerModel.setState(creatingPostView.getViewName());
                    viewManagerModel.firePropertyChanged();
                }
            });
        }
        
        return this;
    }
    
    /**
     * Adds the Create Post View to the application.
     * @return this builder
     */
    public AppBuilder addCreatePostView() {
        createPostViewModel = new interface_adapter.create_post.CreatePostViewModel();
        creatingPostView = new view.CreatingPostView(createPostViewModel);
        cardPanel.add(creatingPostView, creatingPostView.getViewName());
        return this;
    }
    
    /**
     * Adds the Create Post Use Case to the application.
     * @return this builder
     */
    public AppBuilder addCreatePostUseCase() {
        // Setup presenter
        final use_case.create_post_use_case.CreatePostOutputBoundary createPostOutputBoundary =
                new interface_adapter.create_post.CreatePostPresenter(
                        createPostViewModel, viewManagerModel, readPostViewModel);
        
        // Setup interactor
        final use_case.create_post_use_case.CreatePostInputBoundary createPostInteractor =
                new use_case.create_post_use_case.CreatePostInteractor(
                        postDataAccessObject, createPostOutputBoundary);
        
        // Create controller
        final interface_adapter.create_post.CreatePostController createPostController =
                new interface_adapter.create_post.CreatePostController(createPostInteractor);
        
        // Set controller in view
        if (creatingPostView != null) {
            creatingPostView.setController(createPostController);
        }
        
        // Set up reference post button click handler
        if (creatingPostView != null && referencePostView != null) {
            creatingPostView.setOnReferencePostClick(() -> {
                // Clear any previous selection
                referencePostView.clearSelectedReferencedPost();
                // Navigate to reference post view
                viewManagerModel.setState(referencePostView.getViewName());
                viewManagerModel.firePropertyChanged();
            });
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

        // Set initial view
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}