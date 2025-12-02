package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import entities.User;
import interface_adapter.read_post.ReadPostController;
import interface_adapter.read_post.ReadPostState;
import interface_adapter.read_post.ReadPostViewModel;
import interface_adapter.reply_post.ReplyPostController;
import interface_adapter.reply_post.ReplyPostPresenter;
import interface_adapter.translate.TranslationController;
import interface_adapter.translate.TranslationState;
import interface_adapter.translate.TranslationViewModel;
import interface_adapter.upvote_downvote.VoteController;
import use_case.read_post.ReadPostOutputData;

/**
 * The View for reading a post and its replies.
 */
public class PostReadingView extends JPanel implements PropertyChangeListener {

    // Color constants
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color WHITE_COLOR = Color.WHITE;
    private static final Color BORDER_LIGHT = new Color(220, 220, 220);
    private static final Color HEADER_COLOR = new Color(70, 130, 180);
    private static final Color BORDER_DARK = new Color(50, 100, 150);
    private static final Color DARK_TEXT = new Color(50, 50, 50);
    private static final Color AUTHOR_TEXT = new Color(100, 100, 100);
    private static final Color LIGHT_BLUE_BG = new Color(240, 248, 255);
    private static final Color TRANSLATE_BG = new Color(173, 216, 230);
    private static final Color TRANSLATE_BORDER = new Color(135, 206, 250);
    private static final Color VOTE_BG = new Color(240, 240, 240);
    private static final Color VOTE_BORDER = new Color(200, 200, 200);
    private static final Color STATUS_TEXT = new Color(150, 150, 150);
    private static final Color TRANSLATED_BG = new Color(230, 230, 235);
    private static final Color TRANSLATED_BORDER = new Color(200, 200, 200);
    private static final Color REPLY_CONTENT_COLOR = new Color(60, 60, 60);
    private static final Color COMMENT_TRANSLATE_BG = new Color(200, 220, 240);
    private static final Color COMMENT_TRANSLATED_BG = new Color(240, 240, 245);
    private static final Color CANCEL_BUTTON_BG = new Color(186, 185, 185);
    private static final Color REF_ORANGE_BG = new Color(255, 248, 240);
    private static final Color REF_ORANGE_BORDER = new Color(255, 200, 150);
    private static final Color MUTED_TEXT = new Color(120, 120, 120);
    private static final Color CONTENT_TEXT = new Color(80, 80, 80);

    // Size constants
    private static final int PADDING_SMALL = 5;
    private static final int PADDING_MEDIUM = 10;
    private static final int PADDING_LARGE = 15;
    private static final int PADDING_XLARGE = 20;
    private static final int SCROLL_INCREMENT = 16;
    private static final int BORDER_MATTE = 2;
    private static final int BORDER_ACCENT = 4;
    private static final int INDENT_PER_LEVEL = 15;
    private static final int CONTENT_PREVIEW_LENGTH = 80;

    // Font size constants
    private static final int FONT_SMALL = 11;
    private static final int FONT_MEDIUM = 12;
    private static final int FONT_REGULAR = 13;
    private static final int FONT_CONTENT = 14;
    private static final int FONT_LARGE = 15;
    private static final int FONT_VOTE = 16;
    private static final int FONT_COMMENTS = 18;
    private static final int FONT_TITLE = 22;

    // Font name constant
    private static final String FONT_ARIAL = "Arial";

    // String constants
    private static final String MAIN_POST_KEY = "MAIN_POST";
    private static final String CONFIRM_CANCEL_MESSAGE = "You have unsaved changes. Are you sure you want to cancel?";
    private static final String CONFIRM_CANCEL_TITLE = "Confirm Cancel";
    private static final String TRANSLATE_STATUS_DEFAULT = "Select a language and click Translate.";
    private static final String LOADING_TRANSLATION = "Loading translation...";
    private static final String TRANSLATING = "Translating...";
    private static final String REF_LINK_TEXT = "[Link] This post references:";

    // Supported languages for the dropdown
    private static final String DEFAULT_LANGUAGE = "es";
    private static final String[] SUPPORTED_LANGUAGES = {
        "ar", "cn", "en", DEFAULT_LANGUAGE, "fr", "de", "hi", "it", "ja", "ko", "ru",
    };

    private static final int ROWS_THREE = 3;
    private static final int ROWS_FOUR = 4;
    private static final int COLS_THIRTY = 30;
    private static final int COLS_FORTY = 40;
    private static final int MAX_HEIGHT_50 = 50;
    private static final int MAX_HEIGHT_100 = 100;
    private static final int MAX_HEIGHT_150 = 150;
    private static final int MAX_HEIGHT_200 = 200;
    private static final int MAX_HEIGHT_300 = 300;
    private static final int EDIT_BTN_WIDTH = 120;
    private static final int EDIT_BTN_HEIGHT = 40;
    private static final int REF_PANEL_HEIGHT = 60;

    private final ReadPostViewModel viewModel;
    private TranslationViewModel translationViewModel;
    private ReadPostController controller;
    private ReplyPostController replyController;
    private VoteController voteController;
    private TranslationController translationController;
    private Runnable onBackAction;
    private long currentPostId = 1;
    private String textContent = "";

    // Fields to track translation UI elements for COMMENTS
    private final Map<String, JTextArea> commentTranslationAreas = new ConcurrentHashMap<>();
    private final Map<String, JLabel> commentTranslationStatusLabels = new ConcurrentHashMap<>();
    private final Map<String, JButton> commentTranslationButtons = new ConcurrentHashMap<>();
    private String lastTextTranslatedKey;
    private final Set<String> translationsInProgress = ConcurrentHashMap.newKeySet();

    private final JLabel titleLabel;
    private final JLabel authorLabel;
    private final JTextArea contentArea;

    // Translation UI Components
    private final JComboBox<String> languageDropdown;
    private final JButton translateButton;
    private JTextArea translatedContentArea;
    private JLabel translationStatusLabel;
    private final JScrollPane translatedContentScrollPane;

    private final JButton upvoteButton;
    private final JButton downvoteButton;
    private final JLabel voteCountLabel;
    private final JTextField commentField;
    private final JPanel repliesPanel;
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

    private User curUser;
    private JButton editButton;

    /**
     * Creates a new PostReadingView.
     *
     * @param readPostViewModel the view model for reading posts
     * @param translationVm the view model for translations
     */
    public PostReadingView(ReadPostViewModel readPostViewModel, TranslationViewModel translationVm) {
        this.viewModel = readPostViewModel;
        this.viewModel.addPropertyChangeListener(this);
        this.translationViewModel = translationVm;
        this.translationViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBackground(BACKGROUND_COLOR);

        // Initialize components
        titleLabel = createTitleLabel();
        authorLabel = createAuthorLabel();
        contentArea = createContentArea();
        languageDropdown = new JComboBox<>(SUPPORTED_LANGUAGES);
        languageDropdown.setSelectedItem(DEFAULT_LANGUAGE);
        translateButton = createTranslateButton();
        translatedContentArea = createTranslatedContentArea();
        translationStatusLabel = createTranslationStatusLabel();
        translatedContentScrollPane = createTranslatedContentScrollPane();
        upvoteButton = createVoteButton("\u25B2");
        downvoteButton = createVoteButton("\u25BC");
        voteCountLabel = createVoteCountLabel();
        commentField = createCommentField();
        repliesPanel = createRepliesPanel();
        referencedPostContainer = createReferencedPostContainer();
        referencedPostTitleLabel = createReferencedPostTitleLabel();
        referencedPostContentArea = createReferencedPostContentArea();
        referencedPostAuthorLabel = createReferencedPostAuthorLabel();
        viewReferencedPostButton = createViewReferencedPostButton();
        referencingPostsContainer = createReferencingPostsContainer();
        referencingPostsListPanel = createReferencingPostsListPanel();
        referenceBannerPanel = createReferenceBannerPanel();
        referenceBannerButton = createReferenceBannerButton();
        editButton = createEditButton();

        setupUserInterface();
    }

    private void setupUserInterface() {
        final JPanel topPanel = createTopPanel();
        final JPanel mainPanel = createMainPanel();
        final JScrollPane scrollPane = createScrollPane(mainPanel);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        final JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(WHITE_COLOR);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, BORDER_MATTE, 0, BORDER_LIGHT),
                BorderFactory.createEmptyBorder(PADDING_LARGE, PADDING_XLARGE, PADDING_LARGE, PADDING_XLARGE)
        ));

        final JButton backButton = createBackButton();
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(authorLabel, BorderLayout.EAST);

        return topPanel;
    }

    private JButton createBackButton() {
        final JButton backButton = new JButton(ReadPostViewModel.BACK_BUTTON_LABEL);
        backButton.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_CONTENT));
        backButton.setFocusPainted(false);
        backButton.setBackground(HEADER_COLOR);
        backButton.setForeground(WHITE_COLOR);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(true);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 1),
                BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_XLARGE, PADDING_MEDIUM, PADDING_XLARGE)
        ));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(evt -> {
            if (onBackAction != null) {
                onBackAction.run();
            }
        });
        return backButton;
    }

    private JLabel createTitleLabel() {
        final JLabel label = new JLabel();
        label.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_TITLE));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(DARK_TEXT);
        return label;
    }

    private JLabel createAuthorLabel() {
        final JLabel label = new JLabel();
        label.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_CONTENT));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setForeground(AUTHOR_TEXT);
        return label;
    }

    private JTextArea createContentArea() {
        final JTextArea area = new JTextArea();
        area.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_LARGE));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBackground(WHITE_COLOR);
        area.setForeground(DARK_TEXT);
        return area;
    }

    private JButton createTranslateButton() {
        final JButton button = new JButton("Translate Post");
        button.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_CONTENT));
        button.setFocusPainted(false);
        button.setBackground(TRANSLATE_BG);
        button.setForeground(DARK_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TRANSLATE_BORDER, 1),
                BorderFactory.createEmptyBorder(PADDING_SMALL, PADDING_LARGE, PADDING_SMALL, PADDING_LARGE)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(evt -> handleTranslateButtonClick());
        return button;
    }

    private void handleTranslateButtonClick() {
        if (translationController == null) {
            JOptionPane.showMessageDialog(this, "Translation service is not configured.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        translateButton.setEnabled(false);
        translationStatusLabel.setText(TRANSLATING);
        translatedContentArea.setText(LOADING_TRANSLATION);

        translationsInProgress.add(MAIN_POST_KEY);

        final String targetLanguage = (String) languageDropdown.getSelectedItem();
        final long postIdToTranslate = currentPostId;
        final String contentToTranslate = textContent;

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                translationController.execute(postIdToTranslate, contentToTranslate, targetLanguage);
                return null;
            }

            @Override
            protected void done() {
                translationViewModel.firePropertyChanged();
            }
        }.execute();
    }

    private JTextArea createTranslatedContentArea() {
        final JTextArea area = new JTextArea(ROWS_FOUR, COLS_FORTY);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_LARGE));
        area.setBackground(TRANSLATED_BG);
        area.setForeground(DARK_TEXT);
        return area;
    }

    private JLabel createTranslationStatusLabel() {
        final JLabel label = new JLabel(TRANSLATE_STATUS_DEFAULT);
        label.setFont(new Font(FONT_ARIAL, Font.ITALIC, FONT_MEDIUM));
        label.setForeground(STATUS_TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JScrollPane createTranslatedContentScrollPane() {
        final JScrollPane scrollPane = new JScrollPane(translatedContentArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TRANSLATED_BORDER, 1),
                BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM)
        ));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_HEIGHT_200));
        return scrollPane;
    }

    private JButton createVoteButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_VOTE));
        button.setFocusPainted(false);
        button.setBackground(VOTE_BG);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VOTE_BORDER, 1),
                BorderFactory.createEmptyBorder(PADDING_SMALL, FONT_MEDIUM, PADDING_SMALL, FONT_MEDIUM)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel createVoteCountLabel() {
        final JLabel label = new JLabel("0");
        label.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_VOTE));
        label.setForeground(AUTHOR_TEXT);
        return label;
    }

    private JTextField createCommentField() {
        final JTextField field = new JTextField();
        field.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_CONTENT));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VOTE_BORDER, 1),
                BorderFactory.createEmptyBorder(PADDING_MEDIUM, FONT_MEDIUM, PADDING_MEDIUM, FONT_MEDIUM)
        ));
        return field;
    }

    private JPanel createRepliesPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    private JButton createEditButton() {
        final JButton button = new JButton("Edit Post");
        button.setPreferredSize(new Dimension(EDIT_BTN_WIDTH, EDIT_BTN_HEIGHT));
        return button;
    }

    private JPanel createReferencedPostContainer() {
        final JPanel container = new JPanel(new BorderLayout());
        container.setBackground(LIGHT_BLUE_BG);
        container.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Referenced Post"),
                BorderFactory.createEmptyBorder(PADDING_LARGE, PADDING_LARGE, PADDING_LARGE, PADDING_LARGE)
        ));
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_HEIGHT_200));
        container.setVisible(false);
        return container;
    }

    private JLabel createReferencedPostTitleLabel() {
        final JLabel label = new JLabel();
        label.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_CONTENT));
        label.setForeground(DARK_TEXT);
        return label;
    }

    private JTextArea createReferencedPostContentArea() {
        final JTextArea area = new JTextArea(ROWS_THREE, COLS_THIRTY);
        area.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_REGULAR));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBackground(LIGHT_BLUE_BG);
        area.setForeground(CONTENT_TEXT);
        return area;
    }

    private JLabel createReferencedPostAuthorLabel() {
        final JLabel label = new JLabel();
        label.setFont(new Font(FONT_ARIAL, Font.ITALIC, FONT_MEDIUM));
        label.setForeground(MUTED_TEXT);
        return label;
    }

    private JButton createViewReferencedPostButton() {
        final JButton button = new JButton("View Referenced Post");
        button.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_MEDIUM));
        button.setFocusPainted(false);
        button.setBackground(HEADER_COLOR);
        button.setForeground(WHITE_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(PADDING_SMALL, PADDING_LARGE, PADDING_SMALL, PADDING_LARGE));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createReferencingPostsContainer() {
        final JPanel container = new JPanel(new BorderLayout());
        container.setBackground(REF_ORANGE_BG);
        container.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Referenced By"),
                BorderFactory.createEmptyBorder(PADDING_LARGE, PADDING_LARGE, PADDING_LARGE, PADDING_LARGE)
        ));
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_HEIGHT_200));
        container.setVisible(false);
        return container;
    }

    private JPanel createReferencingPostsListPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(REF_ORANGE_BG);
        return panel;
    }

    private JPanel createReferenceBannerPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING_MEDIUM, PADDING_SMALL));
        panel.setBackground(LIGHT_BLUE_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, BORDER_ACCENT, 1, 1, HEADER_COLOR),
                BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_LARGE, PADDING_MEDIUM, PADDING_LARGE)
        ));
        panel.setVisible(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_HEIGHT_50));
        return panel;
    }

    private JButton createReferenceBannerButton() {
        final JButton button = new JButton();
        button.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_REGULAR));
        button.setForeground(HEADER_COLOR);
        button.setBackground(LIGHT_BLUE_BG);
        button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(evt -> handleReferenceBannerClick());
        addHoverEffect(button);
        return button;
    }

    private void handleReferenceBannerClick() {
        if (onViewReferencedPostClick != null) {
            onViewReferencedPostClick.run();
        }
        else if (controller != null) {
            final ReadPostState readPostState = viewModel.getState();
            if (readPostState.getReferencedPost() != null) {
                loadPost(readPostState.getReferencedPost().getId());
            }
        }
    }

    private void addHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(BORDER_DARK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(HEADER_COLOR);
            }
        });
    }

    private JPanel createMainPanel() {
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_XLARGE, PADDING_XLARGE,
                PADDING_XLARGE, PADDING_XLARGE));

        setupReferenceBanner();
        setupContentContainer(mainPanel);
        setupReferencedPostContainer();
        setupReferencingPostsContainer();
        setupTranslationPanel(mainPanel);
        setupVotePanel(mainPanel);
        setupCommentSection(mainPanel);

        return mainPanel;
    }

    private void setupReferenceBanner() {
        final JLabel referenceBannerLabel = new JLabel(REF_LINK_TEXT);
        referenceBannerLabel.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_REGULAR));
        referenceBannerLabel.setForeground(DARK_TEXT);

        referenceBannerPanel.add(referenceBannerLabel);
        referenceBannerPanel.add(referenceBannerButton);
    }

    private void setupContentContainer(JPanel mainPanel) {
        final JPanel contentContainer = new JPanel(new BorderLayout());
        contentContainer.setBackground(WHITE_COLOR);
        contentContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                BorderFactory.createEmptyBorder(PADDING_XLARGE, PADDING_XLARGE, PADDING_XLARGE, PADDING_XLARGE)
        ));
        contentContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_HEIGHT_300));
        contentContainer.add(contentArea, BorderLayout.CENTER);

        mainPanel.add(referenceBannerPanel);
        mainPanel.add(Box.createVerticalStrut(PADDING_MEDIUM));
        mainPanel.add(contentContainer);
        mainPanel.add(Box.createVerticalStrut(PADDING_MEDIUM));
        mainPanel.add(referencedPostContainer);
        mainPanel.add(Box.createVerticalStrut(PADDING_MEDIUM));
        mainPanel.add(referencingPostsContainer);
    }

    private void setupReferencedPostContainer() {
        final JPanel referencedPostInfoPanel = new JPanel();
        referencedPostInfoPanel.setLayout(new BoxLayout(referencedPostInfoPanel, BoxLayout.Y_AXIS));
        referencedPostInfoPanel.setBackground(LIGHT_BLUE_BG);
        referencedPostInfoPanel.add(referencedPostTitleLabel);
        referencedPostInfoPanel.add(Box.createVerticalStrut(PADDING_SMALL));
        referencedPostInfoPanel.add(new JScrollPane(referencedPostContentArea));
        referencedPostInfoPanel.add(Box.createVerticalStrut(PADDING_SMALL));
        referencedPostInfoPanel.add(referencedPostAuthorLabel);

        referencedPostContainer.add(referencedPostInfoPanel, BorderLayout.CENTER);
        referencedPostContainer.add(viewReferencedPostButton, BorderLayout.EAST);
    }

    private void setupReferencingPostsContainer() {
        final JScrollPane referencingPostsScrollPane = new JScrollPane(referencingPostsListPanel);
        referencingPostsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        referencingPostsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        referencingPostsScrollPane.setBorder(null);
        referencingPostsScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_HEIGHT_150));

        referencingPostsContainer.add(referencingPostsScrollPane, BorderLayout.CENTER);
    }

    private void setupTranslationPanel(JPanel mainPanel) {
        final JPanel translationPanel = new JPanel();
        translationPanel.setLayout(new BoxLayout(translationPanel, BoxLayout.Y_AXIS));
        translationPanel.setBackground(BACKGROUND_COLOR);
        translationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        translationPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_LARGE, 0, PADDING_LARGE, 0));

        final JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING_MEDIUM, PADDING_SMALL));
        controlPanel.setOpaque(false);
        controlPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel translateLabel = new JLabel("Translate to:");
        translateLabel.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_CONTENT));

        controlPanel.add(translateLabel);
        controlPanel.add(languageDropdown);
        controlPanel.add(translateButton);
        controlPanel.add(editButton);
        translationPanel.add(controlPanel);

        translationPanel.add(translationStatusLabel);
        translationPanel.add(Box.createVerticalStrut(PADDING_SMALL));
        translationPanel.add(translatedContentScrollPane);

        mainPanel.add(translationPanel);
    }

    private void setupVotePanel(JPanel mainPanel) {
        final JPanel votePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING_MEDIUM, PADDING_MEDIUM));
        votePanel.setBackground(BACKGROUND_COLOR);

        upvoteButton.addActionListener(evt -> {
            if (voteController != null) {
                voteController.execute(true, currentPostId);
            }
        });

        downvoteButton.addActionListener(evt -> {
            if (voteController != null) {
                voteController.execute(false, currentPostId);
            }
        });

        votePanel.add(upvoteButton);
        votePanel.add(downvoteButton);
        votePanel.add(voteCountLabel);

        mainPanel.add(Box.createVerticalStrut(PADDING_LARGE));
        mainPanel.add(votePanel);
    }

    private void setupCommentSection(JPanel mainPanel) {
        final JLabel commentsLabel = new JLabel(ReadPostViewModel.COMMENTS_LABEL);
        commentsLabel.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_COMMENTS));
        commentsLabel.setForeground(DARK_TEXT);
        commentsLabel.setBorder(BorderFactory.createEmptyBorder(PADDING_XLARGE, 0, PADDING_MEDIUM, 0));
        commentsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JPanel commentInputPanel = new JPanel(new BorderLayout(PADDING_MEDIUM, 0));
        commentInputPanel.setBackground(BACKGROUND_COLOR);
        commentInputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_HEIGHT_50));

        final JButton commentButton = createCommentButton();
        commentButton.addActionListener(evt -> {
            if (replyController != null) {
                final String commentText = commentField.getText();
                sendReply(commentText, currentPostId);
            }
        });

        commentInputPanel.add(commentField, BorderLayout.CENTER);
        commentInputPanel.add(commentButton, BorderLayout.EAST);

        mainPanel.add(Box.createVerticalStrut(PADDING_SMALL));
        mainPanel.add(commentsLabel);
        mainPanel.add(Box.createVerticalStrut(PADDING_MEDIUM));
        mainPanel.add(commentInputPanel);
        mainPanel.add(Box.createVerticalStrut(PADDING_LARGE));
        mainPanel.add(repliesPanel);
    }

    private JButton createCommentButton() {
        final JButton button = new JButton(ReadPostViewModel.COMMENT_BUTTON_LABEL);
        button.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_CONTENT));
        button.setFocusPainted(false);
        button.setBackground(HEADER_COLOR);
        button.setForeground(WHITE_COLOR);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 1),
                BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_XLARGE, PADDING_MEDIUM, PADDING_XLARGE)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JScrollPane createScrollPane(JPanel mainPanel) {
        final JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        return scrollPane;
    }

    /**
     * Loads the current user data for permission checks.
     *
     * @param currentUser the current logged-in user
     */
    public void loadUserData(User currentUser) {
        this.curUser = currentUser;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == viewModel && "state".equals(evt.getPropertyName())) {
            final ReadPostState state = (ReadPostState) evt.getNewValue();
            updateView(state);
        }
        else if (TranslationViewModel.STATE_PROPERTY_NAME.equals(evt.getPropertyName())) {
            final TranslationState state = (TranslationState) evt.getNewValue();
            System.out.println("DEBUG: View received translation event! Success: " + state.isTranslationSuccessful());
            SwingUtilities.invokeLater(() -> handleTranslationChange(state));
        }

        if (ReplyPostPresenter.REPLY_SUCCESS.equals(evt.getPropertyName())) {
            commentField.setText("");
            loadPost(viewModel.getState().getId());
        }
    }

    private void handleTranslationChange(TranslationState state) {
        if (lastTextTranslatedKey == null) {
            handleMainPostTranslation(state);
        }
        else {
            handleCommentTranslation(state);
        }

        refreshView();
    }

    private void handleMainPostTranslation(TranslationState state) {
        if (translateButton != null) {
            translateButton.setEnabled(true);
        }

        if (translatedContentArea != null && translationStatusLabel != null) {
            if (state.isTranslationSuccessful()) {
                translatedContentArea.setText(
                        state.getTranslatedText() != null ? state.getTranslatedText() : "");
                final String cacheIndicator = state.isFromCache() ? " (Cached)" : " (API)";
                translationStatusLabel.setText(
                        String.format("Translated to %s%s. %s",
                                state.getTargetLanguage().toUpperCase(),
                                cacheIndicator,
                                state.getStatusMessage()
                        )
                );
            }
            else {
                translatedContentArea.setText("Translation unavailable.");
                translationStatusLabel.setText(state.getStatusMessage());
            }
            if (translatedContentScrollPane != null) {
                translatedContentScrollPane.revalidate();
                translatedContentScrollPane.repaint();
            }
            translationsInProgress.remove(MAIN_POST_KEY);
        }
    }

    private void handleCommentTranslation(TranslationState state) {
        final String lookupKey = lastTextTranslatedKey.trim();

        final JTextArea commentArea = commentTranslationAreas.get(lookupKey);
        final JLabel commentStatus = commentTranslationStatusLabels.get(lookupKey);
        final JButton commentBtn = commentTranslationButtons.get(lookupKey);

        if (commentBtn != null) {
            commentBtn.setEnabled(true);
        }

        if (commentArea != null && commentStatus != null) {
            if (state.isTranslationSuccessful()) {
                final String text = state.getTranslatedText() != null ? state.getTranslatedText() : "";
                commentArea.setText(text);

                final String cacheIndicator = state.isFromCache() ? " (Cached)" : " (API)";
                commentStatus.setText(
                        String.format("Translated to %s%s. %s",
                                state.getTargetLanguage().toUpperCase(),
                                cacheIndicator,
                                state.getStatusMessage()
                        )
                );
            }
            else {
                commentArea.setText("Translation unavailable.");
                commentStatus.setText(state.getStatusMessage());
            }

            refreshCommentArea(commentArea);
        }

        if (repliesPanel != null) {
            repliesPanel.revalidate();
            repliesPanel.repaint();
        }

        translationsInProgress.remove(lookupKey);
        lastTextTranslatedKey = null;
    }

    private void refreshCommentArea(JTextArea commentArea) {
        commentArea.setPreferredSize(null);
        commentArea.setSize(commentArea.getPreferredSize());

        final JScrollPane parentScrollPane = (JScrollPane)
                SwingUtilities.getAncestorOfClass(JScrollPane.class, commentArea);
        if (parentScrollPane != null) {
            parentScrollPane.setViewportView(commentArea);
            parentScrollPane.revalidate();
            parentScrollPane.repaint();
        }

        commentArea.revalidate();
        commentArea.repaint();
    }

    private void refreshView() {
        if (this.getParent() != null) {
            this.getParent().revalidate();
            this.getParent().repaint();
        }
        this.invalidate();
        this.revalidate();
        this.repaint();
    }

    private void clearTranslationDisplay() {
        if (translatedContentArea != null) {
            translatedContentArea.setText("");
        }
        if (translationStatusLabel != null) {
            translationStatusLabel.setText(TRANSLATE_STATUS_DEFAULT);
        }
        if (translateButton != null) {
            translateButton.setEnabled(true);
        }
    }

    private void clearCommentTranslationDisplays() {
        commentTranslationAreas.clear();
        commentTranslationStatusLabels.clear();
        commentTranslationButtons.clear();
    }

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

        updateReferencedPostDisplay(state);
        updateReferencingPostsDisplay(state);
        updateEditButton(state);
        updateRepliesPanel(state);
    }

    private void updateReferencedPostDisplay(ReadPostState state) {
        final ReadPostOutputData.ReferencedPostData referencedPost = state.getReferencedPost();
        if (referencedPost != null) {
            final String referencedTitle = referencedPost.getTitle() != null
                    && !referencedPost.getTitle().isEmpty()
                    ? referencedPost.getTitle()
                    : "Original Post";
            referenceBannerButton.setText(referencedTitle);
            referenceBannerPanel.setVisible(true);
            referenceBannerPanel.revalidate();
            referenceBannerPanel.repaint();

            referencedPostTitleLabel.setText(
                    referencedPost.getTitle().isEmpty() ? "Referenced Post" : referencedPost.getTitle());
            referencedPostContentArea.setText(referencedPost.getContent());
            referencedPostAuthorLabel.setText("By: " + referencedPost.getUsername());
            referencedPostContainer.setVisible(true);

            for (java.awt.event.ActionListener al : viewReferencedPostButton.getActionListeners()) {
                viewReferencedPostButton.removeActionListener(al);
            }
            viewReferencedPostButton.addActionListener(evt -> {
                if (onViewReferencedPostClick != null) {
                    onViewReferencedPostClick.run();
                }
                else if (controller != null) {
                    loadPost(referencedPost.getId());
                }
            });
        }
        else {
            referenceBannerPanel.setVisible(false);
            referencedPostContainer.setVisible(false);
        }
    }

    private void updateReferencingPostsDisplay(ReadPostState state) {
        final List<ReadPostOutputData.ReferencingPostData> referencingPosts = state.getReferencingPosts();
        referencingPostsListPanel.removeAll();
        if (referencingPosts != null && !referencingPosts.isEmpty()) {
            referencingPostsContainer.setVisible(true);
            for (ReadPostOutputData.ReferencingPostData refPost : referencingPosts) {
                final JPanel refPostPanel = createReferencingPostPanel(refPost);
                referencingPostsListPanel.add(refPostPanel);
                referencingPostsListPanel.add(Box.createVerticalStrut(PADDING_SMALL));
            }
        }
        else {
            referencingPostsContainer.setVisible(false);
        }
        referencingPostsListPanel.revalidate();
        referencingPostsListPanel.repaint();
    }

    private void updateEditButton(ReadPostState state) {
        if (curUser != null && curUser.getUsername().equals(state.getUsername())) {
            editButton.setVisible(true);
            for (java.awt.event.ActionListener listener : editButton.getActionListeners()) {
                editButton.removeActionListener(listener);
            }
            editButton.addActionListener(evt -> new EditPostView(contentArea, state, curUser));
        }
        else {
            editButton.setVisible(false);
            for (java.awt.event.ActionListener listener : editButton.getActionListeners()) {
                editButton.removeActionListener(listener);
            }
        }
    }

    private void updateRepliesPanel(ReadPostState state) {
        repliesPanel.removeAll();
        if (!state.getReplies().isEmpty()) {
            for (ReadPostOutputData.ReplyData reply : state.getReplies()) {
                final JPanel replyPanel = createReplyPanel(reply, 0);

                final JPanel fullWidthWrapper = new JPanel(new BorderLayout());
                fullWidthWrapper.setBackground(BACKGROUND_COLOR);
                fullWidthWrapper.add(replyPanel, BorderLayout.CENTER);
                fullWidthWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

                repliesPanel.add(fullWidthWrapper);
                repliesPanel.add(Box.createVerticalStrut(PADDING_SMALL));
            }
        }

        repliesPanel.revalidate();
        repliesPanel.repaint();
    }

    /**
     * Creates a panel for displaying a single reply.
     *
     * @param reply the reply data
     * @param indentLevel the indentation level for nested replies
     * @return the created reply panel
     */
    private JPanel createReplyPanel(ReadPostOutputData.ReplyData reply, int indentLevel) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE_COLOR);

        final int leftIndent = indentLevel * INDENT_PER_LEVEL;
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                BorderFactory.createEmptyBorder(FONT_MEDIUM, PADDING_LARGE + leftIndent, FONT_MEDIUM, PADDING_LARGE)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        final String commentKey = reply.getContent().trim();

        addReplyHeader(panel, reply);
        addReplyContent(panel, reply);
        addCommentTranslationPanel(panel, reply, commentKey);
        addReplyActions(panel, reply);
        addNestedReplies(panel, reply, indentLevel);

        return panel;
    }

    private void addReplyHeader(JPanel panel, ReadPostOutputData.ReplyData reply) {
        final JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, COLS_THIRTY));

        final JLabel usernameLabel = new JLabel(reply.getUsername());
        usernameLabel.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_REGULAR));
        usernameLabel.setForeground(HEADER_COLOR);

        headerPanel.add(usernameLabel, BorderLayout.WEST);
        panel.add(headerPanel);
    }

    private void addReplyContent(JPanel panel, ReadPostOutputData.ReplyData reply) {
        final JTextArea replyContent = new JTextArea(reply.getContent());
        replyContent.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_CONTENT));
        replyContent.setLineWrap(true);
        replyContent.setWrapStyleWord(true);
        replyContent.setEditable(false);
        replyContent.setOpaque(false);
        replyContent.setFocusable(false);
        replyContent.setForeground(REPLY_CONTENT_COLOR);
        replyContent.setAlignmentX(Component.LEFT_ALIGNMENT);
        replyContent.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        panel.add(Box.createVerticalStrut(FONT_MEDIUM - ROWS_FOUR));
        panel.add(replyContent);
        panel.add(Box.createVerticalStrut(PADDING_MEDIUM));
    }

    private void addCommentTranslationPanel(JPanel panel, ReadPostOutputData.ReplyData reply, String commentKey) {
        final JPanel commentTranslationPanel = new JPanel();
        commentTranslationPanel.setLayout(new BoxLayout(commentTranslationPanel, BoxLayout.Y_AXIS));
        commentTranslationPanel.setBackground(WHITE_COLOR);
        commentTranslationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentTranslationPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, 0, PADDING_SMALL, 0));

        final JComboBox<String> commentLanguageDropdown = new JComboBox<>(SUPPORTED_LANGUAGES);
        commentLanguageDropdown.setSelectedItem(DEFAULT_LANGUAGE);
        commentLanguageDropdown.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_MEDIUM));

        final JButton commentTranslateButton = createCommentTranslateButton();
        final JLabel commentTranslationStatusLabel = createCommentTranslationStatusLabel();
        final JTextArea translatedReplyContentArea = createTranslatedReplyContentArea();
        final JScrollPane translatedReplyScrollPane =
                createTranslatedReplyScrollPane(translatedReplyContentArea);

        commentTranslationAreas.put(commentKey, translatedReplyContentArea);
        commentTranslationStatusLabels.put(commentKey, commentTranslationStatusLabel);
        commentTranslationButtons.put(commentKey, commentTranslateButton);

        commentTranslateButton.addActionListener(evt -> {
            handleCommentTranslateClick(commentKey, commentLanguageDropdown, commentTranslateButton,
                    commentTranslationStatusLabel, translatedReplyContentArea, reply.getContent());
        });

        final JPanel commentControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING_SMALL, 0));
        commentControlPanel.setOpaque(false);
        commentControlPanel.add(new JLabel("Translate:"));
        commentControlPanel.add(commentLanguageDropdown);
        commentControlPanel.add(commentTranslateButton);

        commentTranslationPanel.add(commentControlPanel);
        commentTranslationPanel.add(Box.createVerticalStrut(PADDING_SMALL));
        commentTranslationPanel.add(commentTranslationStatusLabel);
        commentTranslationPanel.add(Box.createVerticalStrut(PADDING_SMALL));
        commentTranslationPanel.add(translatedReplyScrollPane);

        panel.add(commentTranslationPanel);
    }

    private JButton createCommentTranslateButton() {
        final JButton button = new JButton("Translate Comment");
        button.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_MEDIUM));
        button.setFocusPainted(false);
        button.setBackground(COMMENT_TRANSLATE_BG);
        button.setForeground(DARK_TEXT);
        button.setBorder(BorderFactory.createEmptyBorder(PADDING_SMALL, PADDING_MEDIUM, PADDING_SMALL, PADDING_MEDIUM));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel createCommentTranslationStatusLabel() {
        final JLabel label = new JLabel("Select language and translate.");
        label.setFont(new Font(FONT_ARIAL, Font.ITALIC, FONT_SMALL));
        label.setForeground(STATUS_TEXT);
        return label;
    }

    private JTextArea createTranslatedReplyContentArea() {
        final JTextArea area = new JTextArea(ROWS_THREE, COLS_FORTY);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font(FONT_ARIAL, Font.ITALIC, FONT_REGULAR));
        area.setBackground(COMMENT_TRANSLATED_BG);
        area.setForeground(DARK_TEXT);
        return area;
    }

    private JScrollPane createTranslatedReplyScrollPane(JTextArea area) {
        final JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_HEIGHT_100));
        return scrollPane;
    }

    private void handleCommentTranslateClick(String commentKey, JComboBox<String> langDropdown,
            JButton translateBtn, JLabel statusLabel, JTextArea translatedArea, String replyContent) {
        if (translationController == null) {
            statusLabel.setText("Error: Translation controller is missing.");
            return;
        }

        translateBtn.setEnabled(false);
        statusLabel.setText(TRANSLATING);
        translatedArea.setText(LOADING_TRANSLATION);

        lastTextTranslatedKey = commentKey;
        translationsInProgress.add(lastTextTranslatedKey);

        final String targetLanguage = (String) langDropdown.getSelectedItem();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                translationController.execute(replyContent, targetLanguage);
                return null;
            }
        }.execute();
    }

    private void addReplyActions(JPanel panel, ReadPostOutputData.ReplyData reply) {
        final JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, FONT_MEDIUM - ROWS_FOUR, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_HEIGHT_50));

        final JButton replyUpvoteButton = createReplyVoteButton("\u25B2");
        final JButton replyDownvoteButton = createReplyVoteButton("\u25BC");

        final JLabel replyVoteCount = new JLabel(String.valueOf(reply.getUpvotes() - reply.getDownvotes()));
        replyVoteCount.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_REGULAR));
        replyVoteCount.setForeground(AUTHOR_TEXT);

        final JButton replyButton = createReplyButton();
        final JPanel replyInputPanel = createReplyInputPanel(reply);

        replyUpvoteButton.addActionListener(evt -> {
            if (voteController != null) {
                voteController.execute(true, reply.getId());
            }
        });

        replyDownvoteButton.addActionListener(evt -> {
            if (voteController != null) {
                voteController.execute(false, reply.getId());
            }
        });

        replyButton.addActionListener(evt -> replyInputPanel.setVisible(true));

        actionsPanel.add(replyUpvoteButton);
        actionsPanel.add(replyDownvoteButton);
        actionsPanel.add(replyVoteCount);
        actionsPanel.add(Box.createHorizontalStrut(FONT_MEDIUM - ROWS_FOUR));
        actionsPanel.add(replyButton);

        panel.add(actionsPanel);
        panel.add(Box.createVerticalStrut(PADDING_MEDIUM));
        panel.add(replyInputPanel);
    }

    private JButton createReplyVoteButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_MEDIUM));
        button.setFocusPainted(false);
        button.setBackground(BACKGROUND_COLOR);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VOTE_BORDER, 1),
                BorderFactory.createEmptyBorder(ROWS_FOUR, PADDING_MEDIUM, ROWS_FOUR, PADDING_MEDIUM)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createReplyButton() {
        final JButton button = new JButton(ReadPostViewModel.REPLY_BUTTON_LABEL);
        button.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_MEDIUM));
        button.setFocusPainted(false);
        button.setBackground(HEADER_COLOR);
        button.setForeground(WHITE_COLOR);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 1),
                BorderFactory.createEmptyBorder(ROWS_FOUR, FONT_MEDIUM, ROWS_FOUR, FONT_MEDIUM)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createReplyInputPanel(ReadPostOutputData.ReplyData reply) {
        final JPanel replyInputPanel = new JPanel(new BorderLayout());
        replyInputPanel.setOpaque(false);
        replyInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        replyInputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, EDIT_BTN_HEIGHT));
        replyInputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VOTE_BORDER, 1),
                BorderFactory.createEmptyBorder(PADDING_MEDIUM, FONT_MEDIUM, PADDING_MEDIUM, FONT_MEDIUM)
        ));
        replyInputPanel.setVisible(false);

        final JTextField replyTextField = new JTextField();
        replyTextField.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_CONTENT));
        replyTextField.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, FONT_MEDIUM, PADDING_MEDIUM,
                FONT_MEDIUM));

        final JPanel replyActionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, FONT_MEDIUM - ROWS_FOUR, 0));
        replyActionsPanel.setOpaque(false);
        replyActionsPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        replyActionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, EDIT_BTN_HEIGHT));

        final JButton replyCancelButton = createReplyCancelButton();
        final JButton sendReplyButton = createSendReplyButton();

        replyCancelButton.addActionListener(evt -> {
            handleCancelReply(replyTextField, replyInputPanel);
        });

        sendReplyButton.addActionListener(evt -> {
            final String replyText = replyTextField.getText();
            sendReply(replyText, reply.getId());
        });

        replyActionsPanel.add(replyCancelButton);
        replyActionsPanel.add(sendReplyButton);

        replyInputPanel.add(replyTextField, BorderLayout.NORTH);
        replyInputPanel.add(replyActionsPanel, BorderLayout.SOUTH);

        return replyInputPanel;
    }

    private JButton createReplyCancelButton() {
        final JButton button = new JButton(ReadPostViewModel.CANCEL_REPLY);
        button.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_MEDIUM));
        button.setFocusPainted(false);
        button.setBackground(CANCEL_BUTTON_BG);
        button.setForeground(WHITE_COLOR);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 1),
                BorderFactory.createEmptyBorder(ROWS_FOUR, FONT_MEDIUM, ROWS_FOUR, FONT_MEDIUM)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createSendReplyButton() {
        final JButton button = new JButton(ReadPostViewModel.REPLY_BUTTON_LABEL);
        button.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_MEDIUM));
        button.setFocusPainted(false);
        button.setBackground(HEADER_COLOR);
        button.setForeground(WHITE_COLOR);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 1),
                BorderFactory.createEmptyBorder(ROWS_FOUR, FONT_MEDIUM, ROWS_FOUR, FONT_MEDIUM)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void handleCancelReply(JTextField replyTextField, JPanel replyInputPanel) {
        if (!replyTextField.getText().isEmpty()) {
            final int userAnswer = JOptionPane.showConfirmDialog(this, CONFIRM_CANCEL_MESSAGE,
                    CONFIRM_CANCEL_TITLE, JOptionPane.YES_NO_OPTION);

            if (userAnswer != JOptionPane.YES_OPTION) {
                return;
            }
        }

        replyInputPanel.setVisible(false);
        replyTextField.setText("");
    }

    private void addNestedReplies(JPanel panel, ReadPostOutputData.ReplyData reply, int indentLevel) {
        if (!reply.getNestedReplies().isEmpty()) {
            panel.add(Box.createVerticalStrut(FONT_MEDIUM));
            for (ReadPostOutputData.ReplyData nestedReply : reply.getNestedReplies()) {
                final JPanel nestedPanel = createReplyPanel(nestedReply, indentLevel + 1);
                panel.add(nestedPanel);
                panel.add(Box.createVerticalStrut(FONT_MEDIUM - ROWS_FOUR));
            }
        }
    }

    /**
     * Loads a post by its ID.
     *
     * @param postId the unique identifier of the post to load
     */
    public void loadPost(long postId) {
        this.currentPostId = postId;

        if (controller != null) {
            controller.execute(postId);
        }
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
     * @param readPostController the read post controller
     */
    public void setController(ReadPostController readPostController) {
        this.controller = readPostController;
    }

    /**
     * Sets the read post controller.
     *
     * @param readPostController the read post controller
     */
    public void setReadPostController(ReadPostController readPostController) {
        this.controller = readPostController;
    }

    /**
     * Sets the translation controller.
     *
     * @param translationCtrl the translation controller
     */
    public void setTranslationController(TranslationController translationCtrl) {
        this.translationController = translationCtrl;
    }

    /**
     * Sets the action to execute when viewing a referenced post.
     *
     * @param viewReferencedPostClick the action to execute
     */
    public void setOnViewReferencedPostClick(Runnable viewReferencedPostClick) {
        this.onViewReferencedPostClick = viewReferencedPostClick;
    }

    /**
     * Sets the vote controller.
     *
     * @param voteCtrl the vote controller
     */
    public void setVoteController(VoteController voteCtrl) {
        this.voteController = voteCtrl;
    }

    /**
     * Sets the reply controller.
     *
     * @param replyCtrl the reply post controller
     */
    public void setReplyController(ReplyPostController replyCtrl) {
        this.replyController = replyCtrl;
    }

    /**
     * Sets the action to execute when back button is clicked.
     *
     * @param backAction the back action
     */
    public void setOnBackAction(Runnable backAction) {
        this.onBackAction = backAction;
    }

    /**
     * Sends a comment/reply.
     *
     * @param content the content of the reply
     * @param parentId the id of the reply's parent
     */
    public void sendReply(String content, long parentId) {
        replyController.execute(content, parentId);
    }

    private JPanel createReferencingPostPanel(ReadPostOutputData.ReferencingPostData refPost) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(REF_ORANGE_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(REF_ORANGE_BORDER, 1),
                BorderFactory.createEmptyBorder(FONT_MEDIUM - ROWS_FOUR, PADDING_MEDIUM, FONT_MEDIUM - ROWS_FOUR,
                        PADDING_MEDIUM)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, REF_PANEL_HEIGHT));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(REF_ORANGE_BG);

        final JLabel refTitleLabel = new JLabel(refPost.getTitle());
        refTitleLabel.setFont(new Font(FONT_ARIAL, Font.BOLD, FONT_REGULAR));
        refTitleLabel.setForeground(DARK_TEXT);
        infoPanel.add(refTitleLabel);

        final String contentPreview = refPost.getContent().length() > CONTENT_PREVIEW_LENGTH
                ? refPost.getContent().substring(0, CONTENT_PREVIEW_LENGTH) + "..."
                : refPost.getContent();
        final JLabel contentLabel = new JLabel(contentPreview);
        contentLabel.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_SMALL));
        contentLabel.setForeground(AUTHOR_TEXT);
        infoPanel.add(contentLabel);

        final JLabel refAuthorLabel = new JLabel("By: " + refPost.getUsername());
        refAuthorLabel.setFont(new Font(FONT_ARIAL, Font.ITALIC, FONT_SMALL - 1));
        refAuthorLabel.setForeground(MUTED_TEXT);
        infoPanel.add(refAuthorLabel);

        final JButton viewButton = new JButton("view");
        viewButton.setFont(new Font(FONT_ARIAL, Font.PLAIN, FONT_SMALL));
        viewButton.setFocusPainted(false);
        viewButton.setBackground(HEADER_COLOR);
        viewButton.setForeground(WHITE_COLOR);
        viewButton.setBorder(BorderFactory.createEmptyBorder(PADDING_SMALL, FONT_MEDIUM, PADDING_SMALL, FONT_MEDIUM));
        viewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewButton.addActionListener(evt -> {
            if (controller != null) {
                loadPost(refPost.getId());
            }
        });

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(viewButton, BorderLayout.EAST);

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
