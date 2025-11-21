package view;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import entities.OriginalPost;
import interface_adapter.search_post.SearchPostController;
import interface_adapter.search_post.SearchPostViewModel;
import use_case.search_post.SearchPostInputData;

public class SearchPostView {

    private JFrame frame;
    private JTextField searchField;
    private JList<String> postList;
    private DefaultListModel<String> listModel;
    private List<OriginalPost> original_posts = new ArrayList<>();

    private SearchPostViewModel searchPostViewModel;

    public SearchPostView() {

        // sample posts

        this.original_posts.add(new OriginalPost("Test 1", "user1", "abc"));
        this.original_posts.add(new OriginalPost("Test 2", "user2", "abc"));
        this.original_posts.add(new OriginalPost("Test 3", "user3", "abc"));

        searchPostViewModel = new SearchPostViewModel();

        // Create main frame
        frame = new JFrame("Searching Feature");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        frame.add(mainPanel);

        // Search field
        searchField = new JTextField();

        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Add a titled border around search box
        searchField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Search Posts",
                TitledBorder.LEADING,
                TitledBorder.TOP
        ));

        mainPanel.add(searchField);

        // List for post titles
        listModel = new DefaultListModel<>();
        postList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(postList);
        mainPanel.add(scrollPane);

        // Populate list initially
        searchPostViewModel.updatePostList(listModel, original_posts);

        // Add search listener
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { searchPosts(); }
            public void removeUpdate(DocumentEvent e) { searchPosts(); }
            public void changedUpdate(DocumentEvent e) { searchPosts(); }

            private void searchPosts() {
                String keyword = searchField.getText();
                SearchPostController searchPostController = new SearchPostController(new SearchPostInputData(listModel, original_posts, keyword));
                searchPostController.searchPosts();
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchPostView());
    }
}
