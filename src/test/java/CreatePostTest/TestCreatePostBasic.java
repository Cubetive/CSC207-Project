package CreatePostTest;

import data_access.InMemorySessionRepository;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.read_post.ReadPostViewModel;
import interface_adapter.translate.TranslationViewModel;
import use_case.session.SessionRepository;
import view.CreatingPostView;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_post.CreatePostController;
import interface_adapter.create_post.CreatePostPresenter;
import interface_adapter.create_post.CreatePostViewModel;
import use_case.create_post_use_case.CreatePostInputBoundary;
import use_case.create_post_use_case.CreatePostInteractor;
import use_case.create_post_use_case.CreatePostOutputBoundary;
import view.PostReadingView;

import javax.swing.*;

public class TestCreatePostBasic {
    public static void main(String[] args) {
        CreatePostViewModel viewModel = new CreatePostViewModel();
        ReadPostViewModel readPostViewModel = new ReadPostViewModel();
        TranslationViewModel  translationViewModel = new TranslationViewModel();
        BrowsePostsViewModel browsePostsViewModel = new BrowsePostsViewModel();
        final CreatePostOutputBoundary presenter = new CreatePostPresenter(
                viewModel,
                new ViewManagerModel(),
                readPostViewModel,
                browsePostsViewModel
        );
        SessionRepository sessionRepository = new InMemorySessionRepository();
        final CreatePostInputBoundary interactor = new CreatePostInteractor(
                new ExampleDataBaseObject("posts.json"),
                presenter,
                sessionRepository
        );


        PostReadingView postReadingView = new PostReadingView(readPostViewModel, translationViewModel);

        CreatePostController controller = new CreatePostController(interactor);
        CreatingPostView view = new CreatingPostView(viewModel);
        view.setController(controller);
        JFrame frame = new JFrame();
        frame.add(view);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
