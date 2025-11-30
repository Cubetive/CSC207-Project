package CreatePostTest;

import app.AppBuilder;
import interface_adapter.read_post.ReadPostController;
import interface_adapter.read_post.ReadPostPresenter;
import interface_adapter.read_post.ReadPostViewModel;
import use_case.read_post.ReadPostInputBoundary;
import use_case.read_post.ReadPostInteractor;
import use_case.read_post.ReadPostOutputBoundary;
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
