package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

/**
 * The View for the Login Use Case.
 */
public class LoginView extends JPanel implements PropertyChangeListener {

    private final LoginViewModel loginViewModel;
    private LoginController loginController;

    private final JTextField usernameInputField = new JTextField(20);
    private final JPasswordField passwordInputField = new JPasswordField(20);

    private final JLabel usernameErrorField = new JLabel();
    private final JLabel passwordErrorField = new JLabel();

    private final JButton loginButton;
    private final JButton toSignupButton;
    private boolean isUpdatingFromState;

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        // Set up the view title
        final JLabel title = new JLabel(LoginViewModel.TITLE_LABEL);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initialize buttons
        loginButton = new JButton(LoginViewModel.LOGIN_BUTTON_LABEL);
        toSignupButton = new JButton(LoginViewModel.TO_SIGNUP_BUTTON_LABEL);

        // Set error labels to red
        usernameErrorField.setForeground(Color.RED);
        passwordErrorField.setForeground(Color.RED);

        // Set up the input fields with document listeners
        addDocumentListener(usernameInputField, this::updateUsername);
        addDocumentListener(passwordInputField, this::updatePassword);

        // Set up button actions
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (loginController != null) {
                    final LoginState currentState = loginViewModel.getState();
                    loginController.execute(
                            currentState.getUsername(),
                            currentState.getPassword()
                    );
                }
            }
        });

        toSignupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (loginController != null) {
                    loginController.switchToSignupView();
                }
            }
        });

        // Layout the components
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(20));
        this.add(title);
        this.add(Box.createVerticalStrut(20));

        // Add input fields with labels and error messages
        addInputField(LoginViewModel.USERNAME_LABEL, usernameInputField, usernameErrorField);
        addInputField(LoginViewModel.PASSWORD_LABEL, passwordInputField, passwordErrorField);

        // Add buttons
        this.add(Box.createVerticalStrut(20));
        final JPanel buttons = new JPanel();
        buttons.add(loginButton);
        buttons.add(toSignupButton);
        this.add(buttons);
        this.add(Box.createVerticalStrut(20));
    }

    /**
     * Adds an input field with its label and error message to the view.
     * @param labelText The label text
     * @param inputField The input field
     * @param errorField The error field
     */
    private void addInputField(String labelText, JTextField inputField, JLabel errorField) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        final JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputField.setMaximumSize(new Dimension(300, 30));

        errorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorField.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(inputField);
        panel.add(Box.createVerticalStrut(2));
        panel.add(errorField);
        panel.add(Box.createVerticalStrut(10));

        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(panel);
    }

    /**
     * Adds a document listener to a text field that calls the update function.
     * @param textField The text field
     * @param updateFunction The runnable update
     */
    private void addDocumentListener(JTextField textField, Runnable updateFunction) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent evt) {
                updateFunction.run();
            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                updateFunction.run();
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                updateFunction.run();
            }
        });
    }

    private void updateUsername() {
        if (!isUpdatingFromState) {
            final LoginState currentState = loginViewModel.getState();
            currentState.setUsername(usernameInputField.getText());
            loginViewModel.setState(currentState);
        }
    }

    private void updatePassword() {
        if (!isUpdatingFromState) {
            final LoginState currentState = loginViewModel.getState();
            currentState.setPassword(new String(passwordInputField.getPassword()));
            loginViewModel.setState(currentState);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final LoginState state = (LoginState) evt.getNewValue();
            updateViewFromState(state);
        }
    }

    /**
     * Updates the view based on the current state.
     * @param state The Login state
     */
    private void updateViewFromState(LoginState state) {
        isUpdatingFromState = true;

        // Update input fields
        usernameInputField.setText(state.getUsername());
        passwordInputField.setText(state.getPassword());

        // Update error fields
        usernameErrorField.setText(state.getUsernameError() != null ? state.getUsernameError() : "");
        passwordErrorField.setText(state.getPasswordError() != null ? state.getPasswordError() : "");

        isUpdatingFromState = false;
    }

    /**
     * Returns the name of this view.
     *
     * @return the view name
     */
    public String getViewName() {
        return loginViewModel.getViewName();
    }

    /**
     * Sets the login controller for this view.
     *
     * @param loginController the login controller
     */
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
