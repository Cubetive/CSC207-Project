package interface_adapter.create_post;

import interface_adapter.ViewManagerModel;
import interface_adapter.browse_posts.BrowsePostsState;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.read_post.ReadPostState;
import interface_adapter.read_post.ReadPostViewModel;
import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.create_post_use_case.CreatePostOutputData;
import use_case.read_post.ReadPostOutputData;
import view.PostReadingView;
import java.util.ArrayList;
import java.util.List;

public class CreatePostPresenter implements CreatePostOutputBoundary {

    /**
     * Create Post view model.
     */
    private CreatePostViewModel createPostViewModel;
    /**
     * Read Post view model.
     */
    private ReadPostViewModel readPostViewModel;
    /**
     * view manager model.
     */
    private ViewManagerModel viewManagerModel;
    /**
     * Read Post view.
     */
    private PostReadingView postReadingView;
    /**
     * Browse Posts view model.
     */
    private BrowsePostsViewModel browsePostsViewModel;

    /**
     * Constructor with necessary elements.
     */
    public CreatePostPresenter(CreatePostViewModel createPostViewModel,
                               ViewManagerModel viewManagerModel,
                               ReadPostViewModel readPostViewModel,
                               BrowsePostsViewModel browsePostsViewModel) {
        this.createPostViewModel = createPostViewModel;
        this.viewManagerModel = viewManagerModel;
        this.readPostViewModel = readPostViewModel;
        this.browsePostsViewModel = browsePostsViewModel;
    }

    /**
     * Set up for the Post Reading View.
     */
    public void setPostReadingView(PostReadingView postReadingView) {
        this.postReadingView = postReadingView;
    }

    /**
     * Prepare the post reading view given that a post was successfully created.
     */
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
        this.postReadingView.loadPost(createPostOutputData.getOriginalPost().getId());
        this.postReadingView.revalidate();
        this.postReadingView.repaint();
        this.postReadingView.setVisible(true);
        this.viewManagerModel.firePropertyChanged();
    }

    /**
     * Create a missing field dialog (pop up) given that improper
     * inputs were given (missing fields).
     */
    public void prepareMissingFieldView(String error) {
        //TODO
        final CreatePostState createPostState = createPostViewModel.getState();
        createPostState.setMissingError(error);
        createPostViewModel.firePropertyChange();
    }

    /**
     * Back button has been pressed, switch to browse posts view.
     */
    public void switchToBrowseView() {
        //TODO
        browsePostsViewModel.setState(new BrowsePostsState());
        viewManagerModel.setState(browsePostsViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
        System.out.println("DEBUG: should switch to BrowsePostsView");
    }


}
