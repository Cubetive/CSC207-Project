package app;

import javax.swing.*;

/**
 * The Main class demonstrates how to use the Login, Signup and Browse Posts functionality.
 */
public class Main {

    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();

        final JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addBrowsePostsView()
                .addReadPostView()
                .addLoginUseCase()
                .addSignupUseCase()
                .addBrowsePostsUseCase()
                .addReadPostUseCase()
                .addBrowsePostsView()
                .addReplyPostUseCase()
                .build();

        application.pack();
        application.setVisible(true);
    }
}
