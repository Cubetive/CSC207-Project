package View;

import interface_adapter.ViewManagerModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Signup Use Case.
 */
public class SignupView extends JPanel implements PropertyChangeListener {

    private final String viewName = "sign up";
    private final SignupViewModel signupViewModel;
    private final ViewManagerModel viewManagerModel;
    private SignupController signupController;

    private final JTextField fullNameInputField = new JTextField(20);
    private final JTextField usernameInputField = new JTextField(20);
    private final JTextField emailInputField = new JTextField(20);
    private final JPasswordField passwordInputField = new JPasswordField(20);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(20);

    private final JLabel fullNameErrorField = new JLabel();
    private final JLabel usernameErrorField = new JLabel();
    private final JLabel emailErrorField = new JLabel();
    private final JLabel passwordErrorField = new JLabel();
    private final JLabel repeatPasswordErrorField = new JLabel();

    private final JButton signUpButton;
    private final JButton toLoginButton;
    private boolean isUpdatingFromState = false;

    public SignupView(SignupViewModel signupViewModel, ViewManagerModel viewManagerModel) {
        this.signupViewModel = signupViewModel;
        this.viewManagerModel = viewManagerModel;
        this.signupViewModel.addPropertyChangeListener(this);
        this.viewManagerModel.addPropertyChangeListener(this);

        // Set up the view title
        final JLabel title = new JLabel(SignupViewModel.TITLE_LABEL);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initialize buttons
        signUpButton = new JButton(SignupViewModel.SIGNUP_BUTTON_LABEL);
        toLoginButton = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);

        // Set error labels to red
        fullNameErrorField.setForeground(Color.RED);
        usernameErrorField.setForeground(Color.RED);
        emailErrorField.setForeground(Color.RED);
        passwordErrorField.setForeground(Color.RED);
        repeatPasswordErrorField.setForeground(Color.RED);

        // Set up the input fields with document listeners
        addDocumentListener(fullNameInputField, this::updateFullName);
        addDocumentListener(usernameInputField, this::updateUsername);
        addDocumentListener(emailInputField, this::updateEmail);
        addDocumentListener(passwordInputField, this::updatePassword);
        addDocumentListener(repeatPasswordInputField, this::updateRepeatPassword);

        // Set up button actions
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (signupController != null) {
                    final SignupState currentState = signupViewModel.getState();
                    signupController.execute(
                            currentState.getFullName(),
                            currentState.getUsername(),
                            currentState.getEmail(),
                            currentState.getPassword(),
                            currentState.getRepeatPassword()
                    );
                }
            }
        });

        toLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    SignupView.this,
                    "Login functionality coming soon!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        // Layout the components
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(20));
        this.add(title);
        this.add(Box.createVerticalStrut(20));

        // Add input fields with labels and error messages
        addInputField(SignupViewModel.FULLNAME_LABEL, fullNameInputField, fullNameErrorField);
        addInputField(SignupViewModel.USERNAME_LABEL, usernameInputField, usernameErrorField);
        addInputField(SignupViewModel.EMAIL_LABEL, emailInputField, emailErrorField);
        addInputField(SignupViewModel.PASSWORD_LABEL, passwordInputField, passwordErrorField);
        addInputField(SignupViewModel.REPEAT_PASSWORD_LABEL, repeatPasswordInputField, repeatPasswordErrorField);

        // Add buttons
        this.add(Box.createVerticalStrut(20));
        final JPanel buttons = new JPanel();
        buttons.add(signUpButton);
        buttons.add(toLoginButton);
        this.add(buttons);
        this.add(Box.createVerticalStrut(20));
    }

    /**
     * Adds an input field with its label and error message to the view.
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
     */
    private void addDocumentListener(JTextField textField, Runnable updateFunction) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFunction.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFunction.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFunction.run();
            }
        });
    }

    private void updateFullName() {
        if (!isUpdatingFromState) {
            final SignupState currentState = signupViewModel.getState();
            currentState.setFullName(fullNameInputField.getText());
            signupViewModel.setState(currentState);
        }
    }

    private void updateUsername() {
        if (!isUpdatingFromState) {
            final SignupState currentState = signupViewModel.getState();
            currentState.setUsername(usernameInputField.getText());
            signupViewModel.setState(currentState);
        }
    }

    private void updateEmail() {
        if (!isUpdatingFromState) {
            final SignupState currentState = signupViewModel.getState();
            currentState.setEmail(emailInputField.getText());
            signupViewModel.setState(currentState);
        }
    }

    private void updatePassword() {
        if (!isUpdatingFromState) {
            final SignupState currentState = signupViewModel.getState();
            currentState.setPassword(new String(passwordInputField.getPassword()));
            signupViewModel.setState(currentState);
        }
    }

    private void updateRepeatPassword() {
        if (!isUpdatingFromState) {
            final SignupState currentState = signupViewModel.getState();
            currentState.setRepeatPassword(new String(repeatPasswordInputField.getPassword()));
            signupViewModel.setState(currentState);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            if (evt.getSource() == signupViewModel) {
                final SignupState state = (SignupState) evt.getNewValue();
                updateViewFromState(state);
            } else if (evt.getSource() == viewManagerModel) {
                final String newView = (String) evt.getNewValue();
                if ("browse posts".equals(newView)) {
                    // Signup was successful - view will switch to browse posts
                    // No need to show a dialog as the view change is immediate
                }
            }
        }
    }

    /**
     * Updates the view based on the current state.
     */
    private void updateViewFromState(SignupState state) {
        isUpdatingFromState = true;

        // Update input fields
        fullNameInputField.setText(state.getFullName());
        usernameInputField.setText(state.getUsername());
        emailInputField.setText(state.getEmail());
        passwordInputField.setText(state.getPassword());
        repeatPasswordInputField.setText(state.getRepeatPassword());

        // Update error fields
        fullNameErrorField.setText(state.getFullNameError() != null ? state.getFullNameError() : "");
        usernameErrorField.setText(state.getUsernameError() != null ? state.getUsernameError() : "");
        emailErrorField.setText(state.getEmailError() != null ? state.getEmailError() : "");
        passwordErrorField.setText(state.getPasswordError() != null ? state.getPasswordError() : "");
        repeatPasswordErrorField.setText(state.getRepeatPasswordError() != null ? state.getRepeatPasswordError() : "");

        isUpdatingFromState = false;
    }

    public String getViewName() {
        return viewName;
    }

    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }
}
