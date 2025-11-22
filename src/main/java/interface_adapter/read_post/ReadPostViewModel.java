package interface_adapter.read_post;

import interface_adapter.ViewModel;

/**
 * The View Model for the Read Post View.
 */
public class ReadPostViewModel extends ViewModel<ReadPostState> {

    public static final String BACK_BUTTON_LABEL = "Back";
    public static final String COMMENT_BUTTON_LABEL = "Comment";
    public static final String REPLY_BUTTON_LABEL = "Reply";
    public static final String COMMENTS_LABEL = "Comments";
    public static final String CANCEL_REPLY = "Cancel";

    public ReadPostViewModel() {
        super("read post");
        setState(new ReadPostState());
    }

    public void firePropertyChanged() {
        firePropertyChange();
    }
}
