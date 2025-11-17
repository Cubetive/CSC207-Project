package app;

import View.BrowsePostsView;
import View.SignupView;
import data_access.FilePostDataAccessObject;
import data_access.FileUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_posts.BrowsePostsController;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The Main class demonstrates how to use the Signup and Browse Posts functionality.
 */
public class Main {

    public static void main(String[] args) {
        // Create the application frame
        final JFrame application = new JFrame("Forum Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create the card layout for switching views
        final CardLayout cardLayout = new CardLayout();
        final JPanel views = new JPanel(cardLayout);

        // Create the data access objects
        final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv");
        final FilePostDataAccessObject postDataAccessObject = new FilePostDataAccessObject("posts.json");

        // Create the view models
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final BrowsePostsViewModel browsePostsViewModel = new BrowsePostsViewModel();

        // Create the signup view and controller
        final SignupView signupView = new SignupView(signupViewModel, viewManagerModel);
        final SignupController signupController = SignupUseCaseFactory.createSignupUseCase(
                viewManagerModel, signupViewModel, userDataAccessObject);
        signupView.setSignupController(signupController);

        // Create the browse posts view and controller
        final BrowsePostsView browsePostsView = new BrowsePostsView(browsePostsViewModel);
        final BrowsePostsController browsePostsController = BrowsePostsUseCaseFactory.createBrowsePostsUseCase(
                browsePostsViewModel, postDataAccessObject);
        browsePostsView.setController(browsePostsController);

        // Add views to the card layout
        views.add(signupView, signupView.getViewName());
        views.add(browsePostsView, browsePostsView.getViewName());

        // Create the view manager
        new ViewManager(views, cardLayout, viewManagerModel);

        // Add listener to load posts when browse posts view becomes active
        viewManagerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName()) && "browse posts".equals(evt.getNewValue())) {
                    browsePostsView.loadPosts();
                }
            }
        });

        // Set up the application
        application.add(views);
        application.pack();
        application.setSize(800, 600);
        application.setLocationRelativeTo(null);
        application.setVisible(true);

        // Show the initial view (signup)
        cardLayout.show(views, "sign up");
    }
}
