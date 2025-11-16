package View;

import interface_adapter.create_post.CreatePostViewModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class CreatingPostView extends JPanel implements ActionListener, PropertyChangeListener {
    private final CreatePostViewModel createPostViewModel;
    private final JTextField titleTextField = new  JTextField(30);
    private final JTextArea contentTextField = new  JTextArea(10, 30);

    private final JButton createPostButton = new  JButton("Create Post");
    //private final JButton logoutButton = new  JButton("Logout");
    //private final JButton searchButton = new  JButton("Search");
    //private final JButton browseButton = new  JButton("Browse");
}
