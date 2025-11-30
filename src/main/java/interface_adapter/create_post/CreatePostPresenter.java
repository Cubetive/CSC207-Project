package interface_adapter.create_post;

import interface_adapter.ViewManagerModel;
import interface_adapter.read_post.ReadPostState;
import interface_adapter.read_post.ReadPostViewModel;
import interface_adapter.signup.SignupViewModel;
import use_case.create_post_use_case.CreatePostInteractor;
import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.create_post_use_case.CreatePostOutputData;
import use_case.read_post.ReadPostOutputData;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

public class CreatePostPresenter implements CreatePostOutputBoundary {

    private CreatePostViewModel createPostViewModel;
    private ReadPostViewModel readPostViewModel;
    //TODO BrowseViewModel
    //TODO SearchViewModel
    //private SignupViewModel signupViewModel;
    private ViewManagerModel viewManagerModel;

    public CreatePostPresenter(CreatePostViewModel createPostViewModel,
                               ViewManagerModel viewManagerModel,
                               ReadPostViewModel readPostViewModel) {
        this.createPostViewModel = createPostViewModel;
        //this.signupViewModel = signupViewModel;
        this.viewManagerModel = viewManagerModel;
        this.readPostViewModel = readPostViewModel;
    }

    public void prepareCreatedView(CreatePostOutputData createPostOutputData) {
        // Case 2: read Post after finishing.
        System.out.println("Post created, move to reading it.");
        final ReadPostState readPostState = this.readPostViewModel.getState();
        readPostState.setContent(createPostOutputData.getOriginalPost().getContent());
        readPostState.setTitle(createPostOutputData.getOriginalPost().getTitle());
        readPostState.setId(createPostOutputData.getOriginalPost().getId());
        List<ReadPostOutputData.ReplyData> replies = new ArrayList<>();
        readPostState.setReplies(replies);
        readPostState.setDownvotes(createPostOutputData.getOriginalPost().getVotes()[1]);
        readPostState.setUpvotes(createPostOutputData.getOriginalPost().getVotes()[0]);
        readPostState.setUsername("Dummy username: replace with session");
        this.readPostViewModel.setState(readPostState);

        this.createPostViewModel.setState(new CreatePostState());

        this.viewManagerModel.setState(readPostViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
        //TODO: call load post from PostReadingView (needs this object) to display.
    }

    public void prepareMissingFieldView(String error) {
        //TODO
        final CreatePostState createPostState = createPostViewModel.getState();
        createPostState.setMissingError(error);
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
