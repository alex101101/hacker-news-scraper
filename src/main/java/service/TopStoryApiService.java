package service;

import com.fasterxml.jackson.core.type.TypeReference;
import model.HackerNewsStory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Consumes the Hacker News api to retrieve the Top Stories on Hacker News
 */
public class TopStoryApiService extends BasicStoryScraperService implements HackerScraperService {
    private static final String HACKER_RANK_API_URL = "https://hacker-news.firebaseio.com";
    private static final String TOP_POST_PATH = "/v0/topstories.json";
    private static final String ITEM_PATH_PREFIX = "/v0/item/";
    private static final String ITEM_PATH_POSTFIX = ".json";

    public TopStoryApiService() {
        super();
    }

    @Override
    public String scrapeSite(int noStories) throws Exception {
        List<Integer> postIds = findTopNStoryIds(noStories);
        List<HackerNewsStory> newsStories = findStoriesFromIds(postIds);
        validateStories(newsStories);
        return objectToPrettyJsonString(newsStories);
    }

    private List<Integer> findTopNStoryIds (int noStories) throws Exception {
        URL url = new URL(HACKER_RANK_API_URL + TOP_POST_PATH);
        List<Integer> topStoryIds = this.mapper.readValue(url, new TypeReference<List<Integer>>() { });
        return topStoryIds.subList(0, noStories);
    }

    private List<HackerNewsStory> findStoriesFromIds (List<Integer> postIds) throws Exception {
        List<HackerNewsStory> newsStories = new ArrayList<HackerNewsStory>();
        int count = 1;
        for (Integer postId : postIds) {
            URL url = new URL(HACKER_RANK_API_URL + ITEM_PATH_PREFIX + postId + ITEM_PATH_POSTFIX);
            HackerNewsStory newsStory = this.mapper.readValue(url, HackerNewsStory.class);
            newsStory.setRank(count++);
            newsStories.add(newsStory);
        }
        return newsStories;
    }
}
