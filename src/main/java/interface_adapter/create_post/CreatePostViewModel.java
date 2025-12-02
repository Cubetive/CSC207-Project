package interface_adapter.create_post;

import interface_adapter.ViewModel;

public class CreatePostViewModel extends ViewModel<CreatePostState> {

    /**
     * View model button name.
     */
    public static final String CREATE_BUTTON_LABEL = "Create Post";

    /**
     * View model name.
     */
    private final String viewName = "CreatePost";

    /**
     * Constructor.
     */
    public CreatePostViewModel() {
        super("CreatePost");
        setState(new CreatePostState());
    }
}
