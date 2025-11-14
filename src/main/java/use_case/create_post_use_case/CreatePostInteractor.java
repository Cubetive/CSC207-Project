package use_case.create_post_use_case;

import entities.Post;

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
        String content = createPostInputData.getContent();
        String title = createPostInputData.getTitle();

        //TODO
        Post post = new Post();

    }
}
