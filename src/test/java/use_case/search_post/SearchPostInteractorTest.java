package use_case.search_post;

import interface_adapter.browse_posts.BrowsePostsState;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.search_post.SearchPostController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import use_case.browse_posts.BrowsePostsOutputData;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchPostInteractorTest {

    BrowsePostsViewModel createSamplePosts() {
        BrowsePostsViewModel viewModel = new BrowsePostsViewModel();

        BrowsePostsState state = new BrowsePostsState();

        List<BrowsePostsOutputData.PostData> posts = new ArrayList<>();
        BrowsePostsOutputData.PostData post1 = new BrowsePostsOutputData.PostData(1, "game 1", "game 1 content", "bob1", new Date(), 0, 0);
        BrowsePostsOutputData.PostData post2 = new BrowsePostsOutputData.PostData(2, "game 2", "game 2 content", "bob1", new Date(), 2, 0);
        BrowsePostsOutputData.PostData post3 = new BrowsePostsOutputData.PostData(3, "drey 3", "drey 3 content", "k3f", new Date(), 3, 1);
        BrowsePostsOutputData.PostData post4 = new BrowsePostsOutputData.PostData(4, "game 4", "game 4 content", "rye2", new Date(), 0, 5);

        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        posts.add(post4);

        state.setPosts(posts);

        viewModel.setState(state);

        return viewModel;
    }

    @Test
    void searchFoundOne() {
        String keyword = "game 2";
        JPanel postsPanel = new JPanel();
        BrowsePostsViewModel viewModel = createSamplePosts();
        SearchPostController searchPostController = new SearchPostController(new SearchPostInputData(postsPanel, viewModel.getState(), keyword));
        searchPostController.searchPosts();

        List<String> actualList = new ArrayList<>();

        for (Component postPanel : postsPanel.getComponents()) {
            if (postPanel instanceof JPanel) {
                JPanel posting = (JPanel) postPanel;
                for (Component titles : posting.getComponents()) {
                    if (titles instanceof JLabel && (titles.getName() != null && titles.getName().equals("titlePost"))) {
                        JLabel title = (JLabel) titles;
                        actualList.add(title.getText());
                    }
                }
            }
        }

        List<String> expectedList = List.of("game 2");
        Assertions.assertEquals(expectedList, actualList, "Lists have the same content and order.");
    }

    @Test
    void searchFoundMultiple() {
        String keyword = "game";
        JPanel postsPanel = new JPanel();
        BrowsePostsViewModel viewModel = createSamplePosts();
        SearchPostController searchPostController = new SearchPostController(new SearchPostInputData(postsPanel, viewModel.getState(), keyword));
        searchPostController.searchPosts();

        List<String> actualList = new ArrayList<>();

        for (Component postPanel : postsPanel.getComponents()) {
            if (postPanel instanceof JPanel) {
                JPanel posting = (JPanel) postPanel;
                for (Component titles : posting.getComponents()) {
                    if (titles instanceof JLabel && (titles.getName() != null && titles.getName().equals("titlePost"))) {
                        JLabel title = (JLabel) titles;
                        actualList.add(title.getText());
                    }
                }
            }
        }

        List<String> expectedList = List.of("game 1", "game 2", "game 4");
        Assertions.assertEquals(expectedList, actualList, "Lists have the same content and order.");
    }

    @Test
    void failSearch() {
        String keyword = "sleep";
        JPanel postsPanel = new JPanel();
        BrowsePostsViewModel viewModel = createSamplePosts();
        SearchPostController searchPostController = new SearchPostController(new SearchPostInputData(postsPanel, viewModel.getState(), keyword));
        searchPostController.searchPosts();

        List<String> actualList = new ArrayList<>();

        for (Component postPanel : postsPanel.getComponents()) {
            if (postPanel instanceof JPanel) {
                JPanel posting = (JPanel) postPanel;
                for (Component titles : posting.getComponents()) {
                    if (titles instanceof JLabel && (titles.getName() != null && titles.getName().equals("titlePost"))) {
                        JLabel title = (JLabel) titles;
                        actualList.add(title.getText());
                    }
                }
            }
        }

        List<String> expectedList = List.of();
        Assertions.assertEquals(expectedList, actualList, "Lists have the same content and order.");
    }
}
