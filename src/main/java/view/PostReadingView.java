package view;

import interface_adapter.read_post.ReadPostController;
import interface_adapter.read_post.ReadPostState;
import interface_adapter.read_post.ReadPostViewModel;
import interface_adapter.reply_post.ReplyPostController;
import interface_adapter.reply_post.ReplyPostPresenter;
import interface_adapter.upvote_downvote.VoteController;
import interface_adapter.upvote_downvote.VotePresenter;
import use_case.read_post.ReadPostOutputData;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for reading a post and its replies.
 */
public class PostReadingView extends JPanel implements PropertyChangeListener {
    public static final String CONFIRM_CANCEL_TITLE = "Cancel Reply";
    public static final String CONFIRM_CANCEL_MESSAGE = "Are you sure? This draft will not be saved.";

    private final ReadPostViewModel viewModel;
    private ReadPostController controller;
    private ReplyPostController replyController;
    private VoteController voteController;
    private Runnable onBackAction;

    private final JButton backButton;
    private final JLabel titleLabel;
    private final JLabel authorLabel;
    private final JTextArea contentArea;
    private final JButton upvoteButton;
    private final JButton downvoteButton;
    private final JLabel voteCountLabel;
    private final JTextField commentField;
    private final JButton commentButton;
    private final JPanel repliesPanel;
    private final JScrollPane scrollPane;

    private long currentPostId; // Tracks the ID of the displayed post

    public PostReadingView(ReadPostViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

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
        commentButton.addActionListener(e -> {
            final ReadPostState readPostState = viewModel.getState();
            final String content = commentField.getText();
            final long parentId = readPostState.getId();
            sendReply(content, parentId);
        });

        commentInputPanel.add(commentField, BorderLayout.CENTER);
        commentInputPanel.add(commentButton, BorderLayout.EAST);

        // Replies panel
        repliesPanel = new JPanel();
        repliesPanel.setLayout(new BoxLayout(repliesPanel, BoxLayout.Y_AXIS));
        repliesPanel.setBackground(new Color(245, 245, 245));

        // Add components to main panel
        mainPanel.add(contentContainer);
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
        if ("state".equals(evt.getPropertyName())) {
            final ReadPostState state = (ReadPostState) evt.getNewValue();
            updateView(state);
        }
        else if (evt.getPropertyName().equals(ReplyPostPresenter.REPLY_SUCCESS)) {
            // Clear comment field
            commentField.setText("");
            // "Refresh" page
            loadPost(viewModel.getState().getId());
        }
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

        titleLabel.setText(state.getTitle());
        authorLabel.setText(state.getUsername());
        contentArea.setText(state.getContent());
        voteCountLabel.setText(String.valueOf(state.getUpvotes() - state.getDownvotes()));

        // Clear and update replies
        repliesPanel.removeAll();
        if (!state.getReplies().isEmpty()) {
            for (ReadPostOutputData.ReplyData reply : state.getReplies()) {
                final JPanel replyPanel = createReplyPanel(reply);

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
     */
    private JPanel createReplyPanel(ReadPostOutputData.ReplyData reply) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // Add left indent for nested replies
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Reply header
        final JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        final JLabel usernameLabel = new JLabel(reply.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        usernameLabel.setForeground(new Color(70, 130, 180));

        headerPanel.add(usernameLabel, BorderLayout.WEST);

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
        panel.add(headerPanel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(replyContent);
        panel.add(Box.createVerticalStrut(10));
        panel.add(actionsPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(replyPanel);

        // Add nested replies directly to panel
        if (!reply.getNestedReplies().isEmpty()) {
            panel.add(Box.createVerticalStrut(12));
            for (ReadPostOutputData.ReplyData nestedReply : reply.getNestedReplies()) {
                final JPanel nestedPanel = createReplyPanel(nestedReply);
                panel.add(nestedPanel);
                panel.add(Box.createVerticalStrut(8));
            }
        }

        return panel;
    }

    public String getViewName() {
        return viewModel.getViewName();
    }

    public void setController(ReadPostController controller) {
        this.controller = controller;
    }

    public void setReplyController(ReplyPostController replyController) { this.replyController = replyController; }

    public void setVoteController(VoteController voteController) {
        this.voteController = voteController;
    }

    public void setOnBackAction(Runnable onBackAction) {
        this.onBackAction = onBackAction;
    }

    /**
     * Loads a post by its ID.
     * @param postId the unique identifier of the post to load
     */
    public void loadPost(long postId) {
        if (controller != null) {
            controller.execute(postId);
        }
    }

    /**
     * Sends a comment/reply
     * @param content The content of the reply
     * @param parentId The id of the reply's parent
     */
    public void sendReply(String content, long parentId) {
        replyController.execute(content, parentId);
    }
}
