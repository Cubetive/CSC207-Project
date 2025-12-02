package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.create_post.CreatePostController;
import interface_adapter.create_post.CreatePostState;
import interface_adapter.create_post.CreatePostViewModel;

public class CreatingPostView extends JPanel implements ActionListener, PropertyChangeListener {
    /**
     * Create post view model.
     */
    private final CreatePostViewModel createPostViewModel;
    /**
     * Retired (but possibly useful) click listener.
     */
    private CreatingPostView.CreatePostClickListener createPostClickListener;

    /**
     * Content input field.
     */
    private final JTextArea contentTextField = new JTextArea(10, 30);
    /**
     * Content title field.
     */
    private final JTextField titleTextField = new JTextField(30);
    /**
     * Referenced post panel for display.
     */
    private final JPanel referencedPostPanel;
    /**
     * Referenced post label.
     */
    private final JLabel referencedPostLabel;

    /**
     * Create post controller.
     */
    private CreatePostController createPostController;
    /**
     * Referenced post click runnable.
     */
    private Runnable onReferencePostClick;

    /**
     * Set up Constructor.
     * @param inputCreatePostViewModel create post view model to use.
     */
    public CreatingPostView(CreatePostViewModel inputCreatePostViewModel) {
        this.createPostViewModel = inputCreatePostViewModel;
        this.createPostViewModel.addPropertyChangeListener(this);

        final JLabel header = new JLabel("Create New Post");
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton createPostButton = new JButton(CreatePostViewModel.CREATE_BUTTON_LABEL);
        createPostButton.addActionListener(evt -> {
            final CreatePostState currentState = createPostViewModel.getState();
            final String referencedPostId = currentState.getReferencedPostId();

            if (referencedPostId != null && !referencedPostId.isEmpty()) {
                createPostController.execute(currentState.getTitle(),
                        currentState.getContent(), referencedPostId);
            }
            else {
                createPostController.execute(currentState.getTitle(),
                        currentState.getContent());
            }

            if (createPostController.isSuccess()) {
                clearForm();
                createPostController.resetSuccess();
                setVisible(false);
            }
        });

        final JButton backToBrowse = new JButton("Back");
        backToBrowse.addActionListener(evt -> {
            clearForm();
            createPostController.resetSuccess();
            createPostController.switchToBrowseView();
        });

        final JButton referencePostButton = new JButton("Reference Post");
        referencePostButton.addActionListener(event -> {
            if (onReferencePostClick != null) {
                onReferencePostClick.run();
            }
        });

        final JPanel buttons = new JPanel();
        buttons.add(backToBrowse);
        buttons.add(referencePostButton);
        buttons.add(createPostButton);

        // Grid Setup
        final JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();

        // Common constraints
        // Padding
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        // Right align
        gbc.anchor = GridBagConstraints.EAST;
        // 30% for labels
        gbc.weightx = 0.3;
        grid.add(new JLabel("Title:", SwingConstants.RIGHT), gbc);

        // Title TextField
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        // 70% for input fields
        gbc.weightx = 0.7;
        grid.add(titleTextField, gbc);

        // Content Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        // Right align at top
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 0.3;
        grid.add(new JLabel("Content:", SwingConstants.RIGHT), gbc);

        // Content TextArea
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        // Allow vertical expansion
        gbc.weighty = 1.0;
        grid.add(contentTextField, gbc);

        // Document Listeners for input fields
        addDocumentListener(titleTextField, this::updateTitle);
        addDocumentListener(contentTextField, this::updateContent);

        // Referenced post display panel
        referencedPostPanel = new JPanel();
        referencedPostPanel.setLayout(new BoxLayout(referencedPostPanel,
                BoxLayout.Y_AXIS));
        referencedPostPanel.setBackground(new Color(245, 245, 245));
        referencedPostPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Referenced Post"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        referencedPostPanel.setVisible(false);

        referencedPostLabel = new JLabel();
        referencedPostLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        referencedPostLabel.setForeground(new Color(80, 80, 80));
        referencedPostPanel.add(referencedPostLabel);

        // Final Self Setup (This is a JPanel)
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(header);
        this.add(grid);
        this.add(referencedPostPanel);
        this.add(buttons);

    }

    /**
     * Set up method for adding controller.
     * @param createPostController controller to add.
     */
    public void setController(CreatePostController createPostController) {
        this.createPostController = createPostController;
    }

    /**
     * Set up method for adding listeners to JTextFields.
     * @param textField field.
     * @param updateFunction function to update.
     */
    private void addDocumentListener(JTextField textField,
                                     Runnable updateFunction) {
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
     * Set up method for adding listeners to JTextAreas.
     * @param textArea area.
     * @param updateFunction function to update.
     */
    private void addDocumentListener(JTextArea textArea,
                                     Runnable updateFunction) {
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

    /**
     * Property change method reacting to an error having occurred and
     * recorded in the state.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final CreatePostState state = (CreatePostState) evt.getNewValue();
        if (!state.getMissingError().isEmpty()) {
            JOptionPane.showMessageDialog(this, state.getMissingError());
        }
    }

    /**
     * Debug method.
     * @param evt event that has occurred.
     */
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

    private void clearForm() {
        contentTextField.setText("");
        titleTextField.setText("");
        clearReferencedPost();
    }

    /**
     * Getter for the view name.
     * @return the name of the view
     */
    public String getViewName() {
        return this.createPostViewModel.getViewName();
    }

    /**
     * Retired method for button detection.
     * @param listener listener for button.
     */
    public void setCreatePostClickListener(
            CreatingPostView.CreatePostClickListener listener) {
        this.createPostClickListener = listener;
    }

    /**
     * Set up method for runnable item to be executed by
     * reference post use case.
     * @param onReferencePostClick runnable item for reference post use case.
     */
    public void setOnReferencePostClick(Runnable onReferencePostClick) {
        this.onReferencePostClick = onReferencePostClick;
    }
    
    /**
     * Updates the display of the referenced post.
     * @param referencedPostTitle the title of the referenced post
     *                           (or content if no title)
     * @param referencedPostContent the content preview of the referenced post
     */
    public void setReferencedPost(String referencedPostTitle,
                                  String referencedPostContent) {
        if (referencedPostTitle != null && !referencedPostTitle.isEmpty()) {
            final String displayText = "<html><b>Title:</b> " + referencedPostTitle + "<br>"
                    + "<b>Content:</b> "
                    + (referencedPostContent.length() > 100
                            ? referencedPostContent.substring(0, 100) + "..."
                            : referencedPostContent)
                    + "</html>";
            referencedPostLabel.setText(displayText);
            referencedPostPanel.setVisible(true);
        }
        else {
            referencedPostPanel.setVisible(false);
        }
        this.revalidate();
        this.repaint();
    }
    
    /**
     * Clears the referenced post display.
     */
    public void clearReferencedPost() {
        referencedPostPanel.setVisible(false);
        referencedPostLabel.setText("");
        this.revalidate();
        this.repaint();
    }

    /**
     * Interface for Create Post Click Listener.
     */
    public interface CreatePostClickListener {
        /**
         * Fires based on the post clicked.
         * @param postId the id of the post clicked
         */
        void onCreatePostClicked(long postId);
    }
}
