package view;

import interface_adapter.browse_posts.BrowsePostsController;
import interface_adapter.browse_posts.BrowsePostsState;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import use_case.browse_posts.BrowsePostsOutputData;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

/**
 * The View for browsing posts.
 */
public class BrowsePostsView extends JPanel implements PropertyChangeListener {

    private final BrowsePostsViewModel viewModel;
    private BrowsePostsController controller;
    private PostClickListener postClickListener;

    private final JPanel postsPanel;
    private final JScrollPane scrollPane;
    private final JButton refreshButton;

    public BrowsePostsView(BrowsePostsViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(245, 245, 245));

        // Title panel
        final JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        final JLabel title = new JLabel(BrowsePostsViewModel.TITLE_LABEL);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        // Posts panel with vertical layout
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(new Color(245, 245, 245));
        postsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Scroll pane for posts
        scrollPane = new JScrollPane(postsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Refresh button
        refreshButton = new JButton(BrowsePostsViewModel.REFRESH_BUTTON_LABEL);
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            if (controller != null) {
                controller.execute();
            }
        });

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(refreshButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        // Add components to view
        this.add(titlePanel, BorderLayout.NORTH);
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
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setForeground(new Color(220, 53, 69));
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsPanel.add(Box.createVerticalGlue());
            postsPanel.add(errorLabel);
            postsPanel.add(Box.createVerticalGlue());
        } else if (state.getPosts().isEmpty()) {
            // Show "no posts" message
            final JLabel noPostsLabel = new JLabel("No posts available");
            noPostsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            noPostsLabel.setForeground(new Color(120, 120, 120));
            noPostsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsPanel.add(Box.createVerticalGlue());
            postsPanel.add(noPostsLabel);
            postsPanel.add(Box.createVerticalGlue());
        } else {
            // Display posts
            for (BrowsePostsOutputData.PostData post : state.getPosts()) {
                final JPanel postPanel = createPostPanel(post);
                postsPanel.add(postPanel);
                postsPanel.add(Box.createVerticalStrut(15));
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
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Make panel clickable with hover effect
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (postClickListener != null) {
                    postClickListener.onPostClicked(post.getId());
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(248, 250, 252));
                panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        BorderFactory.createEmptyBorder(14, 17, 14, 17)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.WHITE);
                panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(15, 18, 15, 18)
                ));
            }
        });

        // Title
        final JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username and creation date
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        final String dateString = dateFormat.format(post.getCreationDate());
        final JLabel usernameLabel = new JLabel("by " + post.getUsername() + " • " + dateString);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        usernameLabel.setForeground(new Color(120, 120, 120));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Content preview
        final JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setOpaque(false);
        contentArea.setFocusable(false);
        contentArea.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contentArea.setForeground(new Color(80, 80, 80));
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentArea.setRows(2);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(contentArea);

        return panel;
    }

    public String getViewName() {
        return viewModel.getViewName();
    }

    public void setController(BrowsePostsController controller) {
        this.controller = controller;
    }

    /**
     * Loads posts when the view becomes visible.
     */
    public void loadPosts() {
        System.out.println("VIEW DEBUG: loadPosts called. Checking controller...");
        if (controller != null) {
            System.out.println("VIEW DEBUG: Controller found. Executing...");
            controller.execute();
        } else {
            System.err.println("VIEW ERROR: Controller is NULL! The use case was not initialized correctly.");
        }
    }

    public void setPostClickListener(PostClickListener listener) {
        this.postClickListener = listener;
    }

    /**
     * Interface for handling post clicks.
     */
    public interface PostClickListener {
        void onPostClicked(long postId);
    }
}
