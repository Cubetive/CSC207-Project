package interface_adapter.read_post;

import use_case.read_post.ReadPostOutputBoundary;
import use_case.read_post.ReadPostOutputData;

/**
 * Presenter for the Read Post use case.
 */
public class ReadPostPresenter implements ReadPostOutputBoundary {

    private final ReadPostViewModel viewModel;

    public ReadPostPresenter(ReadPostViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ReadPostOutputData outputData) {
        final ReadPostState state = new ReadPostState();
        // 🔥 CRITICAL FIX: Add the line to set the Post ID
        state.setId(outputData.getId());
        state.setTitle(outputData.getTitle());
        state.setContent(outputData.getContent());
        state.setUsername(outputData.getUsername());
        state.setUpvotes(outputData.getUpvotes());
        state.setDownvotes(outputData.getDownvotes());
        state.setReplies(outputData.getReplies());
        state.setErrorMessage(null);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final ReadPostState state = new ReadPostState();
        state.setErrorMessage(errorMessage);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
