package interface_adapter.create_post;

import interface_adapter.ViewModel;

public class CreatePostViewModel extends ViewModel<CreatePostState> {

    /**
     * View model name.
     */
    private final String viewName = "CreatePost";

    /**
     * View model button name
     */
    public final static String CREATE_BUTTON_LABEL = "Create Post";

    /**
     * Constructor.
     */
    public CreatePostViewModel() {
        super("CreatePost");
        setState(new CreatePostState());
    }
}
