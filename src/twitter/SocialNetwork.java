package twitter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class SocialNetwork {

    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();

        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor();
            String text = tweet.getText();

            Set<String> mentionedUsers = extractMentionedUsers(text);

            // Ensure the author is not following themselves
            mentionedUsers.remove(author);

            if (!followsGraph.containsKey(author)) {
                followsGraph.put(author, new HashSet<>());
            }

            followsGraph.get(author).addAll(mentionedUsers);
        }

        return followsGraph;
    }

    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCounts = new HashMap<>();

        for (String user : followsGraph.keySet()) {
            for (String follower : followsGraph.get(user)) {
                followerCounts.put(follower, followerCounts.getOrDefault(follower, 0) + 1);
            }
        }

        List<String> influencers = followerCounts.keySet()
                .stream()
                .sorted((a, b) -> Integer.compare(followerCounts.get(b), followerCounts.get(a)))
                .toList();

        return influencers;
    }

    private static Set<String> extractMentionedUsers(String text) {
        Set<String> mentionedUsers = new HashSet<>();

        String[] words = text.split(" ");
        for (String word : words) {
            if (word.startsWith("@")) {
                String username = word.substring(1).toLowerCase();
                mentionedUsers.add(username);
            }
        }

        return mentionedUsers;
    }
}
