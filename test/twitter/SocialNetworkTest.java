package twitter;

import org.junit.Test;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.*;

public class SocialNetworkTest {

    @Test
    public void testSocialNetwork() {
        // Test the SocialNetwork class with various test cases
        testGuessFollowsGraphEmpty();
        testInfluencersEmpty();
        testGuessFollowsGraphSingleMention();
        testGuessFollowsGraphMultipleMentions();
        testCaseInsensitiveUsernames();
        testUserCannotFollowThemselves();
    }

    @Test
    public void testGuessFollowsGraphEmpty() {
        // Test an empty list of tweets to check if the resulting follows graph is also empty
        List<Tweet> tweets = new ArrayList<>();
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.isEmpty());
    }

    @Test
    public void testInfluencersEmpty() {
        // Test an empty follows graph to check if the list of influencers is also empty
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    @Test
    public void testGuessFollowsGraphSingleMention() {
        // Test a single tweet with a mention to check if the follows graph is updated correctly
        List<Tweet> tweets = new ArrayList<>();
        Tweet tweet = new Tweet(1, "Ernie", "@Bert Hello, Bert!", Instant.now());
        tweets.add(tweet);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        // Check that Ernie is in the follows graph and mentions Bert, but Ernie is not following Bert
        assertTrue(followsGraph.containsKey("Ernie"));
        assertTrue(followsGraph.get("Ernie").contains("Bert"));
        assertFalse(followsGraph.containsKey("Bert"));
    }

    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        // Test a single tweet with multiple mentions to check if the follows graph is updated correctly
        List<Tweet> tweets = new ArrayList<>();
        Tweet tweet = new Tweet(1, "Ernie", "@Bert Hello, @Oscar and @BigBird!", Instant.now());
        tweets.add(tweet);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        // Check that Ernie is in the follows graph and mentions Bert, Oscar, and BigBird, but Ernie is not following them
        assertTrue(followsGraph.containsKey("Ernie"));
        assertTrue(followsGraph.get("Ernie").contains("Bert"));
        assertTrue(followsGraph.get("Ernie").contains("Oscar"));
        assertTrue(followsGraph.get("Ernie").contains("BigBird"));
        assertFalse(followsGraph.containsKey("Bert"));
        assertFalse(followsGraph.containsKey("Oscar"));
        assertFalse(followsGraph.containsKey("BigBird"));
    }

    @Test
    public void testCaseInsensitiveUsernames() {
        // Test a single tweet with a mention using different case to check if usernames are case-insensitive
        List<Tweet> tweets = new ArrayList<>();
        Tweet tweet = new Tweet(1, "Ernie", "@bert Hello, Bert!", Instant.now());
        tweets.add(tweet);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        // Check that Ernie (in uppercase) is in the follows graph and mentions bert (in lowercase), but they are not following each other
        assertTrue(followsGraph.containsKey("ERNIE"));
        assertTrue(followsGraph.get("ERNIE").contains("bert"));
        assertFalse(followsGraph.containsKey("bert"));
    }

    @Test
    public void testUserCannotFollowThemselves() {
        // Test a single tweet where a user mentions themselves to ensure they are not following themselves
        List<Tweet> tweets = new ArrayList<>();
        Tweet tweet = new Tweet(1, "Ernie", "@Ernie Hello, me!", Instant.now());
        tweets.add(tweet);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        // Check that Ernie is not in the follows graph
        assertFalse(followsGraph.containsKey("Ernie"));
    }
}
