package service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import model.HackerNewsStory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Scrapes the Hacker News front page using HtmlUnit library to get {@code List<HackerNewsStory>}
 */
public class TopStoryScraperService extends BasicStoryScraperService implements HackerScraperService {
    private static final String HACKER_RANK_URL = "https://news.ycombinator.com/news?p=";
    private static final int HACKER_RANK_RESULTS_PER_PAGE = 30;

    private WebClient client;
    private boolean errorFree;

    public TopStoryScraperService() {
        super();
        this.client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
    }

    public TopStoryScraperService(WebClient client) {
        super();
        this.client = client;
    }

    @Override
    public String scrapeSite(int noPosts) throws Exception {
        List<HackerNewsStory> newsStories = findTopStories(noPosts);
        validateStories(newsStories);
        return objectToPrettyJsonString(newsStories);
    }

    private HtmlPage getPage(int pageNo) throws Exception {
        String url = HACKER_RANK_URL + pageNo;
        return this.client.getPage(url);
    }

    private int pagesToLoad(int noPosts) {
        return (noPosts / HACKER_RANK_RESULTS_PER_PAGE + 1);
    }

    private List<HackerNewsStory> findTopStories(int noPosts) throws Exception {
        List<HackerNewsStory> newsStoriesComplete = new ArrayList<HackerNewsStory>();
        int noPages = pagesToLoad(noPosts);
        for (int pageNo = 1; pageNo <= noPages; pageNo++) {
            HtmlPage page = getPage(pageNo);
            if (pageNo == noPages) {
                int lastPageNoStories = noPosts % HACKER_RANK_RESULTS_PER_PAGE;
                newsStoriesComplete.addAll(parseFrontPageStories(page).subList(0,lastPageNoStories));
            } else {
                newsStoriesComplete.addAll(parseFrontPageStories(page));
            }
        }
        return newsStoriesComplete;
    }

    private List<HackerNewsStory> parseFrontPageStories(HtmlPage page) throws URISyntaxException {
        List<HackerNewsStory> newsStories = new ArrayList<HackerNewsStory>();
        int commentCount;

        // Aggregate the list of news stories over for loop of html table
        final HtmlTable table = (HtmlTable) page.getFirstByXPath("//table[@class='itemlist']");
        HackerNewsStory story = null;
        for (final HtmlTableRow row : table.getRows()) {
            if (row.getAttribute("class").equals("athing")) {
                story = new HackerNewsStory();
                HtmlAnchor storyLink = row.getFirstByXPath(".//a[@class='storylink']");
                int rank = extractInt(row.getFirstByXPath(".//span[@class='rank']"));
                String title = stringOrEmpty(storyLink);

                story.setTitle(title);
                story.setUri(new URI(storyLink.getHrefAttribute()));
                story.setRank(rank);
            }

            HtmlTableCell subText = row.getFirstByXPath(".//td[@class='subtext']");
            if (subText != null && story != null) {
                String author = stringOrEmpty(subText.getFirstByXPath(".//a[@class='hnuser']"));
                int points = extractInt(subText.getFirstByXPath(".//span[@class='score']"));
                List<HtmlElement> emptyATags = subText.getByXPath(".//a[not(@id) and not(@class)]");
                if (emptyATags.size() < 3) {
                    commentCount = 0;
                } else {
                    HtmlElement comments = emptyATags.get(2);

                    if (comments.getTextContent().equals("discuss")) {
                        commentCount = 0;
                    } else {
                        commentCount = extractInt(comments);
                    }
                }

                story.setAuthor(author);
                story.setPoints(points);
                story.setComments(commentCount);
                newsStories.add(story);
            }
        }
        return newsStories;
    }

    private String stringOrEmpty(HtmlElement element) {
        return (element == null) ? "empty" : element.getTextContent();
    }

    private int extractInt(HtmlElement element) {
        if (element == null) return 0;
        return Integer.parseInt(element.getTextContent().replaceAll("\\D+", ""));
    }
}
