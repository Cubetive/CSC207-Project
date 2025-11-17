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
    //private SignupViewModel signupViewModel;
    private ViewManagerModel viewManagerModel;

    public CreatePostPresenter(CreatePostViewModel createPostViewModel,
                               ViewManagerModel viewManagerModel) {
        this.createPostViewModel = createPostViewModel;
        //this.signupViewModel = signupViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void prepareCreatedView(CreatePostOutputData createPostOutputData) {
        //TODO
        // Case 1: return to Browsing when finishing reading post.
        //final BrowingState browsingState = BrowsingViewModel.getState();
        // Update Browing state with necessary information if any
        //this.browseViewModel.firePropertyChange();

        //CreatePostViewModel.setState(new CreatePostState());

        //this.viewManagerModel.setState(BrowseViewModel.getname());
        // Case 2: read Post after finishing.
    }

    public void prepareMissingFieldView() {
        //TODO
        final CreatePostState createPostState = createPostViewModel.getState();
        createPostState.setMissingError("Missing content or title.");
        createPostViewModel.firePropertyChange();
    }

    public void switchToSignUpView() {
        //TODO
        //viewManagerModel.setState(signupViewModel.getViewName);
        viewManagerModel.firePropertyChanged();
    }

    public void switchToBrowseView() {
        //TODO
        //viewManagerModel.setState(BrowseViewModel.getViewName);
        viewManagerModel.firePropertyChanged();
    }

    public void switchToSearchView() {
        //TODO
        //viewManagerModel.setState(SearchViewModel.getViewName);
        viewManagerModel.firePropertyChanged();
    }

}
