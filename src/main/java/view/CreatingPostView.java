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
                                    currentState.getContent()
                            );
                            boolean succeeded = createPostController.isSuccess();
                            if (succeeded) {
                                setVisible(false);
                                contentTextField.setText("");
                                titleTextField.setText("");
                                createPostController.resetSuccess();
                            }
                        }
                    }

                }
        );

        //Placeholder Labels for future buttons
        final JButton backToBrowse = new JButton("Back");
        final JPanel buttons = new JPanel();
        buttons.add(backToBrowse);
        buttons.add(createPostButton);
        backToBrowse.addActionListener(
                // Button Logic
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(backToBrowse)) {
                            setVisible(false);
                            contentTextField.setText("");
                            titleTextField.setText("");
                            createPostController.resetSuccess();
                            createPostController.switchToBrowseView();
                        }
                    }

                }
        );

        //Grid Setup
        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Common constraints
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; // Right align
        gbc.weightx = 0.3; // 30% for labels
        grid.add(new JLabel("Title:", SwingConstants.RIGHT), gbc);

        // Title TextField
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.7; // 70% for input fields
        grid.add(titleTextField, gbc);

        // Content Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST; // Right align at top
        gbc.weightx = 0.3;
        grid.add(new JLabel("Content:", SwingConstants.RIGHT), gbc);

        // Content TextArea
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0; // Allow vertical expansion
        JTextArea textArea = new JTextArea(5, 20);
        grid.add(contentTextField, gbc);

        //Document Listeners for input fields
        addDocumentListener(titleTextField, this::updateTitle);
        addDocumentListener(contentTextField, this::updateContent);

        //Final Self Setup (This is a JPanel)
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(header);
        this.add(grid);
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