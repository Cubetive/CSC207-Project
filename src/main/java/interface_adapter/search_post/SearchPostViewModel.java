package interface_adapter.search_post;

import java.awt.*;

import javax.swing.*;

import interface_adapter.browse_posts.BrowsePostsState;
import use_case.browse_posts.BrowsePostsOutputData;
import view.BrowsePostsView;


public class SearchPostViewModel {

    /**
     * Updates the post panel with the new state.
     * @param postsPanel current viewed post panel
     * @param state the whole post list
     */
    public void updatePostList(JPanel postsPanel, BrowsePostsState state) {
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
        }
        else if (state.getPosts().isEmpty()) {
            // Show "No results found" message
            final JLabel noPostsLabel = new JLabel("No results found");
            noPostsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            noPostsLabel.setForeground(new Color(120, 120, 120));
            noPostsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsPanel.add(Box.createVerticalGlue());
            postsPanel.add(noPostsLabel);
            postsPanel.add(Box.createVerticalGlue());
        }
        else {
            // Display updated posts
            for (BrowsePostsOutputData.PostData post : state.getPosts()) {
                final JPanel postPanel = BrowsePostsView.createPostPanel(post);
                postsPanel.add(postPanel);
                postsPanel.add(Box.createVerticalStrut(15));
            }
        }

        postsPanel.revalidate();
        postsPanel.repaint();
    }
    
}
