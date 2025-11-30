package view;

import interface_adapter.read_post.ReadPostController;
import interface_adapter.read_post.ReadPostState;
import interface_adapter.read_post.ReadPostViewModel;
import interface_adapter.reply_post.ReplyPostController;
import interface_adapter.reply_post.ReplyPostPresenter;
import interface_adapter.upvote_downvote.VoteController;
import interface_adapter.upvote_downvote.VotePresenter;
import interface_adapter.translate.TranslationController; // NEW
import interface_adapter.translate.TranslationViewModel; // NEW
import interface_adapter.translate.TranslationState;
import use_case.read_post.ReadPostOutputData;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The View for reading a post and its replies.
 */
public class PostReadingView extends JPanel implements PropertyChangeListener {

    private final ReadPostViewModel viewModel;
    private TranslationViewModel translationViewModel = new TranslationViewModel();
    private ReadPostController controller;
    private ReplyPostController replyController;
    private VoteController voteController;
    private TranslationController translationController;
    private Runnable onBackAction;
    private long currentPostId = 1;

    private String textContent = "";

    // Fields to track translation UI elements for COMMENTS
    private final Map<String, JTextArea> commentTranslationAreas = new HashMap<>();
    private final Map<String, JLabel> commentTranslationStatusLabels = new HashMap<>();
    private final Map<String, JButton> commentTranslationButtons = new HashMap<>();
    private String lastTextTranslatedKey = null;
    private final Set<String> translationsInProgress = new HashSet<>();
    private static final String MAIN_POST_KEY = "MAIN_POST";
    private static final String CONFIRM_CANCEL_MESSAGE = "You have unsaved changes. Are you sure you want to cancel?";
    private static final String CONFIRM_CANCEL_TITLE = "Confirm Cancel";


    private final JButton backButton;
    private final JLabel titleLabel;
    private final JLabel authorLabel;
    private final JTextArea contentArea;

    // Translation UI Components
    private final JLabel translateLabel;
    private final JComboBox<String> languageDropdown;
    private final JButton translateButton;
    private JTextArea translatedContentArea;
    private JLabel translationStatusLabel;
    private final JScrollPane translatedContentScrollPane;
    // Supported languages for the dropdown
    private static final String[] SUPPORTED_LANGUAGES = {"ar", "cn", "en", "es", "fr", "de", "hi", "it", "ja", "ko", "ru"};


    private final JButton upvoteButton;
    private final JButton downvoteButton;
    private final JLabel voteCountLabel;
    private final JTextField commentField;
    private final JButton commentButton;
    private final JPanel repliesPanel;
    private final JScrollPane scrollPane;
    private final JPanel referencedPostContainer;
    private final JLabel referencedPostTitleLabel;
    private final JTextArea referencedPostContentArea;
    private final JLabel referencedPostAuthorLabel;
    private final JButton viewReferencedPostButton;
    private Runnable onViewReferencedPostClick;
    private final JPanel referencingPostsContainer;
    private final JPanel referencingPostsListPanel;
    private final JPanel referenceBannerPanel;
    private final JButton referenceBannerButton;

    public PostReadingView(ReadPostViewModel viewModel, TranslationViewModel translationViewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        this.translationViewModel = translationViewModel;
        this.translationViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(245, 245, 245));

        // Top panel with back button, title, and author
        final JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 255, 255));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        backButton = new JButton(ReadPostViewModel.BACK_BUTTON_LABEL);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(true);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 100, 150), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            if (onBackAction != null) {
                onBackAction.run();
            }
        });

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(50, 50, 50));

        authorLabel = new JLabel();
        authorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        authorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        authorLabel.setForeground(new Color(100, 100, 100));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(authorLabel, BorderLayout.EAST);

        // Main content panel
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Post content container
        final JPanel contentContainer = new JPanel(new BorderLayout());
        contentContainer.setBackground(Color.WHITE);
        contentContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        contentContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Post content area
        contentArea = new JTextArea();
        contentArea.setFont(new Font("Arial", Font.PLAIN, 15));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.WHITE);
        contentArea.setForeground(new Color(50, 50, 50));

        contentContainer.add(contentArea, BorderLayout.CENTER);

        // Reference banner (shown when this post references another post)
        referenceBannerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        referenceBannerPanel.setBackground(new Color(240, 248, 255)); // Light blue background
        referenceBannerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 4, 1, 1, new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        referenceBannerPanel.setVisible(false);
        referenceBannerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        referenceBannerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        final JLabel referenceBannerLabel = new JLabel("🔗 This post references:");
        referenceBannerLabel.setFont(new Font("Arial", Font.BOLD, 13));
        referenceBannerLabel.setForeground(new Color(50, 50, 50));
        
        referenceBannerButton = new JButton();
        referenceBannerButton.setFont(new Font("Arial", Font.PLAIN, 13));
        referenceBannerButton.setForeground(new Color(70, 130, 180));
        referenceBannerButton.setBackground(new Color(240, 248, 255));
        referenceBannerButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        referenceBannerButton.setFocusPainted(false);
        referenceBannerButton.setContentAreaFilled(false);
        referenceBannerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        referenceBannerButton.addActionListener(e -> {
            if (onViewReferencedPostClick != null) {
                onViewReferencedPostClick.run();
            } else if (controller != null) {
                // Get referenced post ID from state
                final ReadPostState state = viewModel.getState();
                if (state.getReferencedPost() != null) {
                    loadPost(state.getReferencedPost().getId());
                }
            }
        });
        // Add hover effect
        referenceBannerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                referenceBannerButton.setForeground(new Color(50, 100, 150));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                referenceBannerButton.setForeground(new Color(70, 130, 180));
            }
        });
        
        referenceBannerPanel.add(referenceBannerLabel);
        referenceBannerPanel.add(referenceBannerButton);

        // Translation Controls and Display
        final JPanel translationPanel = new JPanel();
        translationPanel.setLayout(new BoxLayout(translationPanel, BoxLayout.Y_AXIS));
        translationPanel.setBackground(new Color(245, 245, 245));
        translationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        translationPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Translation Controls (Dropdown + Button)
        final JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlPanel.setOpaque(false);
        controlPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        translateLabel = new JLabel("Translate to:");
        translateLabel.setFont(new Font("Arial", Font.BOLD, 14));

        languageDropdown = new JComboBox<>(SUPPORTED_LANGUAGES);
        languageDropdown.setSelectedItem("es");

        translateButton = new JButton("Translate Post");
        translateButton.setFont(new Font("Arial", Font.PLAIN, 14));
        translateButton.setFocusPainted(false);
        translateButton.setBackground(new Color(173, 216, 230)); // Light Blue
        translateButton.setForeground(new Color(50, 50, 50));
        translateButton.setOpaque(true);
        translateButton.setBorderPainted(false);
        translateButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(135, 206, 250), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        translateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Inline ActionListener for the translate button
        translateButton.addActionListener(e -> {
            if (translationController == null) { // 💡 NEW: Check for controller existence
                JOptionPane.showMessageDialog(this, "Translation service is not configured.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                translateButton.setEnabled(false);
                translationStatusLabel.setText("Translating...");
                translatedContentArea.setText("Loading translation...");

                translationsInProgress.add(MAIN_POST_KEY);

                String targetLanguage = (String) languageDropdown.getSelectedItem();
                final long postId = currentPostId;
                final String content = textContent;

                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        translationController.execute(postId, content, targetLanguage);
                        return null;
                    }

                    @Override
                    protected void done() {
                        translationViewModel.firePropertyChanged();
                    }
                }.execute();
            } catch (Exception ex) {
                System.err.println("CRASH: Main Post Translation failed on EDT!");
                ex.printStackTrace();
            }
        });

        controlPanel.add(translateLabel);
        controlPanel.add(languageDropdown);
        controlPanel.add(translateButton);
        translationPanel.add(controlPanel);

        // Translation Status and Content Area
        translationStatusLabel = new JLabel("Select a language and click Translate.");
        translationStatusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        translationStatusLabel.setForeground(new Color(150, 150, 150));
        translationStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        translationPanel.add(translationStatusLabel);
        translationPanel.add(Box.createVerticalStrut(5));

        translatedContentArea = new JTextArea(4, 40);
        translatedContentArea.setEditable(false);
        translatedContentArea.setLineWrap(true);
        translatedContentArea.setWrapStyleWord(true);
        translatedContentArea.setFont(new Font("Arial", Font.PLAIN, 15));
        translatedContentArea.setBackground(new Color(230, 230, 235)); // Differentiated background
        translatedContentArea.setForeground(new Color(50, 50, 50));
        translatedContentScrollPane = new JScrollPane(translatedContentArea);
        translatedContentScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        translatedContentScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        translatedContentScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        translationPanel.add(translatedContentScrollPane);

        // Vote panel
        final JPanel votePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        votePanel.setBackground(new Color(245, 245, 245));

        upvoteButton = new JButton("\u25B2");  // Up triangle
        upvoteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        upvoteButton.setFocusPainted(false);
        upvoteButton.setBackground(new Color(240, 240, 240));
        upvoteButton.setOpaque(true);
        upvoteButton.setBorderPainted(false);
        upvoteButton.setContentAreaFilled(true);
        upvoteButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        upvoteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        downvoteButton = new JButton("\u25BC");  // Down triangle
        downvoteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        downvoteButton.setFocusPainted(false);
        downvoteButton.setBackground(new Color(240, 240, 240));
        downvoteButton.setOpaque(true);
        downvoteButton.setBorderPainted(false);
        downvoteButton.setContentAreaFilled(true);
        downvoteButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        downvoteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        voteCountLabel = new JLabel("0");
        voteCountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        voteCountLabel.setForeground(new Color(100, 100, 100));

        votePanel.add(upvoteButton);
        votePanel.add(downvoteButton);
        votePanel.add(voteCountLabel);

        upvoteButton.addActionListener(e -> {
            if (voteController != null) {
                // true = upvote
                voteController.execute(true, currentPostId);
            }
        });

        downvoteButton.addActionListener(e -> {
            if (voteController != null) {
                // false = downvote
                voteController.execute(false, currentPostId);
            }
        });

        // Comments label
        final JLabel commentsLabel = new JLabel(ReadPostViewModel.COMMENTS_LABEL);
        commentsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        commentsLabel.setForeground(new Color(50, 50, 50));
        commentsLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        commentsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Comment input panel
        final JPanel commentInputPanel = new JPanel(new BorderLayout(10, 0));
        commentInputPanel.setBackground(new Color(245, 245, 245));

        commentField = new JTextField();
        commentField.setFont(new Font("Arial", Font.PLAIN, 14));
        commentField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        commentButton = new JButton(ReadPostViewModel.COMMENT_BUTTON_LABEL);
        commentButton.setFont(new Font("Arial", Font.PLAIN, 14));
        commentButton.setFocusPainted(false);
        commentButton.setBackground(new Color(70, 130, 180));
        commentButton.setForeground(Color.WHITE);
        commentButton.setOpaque(true);
        commentButton.setBorderPainted(false);
        commentButton.setContentAreaFilled(true);
        commentButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 100, 150), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        commentButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        commentInputPanel.add(commentField, BorderLayout.CENTER);
        commentInputPanel.add(commentButton, BorderLayout.EAST);
        
        // Add action listener for comment button
        commentButton.addActionListener(e -> {
            final String commentText = commentField.getText().trim();
            if (!commentText.isEmpty() && replyController != null) {
                replyController.execute(commentText, currentPostId);
                commentField.setText(""); // Clear the field after submitting
            }
        });

        // Replies panel
        repliesPanel = new JPanel();
        repliesPanel.setLayout(new BoxLayout(repliesPanel, BoxLayout.Y_AXIS));
        repliesPanel.setBackground(new Color(245, 245, 245));

        // Referenced post panel (initially hidden)
        referencedPostContainer = new JPanel(new BorderLayout());
        referencedPostContainer.setBackground(new Color(240, 248, 255)); // Light blue background
        referencedPostContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Referenced Post"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        referencedPostContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        referencedPostContainer.setVisible(false);
        
        referencedPostTitleLabel = new JLabel();
        referencedPostTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        referencedPostTitleLabel.setForeground(new Color(50, 50, 50));
        
        referencedPostContentArea = new JTextArea(3, 30);
        referencedPostContentArea.setFont(new Font("Arial", Font.PLAIN, 13));
        referencedPostContentArea.setLineWrap(true);
        referencedPostContentArea.setWrapStyleWord(true);
        referencedPostContentArea.setEditable(false);
        referencedPostContentArea.setBackground(new Color(240, 248, 255));
        referencedPostContentArea.setForeground(new Color(80, 80, 80));
        
        referencedPostAuthorLabel = new JLabel();
        referencedPostAuthorLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        referencedPostAuthorLabel.setForeground(new Color(120, 120, 120));
        
        viewReferencedPostButton = new JButton("View Referenced Post");
        viewReferencedPostButton.setFont(new Font("Arial", Font.PLAIN, 12));
        viewReferencedPostButton.setFocusPainted(false);
        viewReferencedPostButton.setBackground(new Color(70, 130, 180));
        viewReferencedPostButton.setForeground(Color.WHITE);
        viewReferencedPostButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        viewReferencedPostButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        final JPanel referencedPostInfoPanel = new JPanel();
        referencedPostInfoPanel.setLayout(new BoxLayout(referencedPostInfoPanel, BoxLayout.Y_AXIS));
        referencedPostInfoPanel.setBackground(new Color(240, 248, 255));
        referencedPostInfoPanel.add(referencedPostTitleLabel);
        referencedPostInfoPanel.add(Box.createVerticalStrut(5));
        referencedPostInfoPanel.add(new JScrollPane(referencedPostContentArea));
        referencedPostInfoPanel.add(Box.createVerticalStrut(5));
        referencedPostInfoPanel.add(referencedPostAuthorLabel);
        
        referencedPostContainer.add(referencedPostInfoPanel, BorderLayout.CENTER);
        referencedPostContainer.add(viewReferencedPostButton, BorderLayout.EAST);
        
        // Referencing posts panel (posts that reference this one)
        referencingPostsContainer = new JPanel(new BorderLayout());
        referencingPostsContainer.setBackground(new Color(255, 248, 240)); // Light orange background
        referencingPostsContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Referenced By"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        referencingPostsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        referencingPostsContainer.setVisible(false);
        
        referencingPostsListPanel = new JPanel();
        referencingPostsListPanel.setLayout(new BoxLayout(referencingPostsListPanel, BoxLayout.Y_AXIS));
        referencingPostsListPanel.setBackground(new Color(255, 248, 240));
        
        final JScrollPane referencingPostsScrollPane = new JScrollPane(referencingPostsListPanel);
        referencingPostsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        referencingPostsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        referencingPostsScrollPane.setBorder(null);
        referencingPostsScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        referencingPostsContainer.add(referencingPostsScrollPane, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.add(referenceBannerPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(contentContainer);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(referencedPostContainer);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(referencingPostsContainer);
        // Add translation panel.
        mainPanel.add(translationPanel);

        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(votePanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(commentsLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(commentInputPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(repliesPanel);

        // Scroll pane for main panel
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add to view
        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == viewModel) {
            if ("state".equals(evt.getPropertyName())) {
                final ReadPostState state = (ReadPostState) evt.getNewValue();
                updateView(state);
            }
        }
        else if (evt.getSource() == translationViewModel) {
            if (evt.getPropertyName().equals(TranslationViewModel.STATE_PROPERTY_NAME)) {

                final TranslationState state = (TranslationState) evt.getNewValue();

                SwingUtilities.invokeLater(() -> {
                    handleTranslationChange(state);
                });
            }
        }
    }

    /**
     * Handles updates from the TranslationViewModel.
     */
    private void handleTranslationChange(TranslationState state) {
        if (lastTextTranslatedKey == null) {
            if (translateButton != null) {
                translateButton.setEnabled(true);
            }

            JTextArea targetArea = translatedContentArea;
            JLabel statusLabel = translationStatusLabel;

            if (targetArea != null && statusLabel != null) {
                if (state.isTranslationSuccessful()) {
                    targetArea.setText(state.getTranslatedText() != null ? state.getTranslatedText() : "");
                    String cacheIndicator = state.isFromCache() ? " (Cached)" : " (API)";
                    statusLabel.setText(
                            String.format("Translated to %s%s. %s",
                                    state.getTargetLanguage().toUpperCase(),
                                    cacheIndicator,
                                    state.getStatusMessage()
                            )
                    );
                } else {
                    targetArea.setText("Translation unavailable.");
                    statusLabel.setText(state.getStatusMessage());
                }
                if (translatedContentScrollPane != null) {
                    translatedContentScrollPane.revalidate();
                    translatedContentScrollPane.repaint();
                }
                translationsInProgress.remove(MAIN_POST_KEY);
            }
        }
        else {
            String lookupKey = lastTextTranslatedKey.trim();

            try {
                JTextArea commentArea = commentTranslationAreas.get(lookupKey);
                JLabel commentStatus = commentTranslationStatusLabels.get(lookupKey);
                JButton commentButton = commentTranslationButtons.get(lookupKey);

                if (commentButton != null) {
                    commentButton.setEnabled(true);
                }

                if (commentArea != null && commentStatus != null) {
                    if (state.isTranslationSuccessful()) {
                        // Ensure text is not null
                        String text = state.getTranslatedText() != null ? state.getTranslatedText() : "";
                        commentArea.setText(text);

                        String cacheIndicator = state.isFromCache() ? " (Cached)" : " (API)";
                        commentStatus.setText(
                                String.format("Translated to %s%s. %s",
                                        state.getTargetLanguage().toUpperCase(),
                                        cacheIndicator,
                                        state.getStatusMessage()
                                )
                        );
                    } else {
                        commentArea.setText("Translation unavailable.");
                        commentStatus.setText(state.getStatusMessage());
                    }

                    try {
                        commentArea.setPreferredSize(null);
                        commentArea.setSize(commentArea.getPreferredSize());

                        JScrollPane parentScrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, commentArea);
                        if (parentScrollPane != null) {
                            parentScrollPane.setViewportView(commentArea);
                            parentScrollPane.revalidate();
                            parentScrollPane.repaint();
                        }

                        commentArea.revalidate();
                        commentArea.repaint();
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed during comment repaint/resize: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                if (repliesPanel != null) {
                    repliesPanel.revalidate();
                    repliesPanel.repaint();
                }

            } catch (Exception e) {
                System.err.println("CRASH: Unhandled exception in Comment Translation logic!");
                e.printStackTrace();
            } finally {
                translationsInProgress.remove(lookupKey);
                lastTextTranslatedKey = null;
            }
        }

        if (this.getParent() != null) {
            this.getParent().revalidate();
            this.getParent().repaint();
        }
        this.invalidate();
        this.revalidate();
        this.repaint();
    }

    /**
     * Resets the main post translation display area.
     */
    private void clearTranslationDisplay() {
        if (translatedContentArea != null) {
            translatedContentArea.setText("");
        }
        if (translationStatusLabel != null) {
            translationStatusLabel.setText("Select a language and click Translate.");
        }
        if (translateButton != null) {
            translateButton.setEnabled(true);
        }
    }

    /**
     * Clears all comment translation areas and internal trackers.
     */
    private void clearCommentTranslationDisplays() {
        commentTranslationAreas.clear();
        commentTranslationStatusLabels.clear();
        commentTranslationButtons.clear();
    }

    /**
     * Updates the view based on the current state.
     */
    private void updateView(ReadPostState state) {
        if (state.getErrorMessage() != null) {
            JOptionPane.showMessageDialog(this, state.getErrorMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.currentPostId = state.getId();
        textContent = state.getContent();

        if (!translationsInProgress.isEmpty()) {
            return;
        }
        clearTranslationDisplay();
        clearCommentTranslationDisplays();

        setTranslationController(translationController);

        titleLabel.setText(state.getTitle());
        authorLabel.setText(state.getUsername());
        contentArea.setText(state.getContent());
        voteCountLabel.setText(String.valueOf(state.getUpvotes() - state.getDownvotes()));
        
        // Update referenced post display
        final ReadPostOutputData.ReferencedPostData referencedPost = state.getReferencedPost();
        if (referencedPost != null) {
            // Show reference banner at top
            final String referencedTitle = referencedPost.getTitle() != null && !referencedPost.getTitle().isEmpty()
                    ? referencedPost.getTitle()
                    : "Original Post";
            referenceBannerButton.setText(referencedTitle);
            referenceBannerPanel.setVisible(true);
            
            // Update referenced post container
            referencedPostTitleLabel.setText(
                    referencedPost.getTitle().isEmpty() ? "Referenced Post" : referencedPost.getTitle());
            referencedPostContentArea.setText(referencedPost.getContent());
            referencedPostAuthorLabel.setText("By: " + referencedPost.getUsername());
            referencedPostContainer.setVisible(true);
            
            // Set up button to view referenced post
            // Remove all existing action listeners
            for (java.awt.event.ActionListener al : viewReferencedPostButton.getActionListeners()) {
                viewReferencedPostButton.removeActionListener(al);
            }
            viewReferencedPostButton.addActionListener(e -> {
                if (onViewReferencedPostClick != null) {
                    onViewReferencedPostClick.run();
                } else if (controller != null) {
                    // Fallback: load the referenced post directly
                    loadPost(referencedPost.getId());
                }
            });
        } else {
            // Hide reference banner and container if no reference
            referenceBannerPanel.setVisible(false);
            referencedPostContainer.setVisible(false);
        }
        
        // Update referencing posts display (posts that reference this one)
        final List<ReadPostOutputData.ReferencingPostData> referencingPosts = state.getReferencingPosts();
        referencingPostsListPanel.removeAll();
        if (referencingPosts != null && !referencingPosts.isEmpty()) {
            referencingPostsContainer.setVisible(true);
            for (ReadPostOutputData.ReferencingPostData refPost : referencingPosts) {
                final JPanel refPostPanel = createReferencingPostPanel(refPost);
                referencingPostsListPanel.add(refPostPanel);
                referencingPostsListPanel.add(Box.createVerticalStrut(5));
            }
        } else {
            referencingPostsContainer.setVisible(false);
        }
        referencingPostsListPanel.revalidate();
        referencingPostsListPanel.repaint();

        // Clear and update replies
        repliesPanel.removeAll();
        if (!state.getReplies().isEmpty()) {
            for (ReadPostOutputData.ReplyData reply : state.getReplies()) {
                final JPanel replyPanel = createReplyPanel(reply, 0);

                // Wrapper to force full width
                final JPanel fullWidthWrapper = new JPanel(new BorderLayout());
                fullWidthWrapper.setBackground(new Color(245, 245, 245));
                fullWidthWrapper.add(replyPanel, BorderLayout.CENTER);
                fullWidthWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

                repliesPanel.add(fullWidthWrapper);
                repliesPanel.add(Box.createVerticalStrut(5));
            }
        }

        repliesPanel.revalidate();
        repliesPanel.repaint();
    }

    /**
     * Creates a panel for displaying a single reply.
     * @param reply the reply data
     * @param indentLevel the indentation level for nested replies
     */
    private JPanel createReplyPanel(ReadPostOutputData.ReplyData reply, int indentLevel) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // Add left indent for nested replies
        final int leftIndent = indentLevel * 15;
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(12, 15 + leftIndent, 12, 15)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // --- Unique Key for this comment (original content) ---
        // Used to map the translation result back to this specific JTextArea
        final String commentKey = reply.getContent().trim();

        // Reply header
        final JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        final JLabel usernameLabel = new JLabel(reply.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        usernameLabel.setForeground(new Color(70, 130, 180));

        // Reply content
        final JTextArea replyContent = new JTextArea(reply.getContent());
        replyContent.setFont(new Font("Arial", Font.PLAIN, 14));
        replyContent.setLineWrap(true);
        replyContent.setWrapStyleWord(true);
        replyContent.setEditable(false);
        replyContent.setOpaque(false);
        replyContent.setFocusable(false);
        replyContent.setForeground(new Color(60, 60, 60));
        replyContent.setAlignmentX(Component.LEFT_ALIGNMENT);
        replyContent.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // --- Comment Translation Controls and Display ---
        final JPanel commentTranslationPanel = new JPanel();
        commentTranslationPanel.setLayout(new BoxLayout(commentTranslationPanel, BoxLayout.Y_AXIS));
        commentTranslationPanel.setBackground(Color.WHITE);
        commentTranslationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentTranslationPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        final JComboBox<String> commentLanguageDropdown = new JComboBox<>(SUPPORTED_LANGUAGES);
        commentLanguageDropdown.setSelectedItem("es");
        commentLanguageDropdown.setFont(new Font("Arial", Font.PLAIN, 12));

        final JButton commentTranslateButton = new JButton("Translate Comment");
        commentTranslateButton.setFont(new Font("Arial", Font.PLAIN, 12));
        commentTranslateButton.setFocusPainted(false);
        commentTranslateButton.setBackground(new Color(200, 220, 240));
        commentTranslateButton.setForeground(new Color(50, 50, 50));
        commentTranslateButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        commentTranslateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Output elements for this specific comment
        final JLabel commentTranslationStatusLabel = new JLabel("Select language and translate.");
        commentTranslationStatusLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        commentTranslationStatusLabel.setForeground(new Color(150, 150, 150));

        final JTextArea translatedReplyContentArea = new JTextArea(3, 40);
        translatedReplyContentArea.setEditable(false);
        translatedReplyContentArea.setLineWrap(true);
        translatedReplyContentArea.setWrapStyleWord(true);
        translatedReplyContentArea.setFont(new Font("Arial", Font.ITALIC, 13));
        translatedReplyContentArea.setBackground(new Color(240, 240, 245));
        translatedReplyContentArea.setForeground(new Color(50, 50, 50));
        final JScrollPane translatedReplyContentScrollPane = new JScrollPane(translatedReplyContentArea);
        translatedReplyContentScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        translatedReplyContentScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        translatedReplyContentScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Store references for later updating in propertyChange
        commentTranslationAreas.put(commentKey, translatedReplyContentArea);
        commentTranslationStatusLabels.put(commentKey, commentTranslationStatusLabel);

        commentTranslationButtons.put(commentKey, commentTranslateButton);

        // Action Listener for Comment Translation
        commentTranslateButton.addActionListener(e -> {
            if (translationController == null) {
                commentTranslationStatusLabel.setText("Error: Translation controller is missing.");
                return;
            }

            commentTranslateButton.setEnabled(false);
            commentTranslationStatusLabel.setText("Translating...");
            translatedReplyContentArea.setText("Loading translation...");

            // Set the tracking key to this comment's content
            lastTextTranslatedKey = commentKey;

            translationsInProgress.add(lastTextTranslatedKey);

            final String targetLanguage = (String) commentLanguageDropdown.getSelectedItem();
            final String replyContentText = reply.getContent();

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    translationController.execute(replyContentText, targetLanguage);
                    return null;
                }
            }.execute();
        });

        final JPanel commentControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        commentControlPanel.setOpaque(false);
        commentControlPanel.add(new JLabel("Translate:"));
        commentControlPanel.add(commentLanguageDropdown);
        commentControlPanel.add(commentTranslateButton);

        commentTranslationPanel.add(commentControlPanel);
        commentTranslationPanel.add(Box.createVerticalStrut(5));
        commentTranslationPanel.add(commentTranslationStatusLabel);
        commentTranslationPanel.add(Box.createVerticalStrut(5));
        commentTranslationPanel.add(translatedReplyContentScrollPane);

        // Vote and reply buttons
        final JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        final JButton replyUpvoteButton = new JButton("\u25B2");
        replyUpvoteButton.setFont(new Font("Arial", Font.PLAIN, 12));
        replyUpvoteButton.setFocusPainted(false);
        replyUpvoteButton.setBackground(new Color(245, 245, 245));
        replyUpvoteButton.setOpaque(true);
        replyUpvoteButton.setBorderPainted(false);
        replyUpvoteButton.setContentAreaFilled(true);
        replyUpvoteButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        replyUpvoteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        final JButton replyDownvoteButton = new JButton("\u25BC");
        replyDownvoteButton.setFont(new Font("Arial", Font.PLAIN, 12));
        replyDownvoteButton.setFocusPainted(false);
        replyDownvoteButton.setBackground(new Color(245, 245, 245));
        replyDownvoteButton.setOpaque(true);
        replyDownvoteButton.setBorderPainted(false);
        replyDownvoteButton.setContentAreaFilled(true);
        replyDownvoteButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        replyDownvoteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        final JLabel replyVoteCount = new JLabel(String.valueOf(
                reply.getUpvotes() - reply.getDownvotes()));
        replyVoteCount.setFont(new Font("Arial", Font.BOLD, 13));
        replyVoteCount.setForeground(new Color(100, 100, 100));

        final JButton replyButton = new JButton(ReadPostViewModel.REPLY_BUTTON_LABEL);
        replyButton.setFont(new Font("Arial", Font.PLAIN, 12));
        replyButton.setFocusPainted(false);
        replyButton.setBackground(new Color(70, 130, 180));
        replyButton.setForeground(Color.WHITE);
        replyButton.setOpaque(true);
        replyButton.setBorderPainted(false);
        replyButton.setContentAreaFilled(true);
        replyButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 100, 150), 1),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        replyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        actionsPanel.add(replyUpvoteButton);
        actionsPanel.add(replyDownvoteButton);
        actionsPanel.add(replyVoteCount);
        actionsPanel.add(Box.createHorizontalStrut(8));
        actionsPanel.add(replyButton);

        replyUpvoteButton.addActionListener(e -> {
            if (voteController != null) {
                voteController.execute(true, reply.getId());
            }
        });

        replyDownvoteButton.addActionListener(e -> {
            if (voteController != null) {
                voteController.execute(false, reply.getId());
            }
        });

        // Reply box
        final JPanel replyPanel = new JPanel(new BorderLayout());
        replyPanel.setOpaque(false);
        replyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        replyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        replyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        final JTextField replyTextField = new JTextField();
        replyTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        replyTextField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        final JPanel replyActionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        replyActionsPanel.setOpaque(false);
        replyActionsPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        replyActionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        final JButton replyCancelButton = new JButton(ReadPostViewModel.CANCEL_REPLY);
        replyCancelButton.setFont(new Font("Arial", Font.PLAIN, 12));
        replyCancelButton.setFocusPainted(false);
        replyCancelButton.setBackground(new Color(186, 185, 185));
        replyCancelButton.setForeground(Color.WHITE);
        replyCancelButton.setOpaque(true);
        replyCancelButton.setBorderPainted(false);
        replyCancelButton.setContentAreaFilled(true);
        replyCancelButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 100, 150), 1),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        replyCancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        final JButton sendReplyButton = new JButton(ReadPostViewModel.REPLY_BUTTON_LABEL);
        sendReplyButton.setFont(new Font("Arial", Font.PLAIN, 12));
        sendReplyButton.setFocusPainted(false);
        sendReplyButton.setBackground(new Color(70, 130, 180));
        sendReplyButton.setForeground(Color.WHITE);
        sendReplyButton.setOpaque(true);
        sendReplyButton.setBorderPainted(false);
        sendReplyButton.setContentAreaFilled(true);
        sendReplyButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 100, 150), 1),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        sendReplyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        replyActionsPanel.add(replyCancelButton);
        replyActionsPanel.add(sendReplyButton);

        replyPanel.add(replyTextField, BorderLayout.NORTH);
        replyPanel.add(replyActionsPanel, BorderLayout.SOUTH);
        // Initial disabling.
        replyPanel.setVisible(false);

        // Functionality for the buttons
        replyButton.addActionListener(e -> {
            replyPanel.setVisible(true);
        });

        replyCancelButton.addActionListener(e -> {
            if (!replyTextField.getText().isEmpty()) {
                // Prompt a confirmation message if there's a draft.
                int userAnswer = JOptionPane.showConfirmDialog(this, CONFIRM_CANCEL_MESSAGE,
                        CONFIRM_CANCEL_TITLE, JOptionPane.YES_NO_OPTION);

                // Return if the user does not choose yes.
                if (userAnswer != JOptionPane.YES_OPTION) return;
            }

            replyPanel.setVisible(false);
            replyTextField.setText("");
        });

        sendReplyButton.addActionListener(e -> {
            final String replyText = replyTextField.getText();
            final long parentId = reply.getId();
            sendReply(replyText, parentId);
        });

        // Adding everything in
        headerPanel.add(usernameLabel, BorderLayout.WEST);
        panel.add(headerPanel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(replyContent);
        panel.add(Box.createVerticalStrut(10));
        panel.add(commentTranslationPanel);
        panel.add(actionsPanel);

        // Add nested replies directly to panel
        if (!reply.getNestedReplies().isEmpty()) {
            panel.add(Box.createVerticalStrut(12));
            for (ReadPostOutputData.ReplyData nestedReply : reply.getNestedReplies()) {
                final JPanel nestedPanel = createReplyPanel(nestedReply, indentLevel + 1);
                panel.add(nestedPanel);
                panel.add(Box.createVerticalStrut(8));
            }
        }

        return panel;
    }


    /**
     * Loads a post by its ID.
     * This method is responsible for storing the postId, which is required for subsequent
     * actions like translation that are not handled by ReadPostState.
     * @param postId the unique identifier of the post to load
     */
    public void loadPost(long postId) {
        this.currentPostId = postId;

        if (controller != null) {
            controller.execute(postId);
        }
    }

    public String getViewName() {
        return viewModel.getViewName();
    }

    public void setController(ReadPostController controller) {
        this.controller = controller;
    }

    public void setReadPostController(ReadPostController controller) {
        this.controller = controller;
    }

    public void setTranslationController(TranslationController controller) {
        this.translationController = controller;
    }

    public void setOnViewReferencedPostClick(Runnable onViewReferencedPostClick) {
        this.onViewReferencedPostClick = onViewReferencedPostClick;
    }
    
    public void setVoteController(VoteController voteController) {
        this.voteController = voteController;
    }

    public void setReplyController(ReplyPostController replyController) {
        this.replyController = replyController;
    }

    public void setOnBackAction(Runnable onBackAction) {
        this.onBackAction = onBackAction;
    }


    /**
     * Sends a comment/reply
     * @param content The content of the reply
     * @param parentId The id of the reply's parent
     */
    public void sendReply(String content, long parentId) {
        replyController.execute(content, parentId);
    }
    
    /**
     * Creates a panel for displaying a post that references this one.
     */
    private JPanel createReferencingPostPanel(ReadPostOutputData.ReferencingPostData refPost) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 248, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 150), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Left panel with post info
        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(255, 248, 240));
        
        final JLabel titleLabel = new JLabel(refPost.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        titleLabel.setForeground(new Color(50, 50, 50));
        infoPanel.add(titleLabel);
        
        final String contentPreview = refPost.getContent().length() > 80 
                ? refPost.getContent().substring(0, 80) + "..." 
                : refPost.getContent();
        final JLabel contentLabel = new JLabel(contentPreview);
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        contentLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(contentLabel);
        
        final JLabel authorLabel = new JLabel("By: " + refPost.getUsername());
        authorLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        authorLabel.setForeground(new Color(120, 120, 120));
        infoPanel.add(authorLabel);
        
        // Right panel with view button
        final JButton viewButton = new JButton("View");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 11));
        viewButton.setFocusPainted(false);
        viewButton.setBackground(new Color(70, 130, 180));
        viewButton.setForeground(Color.WHITE);
        viewButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        viewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewButton.addActionListener(e -> {
            if (controller != null) {
                loadPost(refPost.getId());
            }
        });
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(viewButton, BorderLayout.EAST);
        
        // Make panel clickable
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (controller != null) {
                    loadPost(refPost.getId());
                }
            }
        });
        
        return panel;
    }

}