package interface_adapter.create_post;

import java.beans.PropertyChangeSupport;

public class CreatePostViewModel {
    private final String viewName = "CreatePost";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private final String TITLE_LABEL = "Create Post";
    private final String NEW_CONTENT_LABEL = "Enter Text";
    private final String NEW_TITLE_LABEL = "Enter Title";
    private final String CREATE_BUTTON_LABEL = "Create Post";
    private final String LOGOUT_BUTTON_LABEL = "Logout";
    private final String BROWSE_BUTTON_LABEL = "Browse";
    private final String SEARCH_BUTTON_LABEL = "Search";
    private final String PROFILE_BUTTON_LABEL = "Profile";

    private CreatePostState state = new CreatePostState();

    public String getViewName() {
        return viewName;
    }

    public CreatePostState getState() {
        return state;
    }

    public void setState(CreatePostState state) {
        this.state = state;
    }

    public void firePropertyChange() {
        support.firePropertyChange("state", null, this.state);
    }
}
