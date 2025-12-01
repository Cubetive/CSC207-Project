package app;

import javax.swing.*;

/**
 * The Main class allows the program to run, by adding all the use cases.
 */
public class Main {

    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();

        final JFrame application = appBuilder
                .addSignupView()
                .addLoginView()
                .addBrowsePostsView()
                .addReadPostView()
                .addEditProfileView()
                .addCreatePostView()
                .addReferencePostView()
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
