package view;

import interface_adapter.edit_profile.EditProfileController;
import interface_adapter.edit_profile.EditProfileState;
import interface_adapter.edit_profile.EditProfileViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Edit Profile Use Case.
 */
public class EditProfileView extends JPanel implements PropertyChangeListener {

    private final EditProfileViewModel editProfileViewModel;
    private EditProfileController editProfileController;

    private final JTextField usernameInputField = new JTextField(20);
    private final JTextField fullNameInputField = new JTextField(20);
    private final JTextArea bioInputField = new JTextArea(5, 20);
    private final JTextField profilePictureInputField = new JTextField(20);
    private final JPasswordField currentPasswordInputField = new JPasswordField(20);
    private final JPasswordField newPasswordInputField = new JPasswordField(20);
    private final JPasswordField repeatNewPasswordInputField = new JPasswordField(20);

    private final JLabel usernameErrorField = new JLabel();
    private final JLabel fullNameErrorField = new JLabel();
    private final JLabel bioErrorField = new JLabel();
    private final JLabel profilePictureErrorField = new JLabel();
    private final JLabel passwordErrorField = new JLabel();
    private final JLabel generalErrorField = new JLabel();

    private final JButton saveButton;
    private final JButton cancelButton;
    private boolean isUpdatingFromState = false;

    public EditProfileView(EditProfileViewModel editProfileViewModel) {
        this.editProfileViewModel = editProfileViewModel;
        this.editProfileViewModel.addPropertyChangeListener(this);

        // Set up the view title
        final JLabel title = new JLabel(EditProfileViewModel.TITLE_LABEL);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initialize buttons
        saveButton = new JButton(EditProfileViewModel.SAVE_BUTTON_LABEL);
        cancelButton = new JButton(EditProfileViewModel.CANCEL_BUTTON_LABEL);

        // Set error labels to red
        usernameErrorField.setForeground(Color.RED);
        fullNameErrorField.setForeground(Color.RED);
        bioErrorField.setForeground(Color.RED);
        profilePictureErrorField.setForeground(Color.RED);
        passwordErrorField.setForeground(Color.RED);
        generalErrorField.setForeground(Color.RED);

        // Configure bio text area
        bioInputField.setLineWrap(true);
        bioInputField.setWrapStyleWord(true);
        bioInputField.setMaximumSize(new Dimension(300, 100));

        // Set up the input fields with document listeners
        addDocumentListener(usernameInputField, this::updateUsername);
        addDocumentListener(fullNameInputField, this::updateFullName);
        addDocumentListener(bioInputField, this::updateBio);
        addDocumentListener(profilePictureInputField, this::updateProfilePicture);
        addDocumentListener(currentPasswordInputField, this::updateCurrentPassword);
        addDocumentListener(newPasswordInputField, this::updateNewPassword);
        addDocumentListener(repeatNewPasswordInputField, this::updateRepeatNewPassword);

        // Set up button actions
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editProfileController != null) {
                    final EditProfileState currentState = editProfileViewModel.getState();
                    editProfileController.execute(
                            currentState.getCurrentUsername(),
                            currentState.getNewUsername(),
                            currentState.getFullName(),
                            currentState.getBio(),
                            currentState.getProfilePicture(),
                            currentState.getCurrentPassword(),
                            currentState.getNewPassword(),
                            currentState.getRepeatNewPassword()
                    );
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the form and go back to browse posts view
                final EditProfileState state = editProfileViewModel.getState();
                state.setNewUsername(state.getCurrentUsername());
                state.setCurrentPassword("");
                state.setNewPassword("");
                state.setRepeatNewPassword("");
                state.setUsernameError(null);
                state.setFullNameError(null);
                state.setBioError(null);
                state.setProfilePictureError(null);
                state.setPasswordError(null);
                state.setGeneralError(null);
                editProfileViewModel.setState(state);
                editProfileViewModel.firePropertyChange();
            }
        });

        // Layout the components
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(20));
        this.add(title);
        this.add(Box.createVerticalStrut(20));

        // Add general error field
        generalErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        generalErrorField.setFont(new Font("Arial", Font.PLAIN, 12));
        this.add(generalErrorField);
        this.add(Box.createVerticalStrut(5));

        // Add input fields with labels and error messages
        addInputField(EditProfileViewModel.USERNAME_LABEL, usernameInputField, usernameErrorField);
        addInputField(EditProfileViewModel.FULLNAME_LABEL, fullNameInputField, fullNameErrorField);
        addTextAreaField(EditProfileViewModel.BIO_LABEL, bioInputField, bioErrorField);
        addInputField(EditProfileViewModel.PROFILE_PICTURE_LABEL, profilePictureInputField, profilePictureErrorField);
        
        // Add password fields section
        this.add(Box.createVerticalStrut(10));
        final JLabel passwordSectionLabel = new JLabel("Change Password (Optional)");
        passwordSectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordSectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(passwordSectionLabel);
        this.add(Box.createVerticalStrut(5));
        
        addInputField(EditProfileViewModel.CURRENT_PASSWORD_LABEL, currentPasswordInputField, new JLabel());
        addInputField(EditProfileViewModel.NEW_PASSWORD_LABEL, newPasswordInputField, new JLabel());
        addInputField(EditProfileViewModel.REPEAT_NEW_PASSWORD_LABEL, repeatNewPasswordInputField, passwordErrorField);

        // Add buttons
        this.add(Box.createVerticalStrut(20));
        final JPanel buttons = new JPanel();
        buttons.add(saveButton);
        buttons.add(cancelButton);
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
     * Adds a text area field with its label and error message to the view.
     */
    private void addTextAreaField(String labelText, JTextArea textArea, JLabel errorField) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        final JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setMaximumSize(new Dimension(300, 100));
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(300, 100));

        errorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorField.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(scrollPane);
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

    /**
     * Adds a document listener to a text area that calls the update function.
     */
    private void addDocumentListener(JTextArea textArea, Runnable updateFunction) {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
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

    private void updateUsername() {
        if (!isUpdatingFromState) {
            final EditProfileState currentState = editProfileViewModel.getState();
            currentState.setNewUsername(usernameInputField.getText());
            editProfileViewModel.setState(currentState);
        }
    }

    private void updateFullName() {
        if (!isUpdatingFromState) {
            final EditProfileState currentState = editProfileViewModel.getState();
            currentState.setFullName(fullNameInputField.getText());
            editProfileViewModel.setState(currentState);
        }
    }

    private void updateBio() {
        if (!isUpdatingFromState) {
            final EditProfileState currentState = editProfileViewModel.getState();
            currentState.setBio(bioInputField.getText());
            editProfileViewModel.setState(currentState);
        }
    }

    private void updateProfilePicture() {
        if (!isUpdatingFromState) {
            final EditProfileState currentState = editProfileViewModel.getState();
            currentState.setProfilePicture(profilePictureInputField.getText());
            editProfileViewModel.setState(currentState);
        }
    }

    private void updateCurrentPassword() {
        if (!isUpdatingFromState) {
            final EditProfileState currentState = editProfileViewModel.getState();
            currentState.setCurrentPassword(new String(currentPasswordInputField.getPassword()));
            editProfileViewModel.setState(currentState);
        }
    }

    private void updateNewPassword() {
        if (!isUpdatingFromState) {
            final EditProfileState currentState = editProfileViewModel.getState();
            currentState.setNewPassword(new String(newPasswordInputField.getPassword()));
            editProfileViewModel.setState(currentState);
        }
    }

    private void updateRepeatNewPassword() {
        if (!isUpdatingFromState) {
            final EditProfileState currentState = editProfileViewModel.getState();
            currentState.setRepeatNewPassword(new String(repeatNewPasswordInputField.getPassword()));
            editProfileViewModel.setState(currentState);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final EditProfileState state = (EditProfileState) evt.getNewValue();
            updateViewFromState(state);
        }
    }

    /**
     * Updates the view based on the current state.
     */
    private void updateViewFromState(EditProfileState state) {
        isUpdatingFromState = true;

        // Update input fields
        usernameInputField.setText(state.getNewUsername());
        fullNameInputField.setText(state.getFullName());
        bioInputField.setText(state.getBio() != null ? state.getBio() : "");
        profilePictureInputField.setText(state.getProfilePicture() != null ? state.getProfilePicture() : "");
        currentPasswordInputField.setText(state.getCurrentPassword());
        newPasswordInputField.setText(state.getNewPassword());
        repeatNewPasswordInputField.setText(state.getRepeatNewPassword());

        // Update error fields
        usernameErrorField.setText(state.getUsernameError() != null ? state.getUsernameError() : "");
        fullNameErrorField.setText(state.getFullNameError() != null ? state.getFullNameError() : "");
        bioErrorField.setText(state.getBioError() != null ? state.getBioError() : "");
        profilePictureErrorField.setText(state.getProfilePictureError() != null ? state.getProfilePictureError() : "");
        passwordErrorField.setText(state.getPasswordError() != null ? state.getPasswordError() : "");
        generalErrorField.setText(state.getGeneralError() != null ? state.getGeneralError() : "");

        isUpdatingFromState = false;
    }

    /**
     * Loads the current user's data into the view.
     * This should be called when the view is shown.
     * @param currentUsername the current username
     * @param fullName the current full name
     * @param bio the current bio
     * @param profilePicture the current profile picture URL
     */
    public void loadUserData(String currentUsername, String fullName, String bio, String profilePicture) {
        final EditProfileState state = editProfileViewModel.getState();
        state.setCurrentUsername(currentUsername);
        state.setNewUsername(currentUsername);
        state.setFullName(fullName != null ? fullName : "");
        state.setBio(bio != null ? bio : "");
        state.setProfilePicture(profilePicture != null ? profilePicture : "");
        state.setCurrentPassword("");
        state.setNewPassword("");
        state.setRepeatNewPassword("");
        state.setUsernameError(null);
        state.setFullNameError(null);
        state.setBioError(null);
        state.setProfilePictureError(null);
        state.setPasswordError(null);
        state.setGeneralError(null);
        editProfileViewModel.setState(state);
        editProfileViewModel.firePropertyChange();
    }

    public String getViewName() {
        return editProfileViewModel.getViewName();
    }

    public void setEditProfileController(EditProfileController editProfileController) {
        this.editProfileController = editProfileController;
    }
}

