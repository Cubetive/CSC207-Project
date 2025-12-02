package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import interface_adapter.reference_post.ReferencePostController;
import interface_adapter.reference_post.ReferencePostState;
import interface_adapter.reference_post.ReferencePostViewModel;
import use_case.reference_post.ReferencePostOutputData.PostSearchResult;

/**
 * The View for referencing posts.
 * Allows users to search for posts and select one to reference.
 */
public class ReferencePostView extends JPanel implements ActionListener, PropertyChangeListener {
    public static final String VIEW_NAME = "referencePost";

    // Font name constant
    private static final String FONT_ARIAL = "Arial";
    
    private final ReferencePostViewModel viewModel;
    private ReferencePostController controller;
    private Runnable onCancelAction;
    private Runnable onReferenceSelected;
    
    private final JTextField searchField;
    private final JButton searchButton;
    private final JButton cancelButton;
    private final JPanel resultsPanel;
    private final JScrollPane scrollPane;
    private final JLabel errorLabel;
    private final JLabel noResultsLabel;
    
    public ReferencePostView(ReferencePostViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(245, 245, 245));
        
        // Top panel with title and search
        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        final JLabel titleLabel = new JLabel(ReferencePostViewModel.TITLE_LABEL);
        titleLabel.setFont(new Font(FONT_ARIAL, Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 50));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(20));
        
        // Search panel
        final JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchPanel.setBackground(new Color(245, 245, 245));
        
        final JLabel searchLabel = new JLabel(ReferencePostViewModel.SEARCH_LABEL + ":");
        searchLabel.setFont(new Font(FONT_ARIAL, Font.PLAIN, 14));
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(30);
        searchField.setFont(new Font(FONT_ARIAL, Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        searchPanel.add(searchField);
        
        searchButton = new JButton(ReferencePostViewModel.SEARCH_BUTTON_LABEL);
        searchButton.setFont(new Font(FONT_ARIAL, Font.BOLD, 14));
        searchButton.setFocusPainted(false);
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(this);
        searchButton.setOpaque(true);
        searchPanel.add(searchButton);
        
        // Allow Enter key to trigger search
        searchField.addActionListener(this);
        
        topPanel.add(searchPanel);
        topPanel.add(Box.createVerticalStrut(15));
        
        // Error label
        errorLabel = new JLabel();
        errorLabel.setFont(new Font(FONT_ARIAL, Font.PLAIN, 14));
        errorLabel.setForeground(new Color(220, 53, 69));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);
        topPanel.add(errorLabel);
        
        // No results label
        noResultsLabel = new JLabel(ReferencePostViewModel.NO_RESULTS_MESSAGE);
        noResultsLabel.setFont(new Font(FONT_ARIAL, Font.PLAIN, 16));
        noResultsLabel.setForeground(new Color(120, 120, 120));
        noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        noResultsLabel.setVisible(false);
        topPanel.add(noResultsLabel);
        
        // Results panel
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(new Color(245, 245, 245));
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Bottom panel with cancel button
        final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        
        cancelButton = new JButton(ReferencePostViewModel.CANCEL_BUTTON_LABEL);
        cancelButton.setFont(new Font(FONT_ARIAL, Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setBackground(new Color(166, 166, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
        
        // Add components
        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void setController(ReferencePostController controller) {
        this.controller = controller;
    }
    
    public void setOnCancelAction(Runnable onCancelAction) {
        this.onCancelAction = onCancelAction;
    }
    
    public void setOnReferenceSelected(Runnable onReferenceSelected) {
        this.onReferenceSelected = onReferenceSelected;
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(searchButton) || evt.getSource().equals(searchField)) {
            final String keyword = searchField.getText().trim();
            if (controller != null && !keyword.isEmpty()) {
                // For now, we'll pass null as currentPostId since the post doesn't exist yet
                // This will be handled when the post is created
                controller.searchPosts(null, keyword);
            }
        }
        else if (evt.getSource().equals(cancelButton)) {
            if (controller != null) {
                controller.cancelReferencePost();
            }
            if (onCancelAction != null) {
                onCancelAction.run();
            }
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ReferencePostState state = viewModel.getState();
        
        if ("error".equals(evt.getPropertyName()) || "state".equals(evt.getPropertyName())) {
            updateErrorDisplay(state);
        }
        
        if ("searchResults".equals(evt.getPropertyName()) || "state".equals(evt.getPropertyName())) {
            updateSearchResults(state);
        }
        
        if ("referenceSuccess".equals(evt.getPropertyName())) {
            // Reference was successfully attached
            if (onReferenceSelected != null) {
                onReferenceSelected.run();
            }
        }
    }
    
    private void updateErrorDisplay(ReferencePostState state) {
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            errorLabel.setText(state.getErrorMessage());
            errorLabel.setVisible(true);
            noResultsLabel.setVisible(false);
        }
        else {
            errorLabel.setVisible(false);
        }
    }
    
    private void updateSearchResults(ReferencePostState state) {
        // Clear existing results
        resultsPanel.removeAll();
        
        final java.util.List<PostSearchResult> searchResults = state.getSearchResults();
        
        if (searchResults == null || searchResults.isEmpty()) {
            // Show "no results" message if we have an error message that says "No posts found"
            if (state.getErrorMessage() != null
                    && state.getErrorMessage().equals(ReferencePostViewModel.NO_RESULTS_MESSAGE)) {
                noResultsLabel.setVisible(true);
            }
            else {
                noResultsLabel.setVisible(false);
            }
        }
        else {
            noResultsLabel.setVisible(false);
            errorLabel.setVisible(false);
            
            // Display search results
            for (PostSearchResult result : searchResults) {
                final JPanel resultPanel = createResultPanel(result, state.getCurrentPostId());
                resultsPanel.add(resultPanel);
                resultsPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    
    private JPanel createResultPanel(PostSearchResult result, String currentPostId) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Left panel with post info
        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Title (if available)
        if (result.getTitle() != null && !result.getTitle().isEmpty()) {
            final JLabel titleLabel = new JLabel(result.getTitle());
            titleLabel.setFont(new Font(FONT_ARIAL, Font.BOLD, 16));
            titleLabel.setForeground(new Color(50, 50, 50));
            infoPanel.add(titleLabel);
            infoPanel.add(Box.createVerticalStrut(5));
        }
        
        // Content preview (truncated)
        final String content = result.getContent();
        final String contentPreview = content.length() > 150
                ? content.substring(0, 150) + "..." 
                : content;
        final JLabel contentLabel = new JLabel("<html><body style='width: 400px'>"
                + contentPreview.replace("\n", "<br>") + "</body></html>");
        contentLabel.setFont(new Font(FONT_ARIAL, Font.PLAIN, 13));
        contentLabel.setForeground(new Color(80, 80, 80));
        infoPanel.add(contentLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        
        // Metadata
        final JPanel metadataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        metadataPanel.setBackground(Color.WHITE);
        
        final JLabel authorLabel = new JLabel("By: " + result.getCreatorUsername());
        authorLabel.setFont(new Font(FONT_ARIAL, Font.PLAIN, 12));
        authorLabel.setForeground(new Color(120, 120, 120));
        metadataPanel.add(authorLabel);
        
        final JLabel dateLabel = new JLabel("Date: " + result.getCreationDate());
        dateLabel.setFont(new Font(FONT_ARIAL, Font.PLAIN, 12));
        dateLabel.setForeground(new Color(120, 120, 120));
        metadataPanel.add(dateLabel);
        
        infoPanel.add(metadataPanel);
        
        // Right panel with reference button
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        final JButton referenceButton = new JButton(ReferencePostViewModel.REFERENCE_BUTTON_LABEL);
        referenceButton.setFont(new Font(FONT_ARIAL, Font.BOLD, 13));
        referenceButton.setFocusPainted(false);
        referenceButton.setBackground(new Color(70, 130, 180));
        referenceButton.setForeground(Color.WHITE);
        referenceButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        referenceButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        referenceButton.setOpaque(true);
        referenceButton.addActionListener(evt -> {
            if (controller != null && currentPostId != null) {
                controller.referencePost(currentPostId, result.getPostId());
            }
            else if (controller != null) {
                // Store the referenced post ID for later use when creating the post
                // This will be handled by the CreatePostState
                if (onReferenceSelected != null) {
                    // Store the result in the view model state temporarily
                    final ReferencePostState state = viewModel.getState();
                    state.setReferencedPost(result);
                    viewModel.setState(state);
                    onReferenceSelected.run();
                }
            }
        });
        buttonPanel.add(referenceButton);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    public String getViewName() {
        return VIEW_NAME;
    }
    
    /**
     * Gets the currently selected referenced post from the state.
     * @return the referenced post result, or null if none selected
     */
    public PostSearchResult getSelectedReferencedPost() {
        return viewModel.getState().getReferencedPost();
    }
    
    /**
     * Clears the selected referenced post.
     */
    public void clearSelectedReferencedPost() {
        final ReferencePostState state = viewModel.getState();
        state.setReferencedPost(null);
        viewModel.setState(state);
    }
}

