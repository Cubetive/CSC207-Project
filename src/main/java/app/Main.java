package app;

import View.SignupView;
import data_access.FileUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;

/**
 * The Main class demonstrates how to use the Signup functionality.
 * This is a simple console-based demonstration.
 */
public class Main {

    public static void main(String[] args) {
        // Create the application frame
        final JFrame application = new JFrame("Signup Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create the data access object with CSV file persistence
        final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv");

        // Create the view models
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final SignupViewModel signupViewModel = new SignupViewModel();

        // Create the signup view
        final SignupView signupView = new SignupView(signupViewModel, viewManagerModel);

        // Create the signup controller using the factory
        final SignupController signupController = SignupUseCaseFactory.createSignupUseCase(
                viewManagerModel, signupViewModel, userDataAccessObject);

        // Set the controller in the view
        signupView.setSignupController(signupController);

        // Set up the application
        application.add(signupView);
        application.pack();
        application.setSize(500, 600);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
