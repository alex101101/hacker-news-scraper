package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.*;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

public class TopStoryScraperServiceTest {
    protected WebClient client;

    @Before
    public void setBasePage() throws Exception {
        String html = "<html>\n" +
                "   <table class=\"itemlist\">\n" +
                "      <tr class='athing'>\n" +
                "         <td class=\"title\"><span class=\"rank\">1.</span></td>\n" +
                "         <td class=\"title\"><a href=\"https://cloud.google.com/blog/products/gcp/introducing-headless-chrome-support-in-cloud-functions-and-app-engine\" class=\"storylink\">Headless Chrome support in Cloud Functions and App Engine</a><</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "         <td class=\"subtext\">\n" +
                "            <span class=\"score\">132 points</span> by <a href=\"user?id=idoco\" class=\"hnuser\">idoco</a><a><a></a></a>x<a href=\"item?id=17795626\">26 comments</a>              \n" +
                "         </td>\n" +
                "      </tr>\n" +
                "      <tr class=\"spacer\" style=\"height:5px\"></tr>\n" +
                "   </table>\n" +
                "</html>";
        this.client = new WebClient();
        MockWebConnection connection = new MockWebConnection();
        connection.setDefaultResponse(html);
        this.client.setWebConnection(connection);
    }

    @Test
    public void validHackerStoryTest() throws JsonProcessingException, Exception {
        TopStoryScraperService service = new TopStoryScraperService(client);

        String result = service.scrapeSite(1);

        Assert.assertEquals(result,"[ {\n" +
                "  \"title\" : \"Headless Chrome support in Cloud Functions and App Engine\",\n" +
                "  \"uri\" : \"https://cloud.google.com/blog/products/gcp/introducing-headless-chrome-support-in-cloud-functions-and-app-engine\",\n" +
                "  \"author\" : \"idoco\",\n" +
                "  \"points\" : 132,\n" +
                "  \"comments\" : 26,\n" +
                "  \"rank\" : 1\n" +
                "} ]");
    }

    @Test(expected = ConstraintViolationException.class)
    public void invalidUriStoryTest() throws Exception {
        HtmlPage page = this.client.getPage("https://news.ycombinator.com/news?p=1");
        DomElement element = page.getFirstByXPath(".//a[@class='storylink']");
        element.setAttribute("href", "httQs://cloud.google.co");

        MockWebConnection connection = (MockWebConnection) client.getWebConnection();
        connection.setDefaultResponse(page.asXml());
        client.setWebConnection(connection);

        TopStoryScraperService service = new TopStoryScraperService(client);

        String result = service.scrapeSite(1);
    }

    @Test(expected = ConstraintViolationException.class)
    public void titleTooLongTest() throws Exception {
        HtmlPage page = this.client.getPage("https://news.ycombinator.com/news?p=1");
        DomElement element = page.getFirstByXPath(".//a[@class='storylink']");
        element.setTextContent("titlettttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");

        MockWebConnection connection = (MockWebConnection) client.getWebConnection();
        connection.setDefaultResponse(page.asXml());
        client.setWebConnection(connection);

        TopStoryScraperService service = new TopStoryScraperService(client);

        String result = service.scrapeSite(1);
    }
}
