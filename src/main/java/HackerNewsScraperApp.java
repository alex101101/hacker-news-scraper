import service.HackerScraperService;
import service.TopStoryApiService;
import service.TopStoryScraperService;

public class HackerNewsScraperApp {
    public static void main(String args[]) {
        CommandLineInput values = new CommandLineInput();
        values.parseInput(args);
        if (!values.isErrorFree()) {
            System.exit(1);
        }

        HackerScraperService newsScraper = new TopStoryScraperService();
        try {
            String result = newsScraper.scrapeSite(values.getPosts());
            System.out.println(result);
        } catch (Exception e) {
            //Would normally be logged
            //e.printStackTrace();
        }

    }
}
