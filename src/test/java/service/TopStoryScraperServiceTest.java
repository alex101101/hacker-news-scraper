package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.*;

public class TopStoryScraperServiceTest {

    @Test
    public void validHackerStoryTest() throws JsonProcessingException, Exception {
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
        WebClient client = new WebClient();
        MockWebConnection connection = new MockWebConnection();
        connection.setDefaultResponse(html);
        client.setWebConnection(connection);
        HtmlPage page = client.getPage("https://news.ycombinator.com/news?p=1");

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
}
