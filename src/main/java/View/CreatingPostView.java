package view;

import interface_adapter.create_post.CreatePostController;
import interface_adapter.create_post.CreatePostState;
import interface_adapter.create_post.CreatePostViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.signup.SignupState;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CreatingPostView extends JPanel implements ActionListener, PropertyChangeListener {
    private final CreatePostViewModel createPostViewModel;
    private CreatingPostView.CreatePostClickListener createPostClickListener;

    private final JTextArea contentTextField = new  JTextArea(10, 30);
    private final JTextField titleTextField = new  JTextField(30);

    private CreatePostController createPostController;

    public CreatingPostView(CreatePostViewModel inputCreatePostViewModel) {
        this.createPostViewModel = inputCreatePostViewModel;
        this.createPostViewModel.addPropertyChangeListener(this);

        final JLabel header = new JLabel("Create New Post");
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton createPostButton = new  JButton("Create Post");
        createPostButton.addActionListener(
                // Button Logic
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(createPostButton)) {
                            final CreatePostState currentState = createPostViewModel.getState();

                            createPostController.execute(currentState.getTitle(),
                                    currentState.getContent(),
                                    currentState.getCreator_username()

                            );
                        }
                    }

                }
        );

        //Document Listeners for input fields
        addDocumentListener(titleTextField, this::updateTitle);
        addDocumentListener(contentTextField, this::updateContent);

        //TODO: Discuss with fred integration problems. In the meantime, below is mouse tracker for updating to
        //TODO: ReadingPostView.

        //TODO: Add ActionListeners for other buttons.

        //Placeholder Labels for future buttons
        final JButton logout = new JButton("Logout");
        final JButton search = new JButton("Search");
        final JButton browse = new JButton("Browse");
        final JButton profile = new JButton("Profile");
        final JPanel buttons = new JPanel();
        buttons.add(logout);
        buttons.add(search);
        buttons.add(browse);
        buttons.add(profile);
        buttons.add(createPostButton);

        //Content setup.
        final JPanel contentPanel = new JPanel();

        final  JLabel content = new JLabel("Content");
        contentPanel.add(content);
        contentPanel.add(contentTextField);

        //Title setup.
        final JPanel titlePanel = new JPanel();

        final JLabel title = new JLabel("Title");
        titlePanel.add(title);
        titlePanel.add(titleTextField);

        //Final Self Setup (This is a JPanel)
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(header);
        this.add(titlePanel);
        this.add(contentPanel);
        this.add(buttons);

    }

    public void setController(CreatePostController createPostController) {
        this.createPostController = createPostController;
    }

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final CreatePostState state = (CreatePostState) evt.getNewValue();
        if (!state.getMissingError().isEmpty()) {
            JOptionPane.showMessageDialog(this, state.getMissingError());
        }
    }

    public void actionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Most things are not implemented");
    }

    private void updateTitle() {
        final CreatePostState currentState = createPostViewModel.getState();
        currentState.setTitle(new String(titleTextField.getText()));
        createPostViewModel.setState(currentState);
    }

    private void updateContent() {
        final CreatePostState currentState = createPostViewModel.getState();
        currentState.setContent(new String(contentTextField.getText()));
        createPostViewModel.setState(currentState);
    }

    public String getViewName() {
        return this.createPostViewModel.getViewName();
    }

    public void setCreatePostClickListener(CreatingPostView.CreatePostClickListener listener) {
        this.createPostClickListener = listener;
    }

    public interface CreatePostClickListener {
        void onCreatePostClicked(long postId);
    }
}
