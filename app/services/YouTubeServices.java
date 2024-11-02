package services;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class YouTubeServices {

    private final WSClient ws;
    private final String apiKey = "AIzaSyAaY-Gd4Pej_zGKFpCzYKPXj2r8ejfW5aA";
    private final String ytApiUrl = "https://www.googleapis.com/youtube/v3";

    // Use @Inject to enable dependency injection for WSClient
    @Inject
    public YouTubeServices(WSClient ws) {
        this.ws = ws;
    }

    // Fetch video descriptions from the YouTube API
    public CompletionStage<List<String>> getVideoDescriptions(String searchQuery) {
        String apiUrl = ytApiUrl + "/search";

        return ws.url(apiUrl)
                .setQueryParameter("part", "snippet")
                .setQueryParameter("q", searchQuery)
                .setQueryParameter("maxResults", "50")
                .setQueryParameter("key", apiKey)
                .get()
                .thenApply(response -> {
                    // Parse the response and collect the video descriptions
                    return response.asJson().findPath("items").findValues("description")
                            .stream().map(desc -> desc.asText()).collect(Collectors.toList());
                });
    }

    // Fetch channel profile information from the YouTube API
    public CompletionStage<JsonNode> getChannelProfile(String channelId) {
        String apiUrl = ytApiUrl + "/channels";

        return ws.url(apiUrl)
                .setQueryParameter("part", "snippet,contentDetails,statistics")
                .setQueryParameter("id", channelId)
                .setQueryParameter("key", apiKey)
                .get()
                .thenApply(WSResponse::asJson);  // Return the JSON response as a JsonNode
    }
}
