package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.HackerNewsStory;

import java.util.List;

/**
 * Interface to service implementation to scrape data from front page of the Hacker News website
 */
public interface HackerScraperService {
    /**
     * Main method to scrape the top {@code noPosts}
     *
     * @param noPosts first noPosts to scrape from the site
     */
    public String scrapeSite(int noPosts) throws Exception;

    /**
     * Validates a list of HackerNewsStories
     *
     * @param newsStories list of {@link HackerNewsStory}
     */
    public void validateStories(List<HackerNewsStory> newsStories);

    /**
     * Uses Jackson mapper to serialize object and pretty print string
     *
     * @param newsStories list of {@link HackerNewsStory}
     */
    public String objectToPrettyJsonString(List<HackerNewsStory> newsStories) throws JsonProcessingException;

}
