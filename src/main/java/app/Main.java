package app;

import javax.swing.JFrame;

/**
 * The Main class allows the program to run, by adding all the use cases.
 */
public class Main {
    /**
     * The main method to run the program.
     * @param args arguments
     */
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
