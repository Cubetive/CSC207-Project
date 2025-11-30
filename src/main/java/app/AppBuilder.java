package app;

import interface_adapter.create_post.CreatePostController;
import interface_adapter.create_post.CreatePostPresenter;
import interface_adapter.create_post.CreatePostViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.reply_post.ReplyPostController;
import interface_adapter.reply_post.ReplyPostPresenter;
import use_case.create_post_use_case.CreatePostInputBoundary;
import use_case.create_post_use_case.CreatePostInteractor;
import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.logout.LogoutDataAccessInterface;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.reply_post.ReplyPostInputBoundary;
import use_case.reply_post.ReplyPostInteractor;
import use_case.reply_post.ReplyPostOutputBoundary;
import view.*;
import view.BrowsePostsView;
import view.EditProfileView;
import view.LoginView;
import view.PostReadingView;
import view.SignupView;
import view.ViewManager;
import data_access.FilePostDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.InMemorySessionRepository;
import data_access.TranslationDataAccessObject;
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
import interface_adapter.upvote_downvote.VoteController;
import interface_adapter.upvote_downvote.VotePresenter;
import interface_adapter.upvote_downvote.VoteViewModel;
import interface_adapter.translate.TranslationController;
import interface_adapter.translate.TranslationPresenter;
import interface_adapter.translate.TranslationViewModel;
import interface_adapter.read_post.ReadPostController;
import interface_adapter.read_post.ReadPostPresenter;
import interface_adapter.read_post.ReadPostViewModel;
import interface_adapter.edit_profile.EditProfileController;
import interface_adapter.edit_profile.EditProfilePresenter;
import interface_adapter.edit_profile.EditProfileViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.browse_posts.BrowsePostsInputBoundary;
import use_case.browse_posts.BrowsePostsInteractor;
import use_case.browse_posts.BrowsePostsOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;

import use_case.read_post.ReadPostInputBoundary;
import use_case.read_post.ReadPostInteractor;
import use_case.read_post.ReadPostOutputBoundary;
import use_case.read_post.ReadPostDataAccessInterface;
import use_case.edit_profile.EditProfileInputBoundary;
import use_case.edit_profile.EditProfileInteractor;
import use_case.edit_profile.EditProfileOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.upvote_downvote.VoteInputBoundary;
import use_case.upvote_downvote.VoteInteractor;
import use_case.upvote_downvote.VoteOutputBoundary;
import use_case.translate.TranslationInputBoundary;
import use_case.translate.TranslationInteractor;
import use_case.translate.TranslationOutputBoundary;
import use_case.translate.TranslationDataAccessInterface;

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
    private EditProfileViewModel editProfileViewModel;
    private TranslationViewModel translationViewModel;
    private CreatePostViewModel createPostViewModel;


    // Views
    private SignupView signupView;
    private LoginView loginView;
    private BrowsePostsView browsePostsView;
    private PostReadingView postReadingView;
    private EditProfileView editProfileView;
    private CreatingPostView creatingPostView;

    // Translation Controller (needed for post reading view)
    private TranslationController translationController; // NEW
    private TranslationDataAccessObject translationDataAccessObject;

    // For setting up TranslationInteractor
    private ReadPostDataAccessInterface readPostDataAccessInterface;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        this.translationDataAccessObject = new TranslationDataAccessObject();
        this.translationViewModel = new TranslationViewModel();

        // Add property change listener to load posts when browse posts view becomes active
        // and load user data when edit profile view becomes active
        viewManagerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName())) {
                    final String viewName = (String) evt.getNewValue();
                    // Load posts when browse posts view becomes active
                    if ("browse posts".equals(viewName) && browsePostsView != null) {
                        browsePostsView.loadPosts();
                        // Update profile picture display
                        updateProfilePictureDisplay();
                    }
                    // Load user data when edit profile view becomes active
                    if ("edit profile".equals(viewName) && editProfileView != null && sessionRepository.isLoggedIn()) {
                        final entities.User currentUser = sessionRepository.getCurrentUser();
                        if (currentUser != null) {
                            editProfileView.loadUserData(
                                    currentUser.getUsername(),
                                    currentUser.getFullName(),
                                    currentUser.getBio(),
                                    currentUser.getProfilePicture()
                            );
                        }
                    }
                }
            }
        });
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
        createPostViewModel = new CreatePostViewModel();
        browsePostsView = new BrowsePostsView(browsePostsViewModel, createPostViewModel);
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

    //Execute after addBrowsePostsView
    public AppBuilder addCreatePostView() {
        creatingPostView = new CreatingPostView(this.createPostViewModel);
        cardPanel.add(creatingPostView, creatingPostView.getViewName());
        return this;
    }


    //Right now, needs to be called after the reading view has been added.
    public AppBuilder addCreatePostUseCase() {
        final CreatePostOutputBoundary createPostOutputBoundary =
                new CreatePostPresenter(createPostViewModel, viewManagerModel, readPostViewModel);
        final CreatePostInputBoundary createPostInteractor =
                new CreatePostInteractor(postDataAccessObject, createPostOutputBoundary);

        final CreatePostController controller = new CreatePostController(createPostInteractor);
        creatingPostView.setController(controller);
        createPostOutputBoundary.setPostReadingView(this.postReadingView);
        return this;
    }



    // NEW: for translation.
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
        //Necessary for creation use case
        browsePostsOutputBoundary.setCreatePostViewModel(createPostViewModel);
        browsePostsOutputBoundary.setViewManagerModel(viewManagerModel);
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

        // Set up edit profile button to navigate to edit profile view
        browsePostsView.setOnEditProfileClick(() -> {
            if (editProfileView != null && sessionRepository.isLoggedIn()) {
                viewManagerModel.setState(editProfileView.getViewName());
                viewManagerModel.firePropertyChanged();
            }
        });

        // Set up profile picture update callback to refresh when profile is updated
        browsePostsView.setOnProfilePictureUpdate(() -> {
            if (sessionRepository.isLoggedIn()) {
                final entities.User currentUser = sessionRepository.getCurrentUser();
                if (currentUser != null) {
                    browsePostsView.updateProfilePicture(currentUser.getProfilePicture());
                }
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
     * Adds the Edit Profile Use Case to the application.
     * @return this builder
     */
    public AppBuilder addEditProfileUseCase() {
        final EditProfileOutputBoundary editProfileOutputBoundary = new EditProfilePresenter(
                editProfileViewModel, viewManagerModel);
        final EditProfileInputBoundary editProfileInteractor = new EditProfileInteractor(
                userDataAccessObject, editProfileOutputBoundary, sessionRepository);

        final EditProfileController controller = new EditProfileController(editProfileInteractor);
        editProfileView.setEditProfileController(controller);

        // Set up cancel button to navigate back to browse posts
        editProfileView.setOnCancelAction(() -> {
            if (browsePostsView != null) {
                viewManagerModel.setState(browsePostsView.getViewName());
                viewManagerModel.firePropertyChanged();
            }
        });

        return this;
    }

    /**
     * Adds the Vote Use Case (Upvote/Downvote) to the application.
     * @return this builder
     */
    public AppBuilder addVoteUseCase() {
        final VoteOutputBoundary voteOutputBoundary =
                new VotePresenter(readPostViewModel);

        final VoteInputBoundary voteInteractor = new VoteInteractor(
                postDataAccessObject,
                voteOutputBoundary
        );

        final VoteController voteController = new VoteController(voteInteractor);

        if (postReadingView != null) {
            postReadingView.setVoteController(voteController);
        }

        return this;
    }

    /**
     * Updates the profile picture display in the browse posts view.
     */
    private void updateProfilePictureDisplay() {
        if (browsePostsView != null && sessionRepository.isLoggedIn()) {
            final entities.User currentUser = sessionRepository.getCurrentUser();
            if (currentUser != null) {
                browsePostsView.updateProfilePicture(currentUser.getProfilePicture());
            }
        }
    }

    /**
     * Gets the session repository for use cases that need to access session state.
     * @return the session repository
     */
    public SessionRepository getSessionRepository() {
        return sessionRepository;
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