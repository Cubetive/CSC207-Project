package View;

import interface_adapter.browse_posts.BrowsePostsController;
import interface_adapter.browse_posts.BrowsePostsState;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import use_case.browse_posts.BrowsePostsOutputData;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for browsing posts.
 */
public class BrowsePostsView extends JPanel implements PropertyChangeListener {

    private final String viewName = "browse posts";
    private final BrowsePostsViewModel viewModel;
    private BrowsePostsController controller;

    private final JPanel postsPanel;
    private final JScrollPane scrollPane;
    private final JButton refreshButton;

    public BrowsePostsView(BrowsePostsViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());

        // Title
        final JLabel title = new JLabel(BrowsePostsViewModel.TITLE_LABEL);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Posts panel with vertical layout
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Scroll pane for posts
        scrollPane = new JScrollPane(postsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Refresh button
        refreshButton = new JButton(BrowsePostsViewModel.REFRESH_BUTTON_LABEL);
        refreshButton.addActionListener(e -> {
            if (controller != null) {
                controller.execute();
            }
        });

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Add components to view
        this.add(title, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final BrowsePostsState state = (BrowsePostsState) evt.getNewValue();
            updateView(state);
        }
    }

    /**
     * Updates the view based on the current state.
     */
    private void updateView(BrowsePostsState state) {
        // Clear existing posts
        postsPanel.removeAll();

        if (state.getErrorMessage() != null) {
            // Show error message
            final JLabel errorLabel = new JLabel(state.getErrorMessage());
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsPanel.add(errorLabel);
        } else if (state.getPosts().isEmpty()) {
            // Show "no posts" message
            final JLabel noPostsLabel = new JLabel("No posts available");
            noPostsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsPanel.add(noPostsLabel);
        } else {
            // Display posts
            for (BrowsePostsOutputData.PostData post : state.getPosts()) {
                final JPanel postPanel = createPostPanel(post);
                postsPanel.add(postPanel);
                postsPanel.add(Box.createVerticalStrut(10));
            }
        }

        postsPanel.revalidate();
        postsPanel.repaint();
    }

    /**
     * Creates a panel for displaying a single post.
     */
    private JPanel createPostPanel(BrowsePostsOutputData.PostData post) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        final JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username
        final JLabel usernameLabel = new JLabel("by " + post.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        usernameLabel.setForeground(Color.GRAY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Content
        final JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setOpaque(false);
        contentArea.setFocusable(false);
        contentArea.setCursor(Cursor.getDefaultCursor());
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(contentArea);

        return panel;
    }

    public String getViewName() {
        return viewName;
    }

    public void setController(BrowsePostsController controller) {
        this.controller = controller;
    }

    /**
     * Loads posts when the view becomes visible.
     */
    public void loadPosts() {
        if (controller != null) {
            controller.execute();
        }
    }
}
