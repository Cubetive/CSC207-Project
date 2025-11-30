package app;

import javax.swing.*;

/**
 * The Main class demonstrates how to use the Signup and Browse Posts functionality.
 */
public class Main {

    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();

        final JFrame application = appBuilder
                .addSignupView()
                .addLoginView()
                .addBrowsePostsView()
                .addReadPostView()
                .addTranslationUseCase()
                .addSignupUseCase()
                .addLoginUseCase()
                .addLogoutUseCase()
                .addBrowsePostsUseCase()
                .addReadPostUseCase()
                .addReplyPostUseCase()
                .addVoteUseCase()        // Don't forget this!
                .addEditProfileUseCase()
                .build();

        application.pack();
        application.setVisible(true);
    }
}
