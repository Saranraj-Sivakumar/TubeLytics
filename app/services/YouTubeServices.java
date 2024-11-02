package services;

import play.libs.ws.WSClient;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class YouTubeServices {

    private WSClient ws;

    public YouTubeServices(WSClient ws) {
        this.ws = ws;
    }

    // Fetch video descriptions from the YouTube API
    public CompletionStage<List<String>> getVideoDescriptions(String searchQuery) {
        String apiUrl = "https://www.googleapis.com/youtube/v3/search";
        String apiKey = "AIzaSyAaY-Gd4Pej_zGKFpCzYKPXj2r8ejfW5aA";

        return ws.url(apiUrl)
                .setQueryParameter("part", "snippet")
                .setQueryParameter("q", searchQuery)
                .setQueryParameter("maxResults", "50")
                .setQueryParameter("key", apiKey)
                .get()
                .thenApply(response -> {
                    // Task5: Parse the response and collect the video descriptions
                    return response.asJson().findPath("items").findValues("description")
                            .stream().map(desc -> desc.asText()).collect(Collectors.toList());
                });
    }
}
