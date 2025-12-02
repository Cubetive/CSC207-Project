package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.browse_posts.BrowsePostsController;
import interface_adapter.browse_posts.BrowsePostsState;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.create_post.CreatePostViewModel;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.search_post.SearchPostController;
import use_case.browse_posts.BrowsePostsOutputData;
import use_case.search_post.SearchPostInputData;

/**
 * The View for browsing posts.
 */
public class BrowsePostsView extends JPanel implements PropertyChangeListener {

    // Color constants
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color HEADER_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_GRAY = new Color(166, 166, 166);
    private static final Color BORDER_DARK = new Color(50, 100, 150);
    private static final Color ERROR_COLOR = new Color(220, 53, 69);
    private static final Color TEXT_GRAY = new Color(120, 120, 120);
    private static final Color BORDER_LIGHT = new Color(220, 220, 220);
    private static final Color DARK_TEXT = new Color(50, 50, 50);
    private static final Color CONTENT_GRAY = new Color(80, 80, 80);
    private static final Color HOVER_BG = new Color(248, 250, 252);
    private static final Color LIGHT_BLUE_BG = new Color(240, 248, 255);
    private static final Color GREEN_BUTTON = new Color(34, 139, 34);
    private static final Color WHITE_COLOR = Color.WHITE;

    // Size constants
    private static final int PADDING_SMALL = 5;
    private static final int PADDING_MEDIUM = 10;
    private static final int PADDING_LARGE = 15;
    private static final int PADDING_XLARGE = 20;
    private static final int PADDING_HEADER = 35;
    private static final int PROFILE_PIC_SIZE = 40;
    private static final int SCROLL_INCREMENT = 16;
    private static final int POST_PANEL_HEIGHT = 160;
    private static final int BORDER_THICKNESS = 3;
    private static final int CONTENT_PREVIEW_ROWS = 2;
    private static final int BORDER_WIDTH_TWO = 2;
    private static final int BUTTON_PADDING_VERT = 8;

    // Font size constants
    private static final int FONT_SMALL = 12;
    private static final int FONT_MEDIUM = 13;
    private static final int FONT_REGULAR = 14;
    private static final int FONT_LARGE = 16;
    private static final int FONT_TITLE = 18;

    // Padding constants for borders
    private static final int BORDER_PAD_SMALL = 6;
    private static final int BORDER_PAD_LARGE = 14;
    private static final int BORDER_PAD_XLARGE = 17;
    private static final int BORDER_PAD_POST = 18;

    // Font name constant
    private static final String FONT_ARIAL = "Arial";

    // Placeholder text constant
    private static final String PLACEHOLDER_TEXT = "?";

    private static PostClickListener postClickListener;
    private static JTextField searchField;

    private final BrowsePostsViewModel viewModel;
    private BrowsePostsController controller;
    private Runnable onLogoutAction;
    private Runnable onEditProfileClick;
    private Runnable onCreatePostClick;
    private Runnable onProfilePictureUpdate;

    private final JPanel postsPanel;
    private final JLabel profilePictureLabel;
    private final JPanel titlePanel;

    /**
     * Creates a new BrowsePostsView.
     *
     * @param browsePostsViewModel the view model for browsing posts
     * @param createPostVm the view model for creating posts
     */
    public BrowsePostsView(BrowsePostsViewModel browsePostsViewModel, CreatePostViewModel createPostVm) {
        this.viewModel = browsePostsViewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBackground(BACKGROUND_COLOR);

        titlePanel = createTitlePanel();
        postsPanel = createPostsPanel();
        profilePictureLabel = createProfilePictureLabel();

        final JScrollPane scrollPane = createScrollPane();
        final JPanel buttonPanel = createButtonPanel();

        setupTitlePanel();

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(HEADER_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING_LARGE, PADDING_XLARGE, PADDING_HEADER, PADDING_XLARGE));
        return panel;
    }

    private JPanel createPostsPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING_XLARGE, PADDING_XLARGE, PADDING_XLARGE,
                PADDING_XLARGE));
        return panel;
    }

    private JLabel createProfilePictureLabel() {
        final JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(PROFILE_PIC_SIZE, PROFILE_PIC_SIZE));
        label.setBorder(BorderFactory.createLineBorder(WHITE_COLOR, BORDER_WIDTH_TWO));
        label.setOpaque(false);
        return label;
    }

    private JScrollPane createScrollPane() {
        final JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        return scrollPane;
    }

    private JPanel createButtonPanel() {
        final JButton refreshButton = createRefreshButton();
        final JButton createPostButton = createCreatePostButton();

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalStrut(PADDING_MEDIUM));
        buttonPanel.add(createPostButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, 0, PADDING_LARGE, 0));
        return buttonPanel;
    }

    private JButton createRefreshButton() {
        final JButton refreshButton = new JButton(BrowsePostsViewModel.REFRESH_BUTTON_LABEL);
        refreshButton.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_REGULAR));
        refreshButton.setFocusPainted(false);
        refreshButton.setBackground(HEADER_COLOR);
        refreshButton.setForeground(WHITE_COLOR);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_XLARGE, PADDING_MEDIUM,
                PADDING_XLARGE));
        refreshButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshButton.setOpaque(true);
        refreshButton.addActionListener(evt -> {
            if (controller != null) {
                controller.execute();
            }
        });
        return refreshButton;
    }

    private JButton createCreatePostButton() {
        final JButton createPostButton = new JButton(CreatePostViewModel.CREATE_BUTTON_LABEL);
        createPostButton.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_REGULAR));
        createPostButton.setFocusPainted(false);
        createPostButton.setBackground(GREEN_BUTTON);
        createPostButton.setForeground(WHITE_COLOR);
        createPostButton.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_XLARGE, PADDING_MEDIUM,
                PADDING_XLARGE));
        createPostButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createPostButton.setOpaque(true);
        createPostButton.addActionListener(evt -> {
            if (onCreatePostClick != null) {
                onCreatePostClick.run();
            }
            else if (controller != null) {
                controller.switchToCreatePostView();
            }
        });
        return createPostButton;
    }

    private void setupTitlePanel() {
        final JPanel searchPanel = createSearchPanel();
        final JPanel rightPanel = createRightPanel();
        final JPanel leftPanel = createLeftPanel();

        titlePanel.add(searchPanel, BorderLayout.CENTER);
        titlePanel.add(leftPanel, BorderLayout.WEST);
        titlePanel.add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createSearchPanel() {
        final JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setOpaque(false);

        searchField = new JTextField();
        searchField.setOpaque(false);
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, PROFILE_PIC_SIZE + PADDING_MEDIUM));

        final Border thickLineBorder = BorderFactory.createLineBorder(WHITE_COLOR, BORDER_THICKNESS);
        final TitledBorder titledBorder = BorderFactory.createTitledBorder(
                thickLineBorder,
                "Search Posts",
                TitledBorder.LEADING,
                TitledBorder.TOP,
                null,
                WHITE_COLOR
        );

        searchField.setBorder(titledBorder);
        searchPanel.add(searchField);

        searchFieldListener();
        return searchPanel;
    }

    private JPanel createRightPanel() {
        final JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, PADDING_MEDIUM, 0));
        rightPanel.setBackground(HEADER_COLOR);
        rightPanel.setOpaque(false);
        rightPanel.add(profilePictureLabel);

        final JButton editProfileButton = new JButton("Edit Profile");
        editProfileButton.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_SMALL));
        editProfileButton.setFocusPainted(false);
        editProfileButton.setBackground(WHITE_COLOR);
        editProfileButton.setForeground(HEADER_COLOR);
        editProfileButton.setOpaque(true);
        editProfileButton.setBorderPainted(false);
        editProfileButton.setContentAreaFilled(true);
        editProfileButton.setBorder(BorderFactory.createEmptyBorder(BUTTON_PADDING_VERT, PADDING_LARGE,
                BUTTON_PADDING_VERT, PADDING_LARGE));
        editProfileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editProfileButton.addActionListener(evt -> {
            if (onEditProfileClick != null) {
                onEditProfileClick.run();
            }
        });
        rightPanel.add(editProfileButton);
        return rightPanel;
    }

    private JPanel createLeftPanel() {
        final JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(HEADER_COLOR);
        leftPanel.setOpaque(false);

        final JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING_MEDIUM, 0));
        logoutPanel.setBackground(HEADER_COLOR);
        logoutPanel.setOpaque(false);

        final JButton logoutButton = new JButton(LogoutPresenter.LOGOUT_BUTTON);
        logoutButton.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_SMALL));
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(BUTTON_GRAY);
        logoutButton.setForeground(WHITE_COLOR);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 1),
                BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_LARGE, PADDING_MEDIUM, PADDING_LARGE)
        ));
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(evt -> {
            if (onLogoutAction != null) {
                onLogoutAction.run();
            }
        });
        logoutPanel.add(logoutButton);
        leftPanel.add(logoutPanel, BorderLayout.SOUTH);
        return leftPanel;
    }

    /**
     * Adds a listener to the search field to filter posts as the user types.
     */
    public void searchFieldListener() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent evt) {
                searchPosts();
            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                searchPosts();
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                searchPosts();
            }

            private void searchPosts() {
                final String keyword = searchField.getText();
                final SearchPostController searchPostController =
                        new SearchPostController(new SearchPostInputData(postsPanel,
                                viewModel.getState(), keyword));
                searchPostController.searchPosts();
            }
        });
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
     *
     * @param state the current browse posts state
     */
    private void updateView(BrowsePostsState state) {
        postsPanel.removeAll();

        if (state.getErrorMessage() != null) {
            showCenteredMessage(state.getErrorMessage(), ERROR_COLOR);
        }
        else if (state.getPosts().isEmpty()) {
            showCenteredMessage("No posts available", TEXT_GRAY);
        }
        else {
            displayPosts(state);
        }

        postsPanel.revalidate();
        postsPanel.repaint();
    }

    private void showCenteredMessage(String message, Color color) {
        final JLabel label = new JLabel(message);
        label.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_LARGE));
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        postsPanel.add(Box.createVerticalGlue());
        postsPanel.add(label);
        postsPanel.add(Box.createVerticalGlue());
    }

    private void displayPosts(BrowsePostsState state) {
        for (BrowsePostsOutputData.PostData post : state.getPosts()) {
            final JPanel postPanel = createPostPanel(post);
            postsPanel.add(postPanel);
            postsPanel.add(Box.createVerticalStrut(PADDING_LARGE));
        }
    }

    /**
     * Creates a panel for displaying a single post.
     *
     * @param post the post data to display
     * @return the created post panel
     */
    public static JPanel createPostPanel(BrowsePostsOutputData.PostData post) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                BorderFactory.createEmptyBorder(PADDING_LARGE, BORDER_PAD_POST, PADDING_LARGE, BORDER_PAD_POST)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, POST_PANEL_HEIGHT));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addPanelMouseListener(panel, post);
        addPanelContent(panel, post);

        return panel;
    }

    private static void addPanelMouseListener(JPanel panel, BrowsePostsOutputData.PostData post) {
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (postClickListener != null) {
                    if (searchField != null) {
                        searchField.setText("");
                    }
                    postClickListener.onPostClicked(post.getId());
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(HOVER_BG);
                panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(HEADER_COLOR, BORDER_WIDTH_TWO),
                        BorderFactory.createEmptyBorder(BORDER_PAD_LARGE, BORDER_PAD_XLARGE, BORDER_PAD_LARGE,
                                BORDER_PAD_XLARGE)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(WHITE_COLOR);
                panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                        BorderFactory.createEmptyBorder(PADDING_LARGE, BORDER_PAD_POST, PADDING_LARGE, BORDER_PAD_POST)
                ));
            }
        });
    }

    private static void addPanelContent(JPanel panel, BrowsePostsOutputData.PostData post) {
        final JPanel postTitlePanel = createPostTitlePanel(post);
        final JLabel usernameLabel = createUsernameLabel(post);
        final JTextArea contentArea = createContentArea(post);

        panel.add(postTitlePanel);
        panel.add(Box.createVerticalStrut(BORDER_PAD_SMALL));
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(PADDING_MEDIUM));
        panel.add(contentArea);
    }

    private static JPanel createPostTitlePanel(BrowsePostsOutputData.PostData post) {
        final JPanel postTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING_SMALL, 0));
        postTitlePanel.setBackground(WHITE_COLOR);
        postTitlePanel.setOpaque(false);

        final JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setName("titlePost");
        titleLabel.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_TITLE));
        titleLabel.setForeground(DARK_TEXT);
        postTitlePanel.add(titleLabel);

        if (post.hasReference()) {
            addReferenceButton(postTitlePanel, post);
        }
        postTitlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return postTitlePanel;
    }

    private static void addReferenceButton(JPanel postTitlePanel, BrowsePostsOutputData.PostData post) {
        final boolean canNavigate = post.getReferencedPostId() != null;
        final String refTitle = post.getReferencedPostTitle() != null && !post.getReferencedPostTitle().isEmpty()
                ? post.getReferencedPostTitle()
                : "Another Post";

        final JButton referenceButton = new JButton("[Ref] References: " + refTitle);
        referenceButton.setFont(new Font(FONT_ARIAL, Font.ITALIC, FONT_SMALL));
        referenceButton.setForeground(HEADER_COLOR);
        referenceButton.setBackground(WHITE_COLOR);
        referenceButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(HEADER_COLOR, 1),
                BorderFactory.createEmptyBorder(BORDER_WIDTH_TWO, BORDER_PAD_SMALL, BORDER_WIDTH_TWO, BORDER_PAD_SMALL)
        ));
        referenceButton.setFocusPainted(false);
        referenceButton.setContentAreaFilled(false);
        referenceButton.setOpaque(true);
        referenceButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (canNavigate) {
            referenceButton.addActionListener(evt -> {
                if (postClickListener != null) {
                    postClickListener.onPostClicked(post.getReferencedPostId());
                }
            });
        }
        else {
            referenceButton.setEnabled(false);
            referenceButton.setToolTipText("Referenced post ID not available");
        }

        addReferenceButtonHoverEffect(referenceButton);
        postTitlePanel.add(referenceButton);
    }

    private static void addReferenceButtonHoverEffect(JButton referenceButton) {
        referenceButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                referenceButton.setBackground(LIGHT_BLUE_BG);
                referenceButton.setForeground(BORDER_DARK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                referenceButton.setBackground(WHITE_COLOR);
                referenceButton.setForeground(HEADER_COLOR);
            }
        });
    }

    private static JLabel createUsernameLabel(BrowsePostsOutputData.PostData post) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        final String dateString = dateFormat.format(post.getCreationDate());
        final JLabel usernameLabel = new JLabel("by " + post.getUsername() + " - " + dateString);
        usernameLabel.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_MEDIUM));
        usernameLabel.setForeground(TEXT_GRAY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return usernameLabel;
    }

    private static JTextArea createContentArea(BrowsePostsOutputData.PostData post) {
        final JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_REGULAR));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setOpaque(false);
        contentArea.setFocusable(false);
        contentArea.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contentArea.setForeground(CONTENT_GRAY);
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentArea.setRows(CONTENT_PREVIEW_ROWS);
        return contentArea;
    }

    /**
     * Returns the name of this view.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewModel.getViewName();
    }

    /**
     * Sets the controller for this view.
     *
     * @param browsePostsController the browse posts controller
     */
    public void setController(BrowsePostsController browsePostsController) {
        this.controller = browsePostsController;
    }

    /**
     * Sets the action to be executed when logout is triggered.
     *
     * @param logoutAction the logout action
     */
    public void setOnLogoutAction(Runnable logoutAction) {
        this.onLogoutAction = logoutAction;
    }

    /**
     * Loads posts when the view becomes visible.
     */
    public void loadPosts() {
        System.out.println("VIEW DEBUG: loadPosts called. Checking controller...");
        if (controller != null) {
            System.out.println("VIEW DEBUG: Controller found. Executing...");
            controller.execute();
        }
        else {
            System.err.println("VIEW ERROR: Controller is NULL! The use case was not initialized correctly.");
        }
    }

    /**
     * Sets the listener for post click events.
     *
     * @param listener the post click listener
     */
    public void setPostClickListener(PostClickListener listener) {
        BrowsePostsView.postClickListener = listener;
    }

    /**
     * Sets the action to be executed when edit profile is clicked.
     *
     * @param editProfileClick the edit profile action
     */
    public void setOnEditProfileClick(Runnable editProfileClick) {
        this.onEditProfileClick = editProfileClick;
    }

    /**
     * Sets the action to be executed when create post is clicked.
     *
     * @param createPostClick the create post action
     */
    public void setOnCreatePostClick(Runnable createPostClick) {
        this.onCreatePostClick = createPostClick;
    }

    /**
     * Sets the callback to be executed when the profile picture needs to be updated.
     *
     * @param onProfilePictureUpdate the profile picture update callback
     */
    public void setOnProfilePictureUpdate(Runnable onProfilePictureUpdate) {
        this.onProfilePictureUpdate = onProfilePictureUpdate;
    }

    /**
     * Updates the profile picture display.
     *
     * @param profilePicturePath the path to the profile picture file, or null/empty if no picture
     */
    public void updateProfilePicture(String profilePicturePath) {
        if (profilePicturePath != null && !profilePicturePath.trim().isEmpty()) {
            loadProfilePicture(profilePicturePath);
        }
        else {
            showPlaceholder();
        }
        titlePanel.revalidate();
        titlePanel.repaint();
    }

    private void loadProfilePicture(String profilePicturePath) {
        final File imageFile = new File(profilePicturePath);
        if (imageFile.exists() && imageFile.isFile()) {
            final ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());
            final Image originalImage = originalIcon.getImage();
            final Image scaledImage = originalImage.getScaledInstance(PROFILE_PIC_SIZE, PROFILE_PIC_SIZE,
                    Image.SCALE_SMOOTH);
            final ImageIcon scaledIcon = new ImageIcon(scaledImage);
            profilePictureLabel.setIcon(scaledIcon);
            profilePictureLabel.setText("");
        }
        else {
            showPlaceholder();
        }
    }

    private void showPlaceholder() {
        profilePictureLabel.setIcon(null);
        profilePictureLabel.setText(PLACEHOLDER_TEXT);
        profilePictureLabel.setForeground(WHITE_COLOR);
        profilePictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Interface for handling post clicks.
     */
    public interface PostClickListener {
        /**
         * Called when a post is clicked.
         *
         * @param postId the ID of the clicked post
         */
        void onPostClicked(long postId);
    }
}
