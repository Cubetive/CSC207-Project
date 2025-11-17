package View;

import interface_adapter.create_post.CreatePostController;
import interface_adapter.create_post.CreatePostState;
import interface_adapter.create_post.CreatePostViewModel;
import interface_adapter.signup.SignupState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CreatingPostView extends JPanel implements ActionListener, PropertyChangeListener {
    private final CreatePostViewModel createPostViewModel;
    private final JTextField titleTextField = new  JTextField(30);
    private final JTextArea contentTextField = new  JTextArea(10, 30);

    private final JButton createPostButton = new  JButton("Create Post");
    //private final JButton logoutButton = new  JButton("Logout");
    //private final JButton searchButton = new  JButton("Search");
    //private final JButton browseButton = new  JButton("Browse");

    private CreatePostController createPostController;

    public CreatingPostView(CreatePostViewModel inputCreatePostViewModel) {
        this.createPostViewModel = inputCreatePostViewModel;
        this.createPostViewModel.addPropertyChangeListener(this);

        final JLabel header = new JLabel("Create New Post");
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Label for titleTextField
        final JLabel title = new JLabel("Title");

        //Label for contentTextField
        final  JLabel content = new JLabel("Content");

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
        //TODO: Add ActionListeners for other buttons.

        //Placeholder Labels for future buttons
        final JLabel logout = new JLabel("Logout");
        final JLabel search = new JLabel("Search");
        final JLabel browse = new JLabel("Browse");
        final JPanel buttons = new JPanel();
        buttons.add(logout);
        buttons.add(search);
        buttons.add(browse);
        buttons.add(createPostButton);

        //Content setup.
        final JPanel contentPanel = new JPanel();
        contentPanel.add(content);
        contentPanel.add(this.contentTextField);

        //Title setup.
        final JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        titlePanel.add(this.titleTextField);

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final CreatePostState state = (CreatePostState) evt.getNewValue();
        if (state.getMissingError().isEmpty()) {
            JOptionPane.showMessageDialog(this, state.getMissingError());
        }
    }

    public void actionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Most things are not implemented");
    }
}
