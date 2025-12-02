package use_case.create_post;

import javax.swing.*;

public class CreatePostComplexTest {

    /**
     * Main program.
     * @param args default parameter args.
     */
    // Main method.
    @SuppressWarnings({"checkstyle:UncommentedMain", "checkstyle:SuppressWarnings"})
    public static void main(String[] args) {
        // Builder.
        final CreatePostTestBuilder builder = new CreatePostTestBuilder();

        // Execute the builder.
        final JFrame testApplication = builder
                .addCreatePostView()
                .addReadPostView()
                .addCreatePostUseCase()
                .addReadPostUseCase()
                .build();
        testApplication.pack();
        testApplication.setVisible(true);
    }
}
