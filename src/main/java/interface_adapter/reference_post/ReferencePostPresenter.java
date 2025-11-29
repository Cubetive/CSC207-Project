package interface_adapter.reference_post;

import interface_adapter.ViewManagerModel;
import use_case.reference_post.ReferencePostOutputBoundary;
import use_case.reference_post.ReferencePostOutputData;

/**
 * The Presenter for the Reference Post Use Case.
 */
public class ReferencePostPresenter implements ReferencePostOutputBoundary {

    private final ReferencePostViewModel referencePostViewModel;
    private final ViewManagerModel viewManagerModel;

    public ReferencePostPresenter(ReferencePostViewModel referencePostViewModel,
                                 ViewManagerModel viewManagerModel) {
        this.referencePostViewModel = referencePostViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSearchResultsView(ReferencePostOutputData outputData) {
        final ReferencePostState state = referencePostViewModel.getState();
        
        // Clear any previous errors
        state.setErrorMessage(null);
        
        // Set the search results
        state.setSearchResults(outputData.getSearchResults());
        state.setCurrentPostId(outputData.getCurrentPostId());
        
        referencePostViewModel.setState(state);
        referencePostViewModel.firePropertyChanged("searchResults");
    }

    @Override
    public void prepareSuccessView(ReferencePostOutputData outputData) {
        final ReferencePostState state = referencePostViewModel.getState();
        
        // Clear any previous errors
        state.setErrorMessage(null);
        
        // Set the referenced post
        state.setReferencedPost(outputData.getReferencedPost());
        state.setCurrentPostId(outputData.getCurrentPostId());
        
        // Clear search results
        state.setSearchResults(null);
        state.setSearchKeyword("");
        
        referencePostViewModel.setState(state);
        referencePostViewModel.firePropertyChanged("referenceSuccess");
        
        // Return to the create post view
        viewManagerModel.setState("createPost");
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final ReferencePostState state = referencePostViewModel.getState();
        state.setErrorMessage(errorMessage);
        
        referencePostViewModel.firePropertyChanged("error");
    }

    @Override
    public void cancelReferencePost() {
        final ReferencePostState state = referencePostViewModel.getState();
        
        // Clear the state
        state.setSearchKeyword("");
        state.setSearchResults(null);
        state.setErrorMessage(null);
        
        referencePostViewModel.setState(state);
        referencePostViewModel.firePropertyChanged();
        
        // Return to the create post view
        viewManagerModel.setState("createPost");
        viewManagerModel.firePropertyChanged();
    }
}
