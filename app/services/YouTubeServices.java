package services;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import com.fasterxml.jackson.databind.JsonNode;

import javax.inject.Inject;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class YouTubeServices {

    private final WSClient ws;
    private final String apiKey = "AIzaSyAaY-Gd4Pej_zGKFpCzYKPXj2r8ejfW5aA"; // Replace with your actual API key
    private final String ytApiUrl = "https://www.googleapis.com/youtube/v3";

    @Inject
    public YouTubeServices(WSClient ws) {
        this.ws = ws;
    }

    /**
     * Retrieves the profile information of a YouTube channel by ID.
     ** @author Saranraj Sivakumar (ID:40306771)
     * @param channelId The channel ID.
     * @return CompletionStage containing JsonNode with channel information.
     */
    public CompletionStage<JsonNode> getChannelProfile(String channelId) {
        String apiUrl = ytApiUrl + "/channels";

        return ws.url(apiUrl)
                .setQueryParameter("part", "snippet,contentDetails,statistics")
                .setQueryParameter("id", channelId)
                .setQueryParameter("key", apiKey)
                .get()
                .thenApply(WSResponse::asJson);  // Return the JSON response as a JsonNode
    }


    public CompletionStage<List<JsonNode>> getChannelVideos(String channelId) {
        String apiUrl = ytApiUrl + "/search";

        return ws.url(apiUrl)
                .setQueryParameter("part", "snippet")
                .setQueryParameter("channelId", channelId)
                .setQueryParameter("maxResults", "10")
                .setQueryParameter("order", "date")
                .setQueryParameter("key", apiKey)
                .get()
                .thenApply(response -> {
                    List<JsonNode> videos = new ArrayList<>();
                    response.asJson().findPath("items").elements().forEachRemaining(videos::add);
                    return videos.stream()
                            .map(item -> item.get("snippet"))
                            .collect(Collectors.toList());
                });
    }
}
