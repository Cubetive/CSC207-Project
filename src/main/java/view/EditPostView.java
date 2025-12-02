package view;

import java.awt.*;

import javax.swing.*;

import entities.User;
import interface_adapter.edit_post.EditPostController;
import interface_adapter.read_post.ReadPostState;
import use_case.edit_post.EditPostInputData;

public class EditPostView {

    public EditPostView(JTextArea contentArea, ReadPostState state, User cur_user) {
        final JDialog dialog = new JDialog((Frame) null, "Edit Post", true);
        dialog.setSize(550, 450);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));

        final JLabel bodyLabel = new JLabel("Post Text");
        bodyLabel.setFont(new Font("Arial", Font.BOLD, 14));

        final JTextArea bodyArea = new JTextArea(state.getContent(), 10, 40);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        bodyArea.setFont(new Font("Serif", Font.PLAIN, 14));

        final JScrollPane scrollPane = new JScrollPane(
                bodyArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        bodyPanel.add(bodyLabel);
        bodyPanel.add(Box.createVerticalStrut(5));
        bodyPanel.add(scrollPane);

        final JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(100, 35));

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(submitButton);

        submitButton.addActionListener(event -> {
            final String updatedText = bodyArea.getText();

            if (!updatedText.isEmpty()) {
                final EditPostController editPostController =
                        new EditPostController(new EditPostInputData(contentArea, state.getId(),
                                cur_user.getUsername(), state, updatedText, dialog));
                editPostController.editPost();
            }
            else {
                JOptionPane.showMessageDialog(dialog, "Fill in the content.",
                        "MISSING CONTENT", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        dialog.add(bodyPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
