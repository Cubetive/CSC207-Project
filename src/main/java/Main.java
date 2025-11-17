import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import java.awt.*;
import java.util.List;
import entities.User;
import entities.OriginalPost;

public class Main {

    private JFrame frame;
    private JTextField searchField;
    private JList<String> postList;
    private DefaultListModel<String> listModel;
    private User user;

    public Main() {
        // Sample user
        user = new User("John Doe", "john123", "john@example.com", "password");

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
        updatePostList(user.getOriginalPosts());

        // Add search listener
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { searchPosts(); }
            public void removeUpdate(DocumentEvent e) { searchPosts(); }
            public void changedUpdate(DocumentEvent e) { searchPosts(); }

            private void searchPosts() {
                String keyword = searchField.getText();
                List<OriginalPost> filtered = user.searchPosts(keyword);
                updatePostList(filtered);
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void updatePostList(List<OriginalPost> posts) {
        listModel.clear();
        for (OriginalPost post : posts) {
            listModel.addElement(post.getTitle());
        }
    }

    public static void main(String[] args) {
        // Ensure GUI runs on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new Main());
    }
}
