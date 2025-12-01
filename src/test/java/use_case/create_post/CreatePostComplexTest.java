package use_case.create_post;

import javax.swing.*;

public class CreatePostComplexTest {


    public static void main(String[] args) {
        final CreatePostTestBuilder builder = new CreatePostTestBuilder();
        
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
