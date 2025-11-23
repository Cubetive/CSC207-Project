package view;

import interface_adapter.read_post.ReadPostController;
import interface_adapter.read_post.ReadPostState;
import interface_adapter.read_post.ReadPostViewModel;
import interface_adapter.translate.TranslationController; // NEW
import interface_adapter.translate.TranslationViewModel; // NEW
import interface_adapter.translate.TranslationState;
import use_case.read_post.ReadPostOutputData;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * The View for reading a post and its replies.
 */
public class PostReadingView extends JPanel implements PropertyChangeListener {

    private final ReadPostViewModel viewModel;
    private TranslationViewModel translationViewModel = new TranslationViewModel(); // NEW: Translation ViewModel
    private ReadPostController controller;
    private TranslationController translationController; // FIX: Added missing declaration
    private Runnable onBackAction;
    private long currentPostId = -1; // FIX: Added missing declaration. Stores the current post ID for translation.

    private String textContent = ""; // NEW to keep track of current text.

    // Fields to track translation UI elements for COMMENTS
    private final Map<String, JTextArea> commentTranslationAreas = new HashMap<>();
    private final Map<String, JLabel> commentTranslationStatusLabels = new HashMap<>();
    // FIX 1B: New map to track the dynamically created buttons so they can be re-enabled.
    private final Map<String, JButton> commentTranslationButtons = new HashMap<>();
    // This key (original text content) is used to map the result back to the correct comment UI
    private String lastTextTranslatedKey = null;


    private final JButton backButton;
    private final JLabel titleLabel;
    private final JLabel authorLabel;
    private final JTextArea contentArea;

    // NEW: Translation UI Components
    private final JLabel translateLabel;
    private final JComboBox<String> languageDropdown;
    private final JButton translateButton;
    private JTextArea translatedContentArea;
    private JLabel translationStatusLabel;
    private final JScrollPane translatedContentScrollPane;
    // Supported languages for the dropdown
    private static final String[] SUPPORTED_LANGUAGES = {"en", "es", "fr", "de", "ja"};


    private final JButton upvoteButton;
    private final JButton downvoteButton;
    private final JLabel voteCountLabel;
    private final JTextField commentField;
    private final JButton commentButton;
    private final JPanel repliesPanel;
    private final JScrollPane scrollPane;

    public PostReadingView(ReadPostViewModel viewModel, TranslationViewModel translationViewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        this.translationViewModel = translationViewModel;

        // FIX: Register the view as a listener for Translation updates
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

        // --- NEW: Translation Controls and Display ---

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

        // Hardcoding languages, assuming TranslationViewModel.SUPPORTED_LANGUAGES exists
        final String[] languages = {"en", "es", "fr", "de", "ja"};
        languageDropdown = new JComboBox<>(languages);
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
            if (textContent.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cannot translate. Controller or Post ID is missing.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            translateButton.setEnabled(false);
            translationStatusLabel.setText("Translating...");
            translatedContentArea.setText("Loading translation...");

            String targetLanguage = (String) languageDropdown.getSelectedItem();
            if (!textContent.trim().isEmpty()) { //Original: targetLanguage != null
                // Call the translation controller
                translationController.execute(textContent, targetLanguage);
            }
        });

        controlPanel.add(translateLabel);
        controlPanel.add(languageDropdown);
        controlPanel.add(translateButton);
        translationPanel.add(controlPanel);

        // Translation Status and Content Area
        translationStatusLabel = new JLabel("Select a language and click Translate."); // Initialization
        translationStatusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        translationStatusLabel.setForeground(new Color(150, 150, 150));
        translationStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        translationPanel.add(translationStatusLabel);
        translationPanel.add(Box.createVerticalStrut(5));

        translatedContentArea = new JTextArea(4, 40); // Initialization
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

        // --- End Translation UI ---

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

        // Replies panel
        repliesPanel = new JPanel();
        repliesPanel.setLayout(new BoxLayout(repliesPanel, BoxLayout.Y_AXIS));
        repliesPanel.setBackground(new Color(245, 245, 245));

        // Add components to main panel
        mainPanel.add(contentContainer);
        // INSERTING TRANSLATION PANEL HERE
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

//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        if ("state".equals(evt.getPropertyName())) {
//            final ReadPostState state = (ReadPostState) evt.getNewValue();
//            updateView(state);
//        }
//    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == viewModel) {
            if ("state".equals(evt.getPropertyName())) {
                final ReadPostState state = (ReadPostState) evt.getNewValue();
                updateView(state);
            }
        } else if (evt.getSource() == translationViewModel) {
            if ("state".equals(evt.getPropertyName())) {
                final TranslationState state = (TranslationState) evt.getNewValue();
                handleTranslationChange(state);
            }
        }
    }


    /**
     * Handles updates from the TranslationViewModel.
     */
    private void handleTranslationChange(TranslationState state) {
        // Find which component to update: Main Post or a Comment
        if (lastTextTranslatedKey == null) {
            // --- UPDATE MAIN POST TRANSLATION UI ---
            if (translateButton != null) {
                translateButton.setEnabled(true); // Re-enable button
            }

            if (state.isTranslationSuccessful()) {
                if (translatedContentArea != null) {
                    translatedContentArea.setText(state.getTranslatedText());
                }
                if (translationStatusLabel != null) {
                    String cacheIndicator = state.isFromCache() ? " (Cached)" : " (API)";
                    translationStatusLabel.setText(
                            String.format("Translated to %s%s. %s",
                                    state.getTargetLanguage().toUpperCase(),
                                    cacheIndicator,
                                    state.getStatusMessage()
                            )
                    );
                }
            } else {
                if (translatedContentArea != null) {
                    translatedContentArea.setText("Translation unavailable.");
                }
                if (translationStatusLabel != null) {
                    translationStatusLabel.setText(state.getStatusMessage());
                }
            }
            // FIX: MANDATORY REPAINT CALLS (Add this block here for the main post)
            if (translatedContentArea != null) {
                translatedContentArea.revalidate();
                translatedContentArea.repaint();

                // CRITICAL FIX: Repaint the Scroll Pane that contains the text area
                translatedContentScrollPane.revalidate(); // Add this line
                translatedContentScrollPane.repaint();   // Add this line
            }
        } else {
            // --- UPDATE COMMENT TRANSLATION UI ---

            // Get the specific components for the last requested comment
            JTextArea commentArea = commentTranslationAreas.get(lastTextTranslatedKey);
            JLabel commentStatus = commentTranslationStatusLabels.get(lastTextTranslatedKey);
            // FIX 2: Retrieve the button reference
            JButton commentButton = commentTranslationButtons.get(lastTextTranslatedKey);

            if (commentArea != null && commentStatus != null) {
                if (commentButton != null) {
                    commentButton.setEnabled(true);
                }
                if (state.isTranslationSuccessful()) {
                    commentArea.setText(state.getTranslatedText());
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
                // FIX: MANDATORY REPAINT CALLS (Add this block here for the comment area)
                commentArea.revalidate();
                commentArea.repaint();
            }
            // FIX: Ensure the main scroll pane updates its size/layout if necessary
            scrollPane.revalidate();
            scrollPane.repaint();
            repliesPanel.revalidate();
            repliesPanel.repaint();
            // Reset the comment tracker after receiving a result
            lastTextTranslatedKey = null;
        }
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
        // FIX 3: Clear the button map as well
        commentTranslationButtons.clear();
        // The individual reply panels will be recreated and refreshed by updateView/repliesPanel.removeAll()
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

        // NEW: If a new post loaded, set the currentPostId.
        // Assuming getPostId() returns a String ID.
        this.currentPostId = state.getId();
        textContent = state.getContent();

        // NEW Clear previous translation when a new post is loaded
        clearTranslationDisplay();
        clearCommentTranslationDisplays();

        setTranslationController(translationController);

        // FIX: Update currentPostId when a post is successfully loaded
        // Assuming ReadPostState has a getPostId() method:
        titleLabel.setText(state.getTitle());
        authorLabel.setText(state.getUsername());
        contentArea.setText(state.getContent());
        voteCountLabel.setText(String.valueOf(state.getUpvotes() - state.getDownvotes()));

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
        final String commentKey = reply.getContent();

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

        // --- NEW: Comment Translation Controls and Display ---
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
        // FIX 5: Store the button reference
        commentTranslationButtons.put(commentKey, commentTranslateButton);

        // Action Listener for Comment Translation
        commentTranslateButton.addActionListener(e -> {
            if (textContent.isEmpty()) {
                commentTranslationStatusLabel.setText("Error: Translation controller is missing.");
                return;
            }

            commentTranslateButton.setEnabled(false);
            commentTranslationStatusLabel.setText("Translating...");
            translatedReplyContentArea.setText("Loading translation...");

            // Set the tracking key to this comment's content
            lastTextTranslatedKey = commentKey;

            String targetLanguage = (String) commentLanguageDropdown.getSelectedItem();
            if (!textContent.trim().isEmpty()) { // Originally targetLanguage != null
                // Call the translation controller with the comment's raw text
                // Assumption: TranslationController has an overload for (String text, String targetLang)
                translationController.execute(reply.getContent(), targetLanguage);
            }
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
        // --- End Comment Translation UI ---

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

        headerPanel.add(usernameLabel, BorderLayout.WEST);
        panel.add(headerPanel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(replyContent);
        panel.add(Box.createVerticalStrut(10));
        panel.add(commentTranslationPanel); // The entire, grouped translation UI
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
        // CORRECTED FIX: Store the postId here, as ReadPostState does not provide a getter for it.
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

    // --- Controller Setters (FIX: Added these missing setters) ---
    public void setReadPostController(ReadPostController controller) {
        this.controller = controller;
    }
    // NEW: Setter for the TranslationController
    public void setTranslationController(TranslationController controller) {
        this.translationController = controller;
    }

    public void setOnBackAction(Runnable onBackAction) {
        this.onBackAction = onBackAction;
    }

}
