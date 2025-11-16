package interface_adapter.create_post;

import interface_adapter.ViewManagerModel;
import interface_adapter.signup.SignupViewModel;
import use_case.create_post_use_case.CreatePostInteractor;
import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.create_post_use_case.CreatePostOutputData;

import javax.swing.text.View;

public class CreatePostPresenter implements CreatePostOutputBoundary {

    private CreatePostViewModel createPostViewModel;
    //TODO BrowseViewModel
    //TODO SearchViewModel
    private SignupViewModel signupViewModel;
    private ViewManagerModel viewManagerModel;

    public CreatePostPresenter(CreatePostViewModel createPostViewModel,
                               SignupViewModel signupViewModel) {
        this.createPostViewModel = createPostViewModel;
        this.signupViewModel = signupViewModel;
    }

    public void prepareCreatedView(CreatePostOutputData createPostOutputData) {
        //TODO
        //final BrowingState browsingState = BrowsingViewModel.getState();
        // Update Browing state with necessary information if any
        //this.browseViewModel.firePropertyChange();

        //CreatePostViewModel.setState(new CreatePostState());

        //this.viewManagerModel.setState(BrowseViewModel.getname());
    }

    public void prepareMissingFieldView() {
        //TODO
    }

    public void switchToSignInView() {
        //TODO
    }

    public void switchToBrowseView() {
        //TODO
    }

    public void switchToSearchView() {
        //TODO
    }

}
