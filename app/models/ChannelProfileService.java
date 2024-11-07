package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class ChannelProfileService {

    private final YouTubeService youTubeService;
    private final ObjectMapper objectMapper = new ObjectMapper();  // Create an instance of ObjectMapper

    @Inject
    public ChannelProfileService(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    // Fetches channel profile and last 10 videos
    public CompletionStage<JsonNode> fetchChannelProfile(String channelId) {
        // Fetch the channel details using YouTubeService
        CompletionStage<JsonNode> channelDetails = youTubeService.fetchChannelDetails(channelId);
        CompletionStage<JsonNode> channelVideos = youTubeService.fetchChannelVideos(channelId);

        // Combine the results when both requests complete
        return channelDetails.thenCombine(channelVideos, (details, videos) -> {
            return createCombinedProfileJson(details, videos);
        });
    }

    // Helper method to combine channel details and video items into a single JSON structure
    private JsonNode createCombinedProfileJson(JsonNode channelInfo, JsonNode videoItems) {
        ObjectNode profileJson = objectMapper.createObjectNode();  // Use ObjectMapper to create ObjectNode
        profileJson.set("channelInfo", channelInfo);
        profileJson.set("videos", videoItems);
        return profileJson;
    }
}
