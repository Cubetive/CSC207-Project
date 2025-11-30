package view;

import interface_adapter.browse_posts.BrowsePostsController;
import interface_adapter.browse_posts.BrowsePostsState;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import use_case.browse_posts.BrowsePostsOutputData;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;

/**
 * The View for browsing posts.
 */
public class BrowsePostsView extends JPanel implements PropertyChangeListener {

    private final BrowsePostsViewModel viewModel;
    private BrowsePostsController controller;
    private PostClickListener postClickListener;
    private Runnable onLogoutAction;

    private final JPanel postsPanel;
    private final JScrollPane scrollPane;
    private final JButton refreshButton;
    private final JButton editProfileButton;
    private final JLabel profilePictureLabel;
    private final JPanel titlePanel;
    private Runnable onEditProfileClick;
    private Runnable onProfilePictureUpdate;
    private Runnable onCreatePostClick;

    public BrowsePostsView(BrowsePostsViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(245, 245, 245));

        // Title panel with BorderLayout for left, center, right alignment
        titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(70, 130, 180));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Title in center
        final JLabel title = new JLabel(BrowsePostsViewModel.TITLE_LABEL);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.CENTER);

        // Right panel for profile picture and edit profile button
        final JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(70, 130, 180));
        rightPanel.setOpaque(false);

        // Profile picture label
        profilePictureLabel = new JLabel();
        profilePictureLabel.setPreferredSize(new Dimension(40, 40));
        profilePictureLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        profilePictureLabel.setOpaque(false);
        rightPanel.add(profilePictureLabel);

        // Edit Profile button
        editProfileButton = new JButton("Edit Profile");
        editProfileButton.setFont(new Font("Arial", Font.PLAIN, 12));
        editProfileButton.setFocusPainted(false);
        editProfileButton.setBackground(new Color(255, 255, 255));
        editProfileButton.setForeground(new Color(70, 130, 180));
        editProfileButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        editProfileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editProfileButton.addActionListener(e -> {
            if (onEditProfileClick != null) {
                onEditProfileClick.run();
            }
        });
        rightPanel.add(editProfileButton);

        // Left panel for logout button
        final JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(new Color(70, 130, 180));
        leftPanel.setOpaque(false);

        // Logout button
        final JButton logoutButton = new JButton(LogoutPresenter.LOGOUT_BUTTON);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(166, 166, 166));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 100, 150), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            if (onLogoutAction != null) {
                onLogoutAction.run();
            }
        });
        leftPanel.add(logoutButton);

        titlePanel.add(title, BorderLayout.CENTER);
        titlePanel.add(leftPanel, BorderLayout.WEST);
        titlePanel.add(rightPanel, BorderLayout.EAST);

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
        
        // Create Post button
        final JButton createPostButton = new JButton("Create Post");
        createPostButton.setFont(new Font("Arial", Font.PLAIN, 14));
        createPostButton.setFocusPainted(false);
        createPostButton.setBackground(new Color(34, 139, 34)); // Green color
        createPostButton.setForeground(Color.WHITE);
        createPostButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        createPostButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createPostButton.addActionListener(e -> {
            if (onCreatePostClick != null) {
                onCreatePostClick.run();
            }
        });

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(createPostButton);
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
                    // If this post references another post, navigate to the referenced post
                    // Otherwise, navigate to this post
                    final long postIdToLoad = (post.hasReference() && post.getReferencedPostId() != null)
                            ? post.getReferencedPostId()
                            : post.getId();
                    postClickListener.onPostClicked(postIdToLoad);
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

        // Title with reference indicator
        final JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setOpaque(false);
        
        final JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 50));
        titlePanel.add(titleLabel);
        
        // Add clickable reference indicator if post has a reference
        if (post.hasReference() && post.getReferencedPostId() != null) {
            final JButton referenceButton = new JButton("🔗 References: " + 
                    (post.getReferencedPostTitle() != null && !post.getReferencedPostTitle().isEmpty() 
                            ? post.getReferencedPostTitle() 
                            : "Another Post"));
            referenceButton.setFont(new Font("Arial", Font.ITALIC, 12));
            referenceButton.setForeground(new Color(70, 130, 180));
            referenceButton.setBackground(Color.WHITE);
            referenceButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            referenceButton.setFocusPainted(false);
            referenceButton.setContentAreaFilled(false);
            referenceButton.setOpaque(true);
            referenceButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            referenceButton.addActionListener(e -> {
                if (postClickListener != null) {
                    postClickListener.onPostClicked(post.getReferencedPostId());
                }
            });
            // Add hover effect
            referenceButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    referenceButton.setBackground(new Color(240, 248, 255));
                    referenceButton.setForeground(new Color(50, 100, 150));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    referenceButton.setBackground(Color.WHITE);
                    referenceButton.setForeground(new Color(70, 130, 180));
                }
            });
            titlePanel.add(referenceButton);
        }
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        panel.add(titlePanel);
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

    public void setOnLogoutAction(Runnable onLogoutAction) {
        this.onLogoutAction = onLogoutAction;
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

    public void setOnEditProfileClick(Runnable onEditProfileClick) {
        this.onEditProfileClick = onEditProfileClick;
    }
    
    public void setOnCreatePostClick(Runnable onCreatePostClick) {
        this.onCreatePostClick = onCreatePostClick;
    }

    /**
     * Updates the profile picture display.
     * @param profilePicturePath the path to the profile picture file, or null/empty if no picture
     */
    public void updateProfilePicture(String profilePicturePath) {
        if (profilePicturePath != null && !profilePicturePath.trim().isEmpty()) {
            try {
                final File imageFile = new File(profilePicturePath);
                if (imageFile.exists() && imageFile.isFile()) {
                    // Load and scale the image
                    final ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());
                    final Image originalImage = originalIcon.getImage();
                    final Image scaledImage = originalImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    final ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    profilePictureLabel.setIcon(scaledIcon);
                    profilePictureLabel.setText("");
                } else {
                    // File doesn't exist, show placeholder
                    profilePictureLabel.setIcon(null);
                    profilePictureLabel.setText("?");
                    profilePictureLabel.setForeground(Color.WHITE);
                    profilePictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
                }
            } catch (Exception e) {
                // Error loading image, show placeholder
                profilePictureLabel.setIcon(null);
                profilePictureLabel.setText("?");
                profilePictureLabel.setForeground(Color.WHITE);
                profilePictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } else {
            // No profile picture, show placeholder
            profilePictureLabel.setIcon(null);
            profilePictureLabel.setText("?");
            profilePictureLabel.setForeground(Color.WHITE);
            profilePictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        titlePanel.revalidate();
        titlePanel.repaint();
    }

    /**
     * Sets a callback to be called when profile picture should be updated.
     * This allows the view to refresh the profile picture when needed.
     */
    public void setOnProfilePictureUpdate(Runnable onProfilePictureUpdate) {
        this.onProfilePictureUpdate = onProfilePictureUpdate;
    }

    /**
     * Interface for handling post clicks.
     */
    public interface PostClickListener {
        void onPostClicked(long postId);
    }
}
