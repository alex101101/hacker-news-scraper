import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Class to read and validate input to the {@link HackerNewsScraperApp}
 */
public class CommandLineInput {
    @Option(name = "--posts", required = true,
            usage = "--posts how many posts to print. A positive integer <= 100")
    private int posts;
    private boolean errorFree;
    private CmdLineParser parser;

    public CommandLineInput() {
        parser = new CmdLineParser(this);
        errorFree = false;

    }

    public void parseInput(String... args) {
        try {
            parser.parseArgument(args);

            if (getPosts() < 0 || getPosts() >= 100) {
                throw new CmdLineException(parser,
                        new Throwable("--posts is not a valid integer"));
            }

            errorFree = true;
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
        }
    }

    /**
     * Returns whether the parameters could be parsed without an
     * error.
     *
     * @return true if no error occurred.
     */
    public boolean isErrorFree() {
        return errorFree;
    }

    /**
     * Returns the number of posts.
     *
     * @return The number of posts.
     */
    public int getPosts() {
        return posts;
    }
}
