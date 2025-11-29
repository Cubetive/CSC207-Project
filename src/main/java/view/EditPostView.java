package view;
import javax.swing.*;

import interface_adapter.edit_post.EditPostController;
import use_case.edit_post.EditPostInputData;
import entities.User;

import java.awt.*;

public class EditPostView {
    private User cur_user;

    public EditPostView(InMemorySessionRepository sessionRepository) {
        this.cur_user = sessionRepository.getCurrentUser();
    }

    private void createEditButton() {
        JButton editButton = new JButton("Edit Post");
        editButton.setPreferredSize(new Dimension(120, 40));

        editButton.addActionListener(e -> openEditPostDialog(state));

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        if (cur_user.getUsername().equals(state.getUsername())) {
            editPanel.add(editButton);
        }

        mainPanel.add(editPanel);
        mainPanel.setLocationRelativeTo(null);
        mainPanel.setVisible(true);
    }

    private void openEditPostDialog(ReadPostState state) {
        JDialog dialog = new JDialog((Frame) null, "Edit Post", true);
        dialog.setSize(550, 450);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------- Body Section ----------
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));

        JLabel bodyLabel = new JLabel("Post Text");
        bodyLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextArea bodyArea = new JTextArea(state.getContent(), 10, 40);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        bodyArea.setFont(new Font("Serif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(
                bodyArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        bodyPanel.add(bodyLabel);
        bodyPanel.add(Box.createVerticalStrut(5));
        bodyPanel.add(scrollPane);

        // ---------- Submit Button ----------
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(100, 35));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(submitButton);

        submitButton.addActionListener(e -> {
            String updatedText = bodyArea.getText();

            // Controller call
            EditPostController editPostController =
                new EditPostController(new EditPostInputData(state.getID(), cur_user.getUsername(), state, updatedText, dialog));
            editPostController.editPost();
        });

        // ---------- Add Components ----------
        dialog.add(bodyPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
