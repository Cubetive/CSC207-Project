package use_case.create_post;

import view.PostReadingView;

import javax.swing.*;

public class TestCreatePostComplex {


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
