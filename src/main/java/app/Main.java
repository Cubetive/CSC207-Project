package app;

import javax.swing.*;

/**
 * The Main class demonstrates how to use the Signup and Browse Posts functionality.
 */
public class Main {

    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();

        final JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoginView()
                .addBrowsePostsView()
                .addReadPostView()
                .addEditProfileView()
                .addCreatePostView()
                .addReferencePostView
                .addLoginUseCase()
                .addLogoutUseCase()
                .addSignupUseCase()
                .addBrowsePostsUseCase()
                .addReadPostUseCase()
                .addVoteUseCase()
                .addTranslationUseCase()
                .addReplyPostUseCase()
                .addEditProfileUseCase()
                .addCreatePostUseCase()
                .addReferencePostUseCase()
                .build();

        application.pack();
        application.setVisible(true);
    }
}
