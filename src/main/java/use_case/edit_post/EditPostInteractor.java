package use_case.edit_post;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import interface_adapter.edit_post.EditPostPresenter;

public class EditPostInteractor implements EditPostInputBoundary {

    private EditPostOutputBoundary editPostOutputBoundary;

    @Override
    public void editPost(int id, String username, ReadPostState postToEdit, String contentNew, JDialog dialog) {
        if (postToEdit.getUsername().equals(username)) {
            postToEdit.setContent(contentNew);

            ObjectMapper mapper = new ObjectMapper();

            try {
                // Load the JSON file into a List of Maps
                List<Map<String, Object>> posts = mapper.readValue(
                        new File(".../Posts.json"), // TODO: CHECK IF DATA PERSISTS
                        new TypeReference<List<Map<String, Object>>>() {}
                );

                // Update the matching post
                for (Map<String, Object> post : posts) {
                    if (((Number) post.get("id")).intValue() == id) {
                        post.put("content", contentNew);
                    }
                }

                // Write updated JSON back to the file
                mapper.writerWithDefaultPrettyPrinter().writeValue(
                        new File("posts.json"), posts
                );

                System.out.println("JSON updated successfully!");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        EditPostOutputData editPostOutputData = new EditPostOutputData(postToEdit);
        
        editPostOutputBoundary = new EditPostPresenter(editPostOutputData);
        editPostOutputBoundary.prepareSuccessView(dialog);
    }

}
