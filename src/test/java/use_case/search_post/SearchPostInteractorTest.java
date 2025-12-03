package use_case.search_post;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import interface_adapter.browse_posts.BrowsePostsState;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.search_post.SearchPostController;
import use_case.browse_posts.BrowsePostsOutputData;

public class SearchPostInteractorTest {

    BrowsePostsViewModel createSamplePosts() {
        final BrowsePostsViewModel viewModel = new BrowsePostsViewModel();

        final BrowsePostsState state = new BrowsePostsState();

        final List<BrowsePostsOutputData.PostData> posts = new ArrayList<>();
        final BrowsePostsOutputData.PostData post1 = new BrowsePostsOutputData.PostData(1, "game 1",
                "game 1 content", "bob1", new Date(), 0, 0);
        final BrowsePostsOutputData.PostData post2 = new BrowsePostsOutputData.PostData(2, "game 2",
                "game 2 content", "bob1", new Date(), 2, 0);
        final BrowsePostsOutputData.PostData post3 = new BrowsePostsOutputData.PostData(3, "drey 3",
                "drey 3 content", "k3f", new Date(), 3, 1);
        final BrowsePostsOutputData.PostData post4 = new BrowsePostsOutputData.PostData(4, "game 4",
                "game 4 content", "rye2", new Date(), 0, 5);

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
        final String keyword = "game 2";
        final JPanel postsPanel = new JPanel();
        final BrowsePostsViewModel viewModel = createSamplePosts();
        final SearchPostController searchPostController = new SearchPostController(new SearchPostInputData(postsPanel,
                viewModel.getState(), keyword));
        searchPostController.searchPosts();

        final List<String> actualList = new ArrayList<>();

        for (Component postPanel : postsPanel.getComponents()) {
            if (postPanel instanceof JPanel) {
                final JPanel posting = (JPanel) postPanel;
                for (Component titlesPanel : posting.getComponents()) {
                    if (titlesPanel instanceof JPanel) {
                        final JPanel titlePanel = (JPanel) titlesPanel;
                        for (Component titles : titlePanel.getComponents()) {
                            if (titles instanceof JLabel && titles.getName() != null
                                    && titles.getName().equals("titlePost")) {
                                final JLabel title = (JLabel) titles;
                                actualList.add(title.getText());
                            }
                        }
                    }
                }
            }
        }

        final List<String> expectedList = List.of("game 2");
        Assertions.assertEquals(expectedList, actualList, "Lists have the same content and order.");
    }

    @Test
    void searchFoundMultiple() {
        final String keyword = "game";
        final JPanel postsPanel = new JPanel();
        final BrowsePostsViewModel viewModel = createSamplePosts();
        final SearchPostController searchPostController = new SearchPostController(new SearchPostInputData(postsPanel,
                viewModel.getState(), keyword));
        searchPostController.searchPosts();

        final List<String> actualList = new ArrayList<>();

        for (Component postPanel : postsPanel.getComponents()) {
            if (postPanel instanceof JPanel) {
                final JPanel posting = (JPanel) postPanel;
                for (Component titlesPanel : posting.getComponents()) {
                    if (titlesPanel instanceof JPanel) {
                        final JPanel titlePanel = (JPanel) titlesPanel;
                        for (Component titles : titlePanel.getComponents()) {
                            if (titles instanceof JLabel && titles.getName() != null
                                    && titles.getName().equals("titlePost")) {
                                final JLabel title = (JLabel) titles;
                                actualList.add(title.getText());
                            }
                        }
                    }
                }
            }
        }

        final List<String> expectedList = List.of("game 1", "game 2", "game 4");
        Assertions.assertEquals(expectedList, actualList, "Lists have the same content and order.");
    }

    @Test
    void failSearch() {
        final String keyword = "sleep";
        final JPanel postsPanel = new JPanel();
        final BrowsePostsViewModel viewModel = createSamplePosts();
        final SearchPostController searchPostController = new SearchPostController(new SearchPostInputData(postsPanel,
                viewModel.getState(), keyword));
        searchPostController.searchPosts();

        final List<String> actualList = new ArrayList<>();

        for (Component postPanel : postsPanel.getComponents()) {
            if (postPanel instanceof JPanel) {
                final JPanel posting = (JPanel) postPanel;
                for (Component titlesPanel : posting.getComponents()) {
                    if (titlesPanel instanceof JPanel) {
                        final JPanel titlePanel = (JPanel) titlesPanel;
                        for (Component titles : titlePanel.getComponents()) {
                            if (titles instanceof JLabel && titles.getName() != null
                                    && titles.getName().equals("titlePost")) {
                                final JLabel title = (JLabel) titles;
                                actualList.add(title.getText());
                            }
                        }
                    }
                }
            }
        }

        final List<String> expectedList = List.of();
        Assertions.assertEquals(expectedList, actualList, "Lists have the same content and order.");
    }
}
