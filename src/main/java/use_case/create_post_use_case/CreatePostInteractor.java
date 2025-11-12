package use_case.create_post_use_case;

public class CreatePostInteractor implements CreatePostInputBoundary{
    private final CreatePostUserDataAccessInterface createPostUserDataAccessInterface;
    private final CreatePostOutputBoundary createPostOutputBoundary;

    public CreatePostInteractor(
            CreatePostUserDataAccessInterface createPostUserDataAccessInterface,
            CreatePostOutputBoundary createPostOutputBoundary) {
        this.createPostUserDataAccessInterface = createPostUserDataAccessInterface;
        this.createPostOutputBoundary = createPostOutputBoundary;
    }

    @Override
    public void execute(CreatePostInputData createPostInputData) {

    }
}
