package CreatePostTest;

import View.CreatingPostView;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_post.CreatePostController;
import interface_adapter.create_post.CreatePostPresenter;
import interface_adapter.create_post.CreatePostViewModel;
import use_case.create_post_use_case.CreatePostInputBoundary;
import use_case.create_post_use_case.CreatePostInteractor;
import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.create_post_use_case.CreatePostUserDataAccessInterface;

public class TestCreatePostUI {
    public static void main(String[] args) {
        CreatePostViewModel viewModel = new CreatePostViewModel();
        final CreatePostOutputBoundary presenter = new CreatePostPresenter(
                viewModel,
                new ViewManagerModel()
        );

        final CreatePostInputBoundary interactor = new CreatePostInteractor(
                new ExampleDataBaseObject(),
                presenter

        );

        CreatePostController controller = new CreatePostController(interactor);
        CreatingPostView view = new CreatingPostView(viewModel);
        view.setController(controller);
        view.setVisible(true);
    }
}
