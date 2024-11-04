package controllers;

import play.mvc.*;
import services.YouTubeServices;
import com.fasterxml.jackson.databind.JsonNode;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class ChannelProfileController extends Controller {

    private final YouTubeServices youtubeServices;

    @Inject
    public ChannelProfileController(YouTubeServices youtubeServices) {
        this.youtubeServices = youtubeServices;
    }

    public CompletionStage<Result> showChannelProfile(String id) {
        CompletionStage<JsonNode> profile = youtubeServices.getChannelProfile(id);
        CompletionStage<List<JsonNode>> videos = youtubeServices.getChannelVideos(id);

        return profile.thenCombine(videos, (profileJson, videoList) -> {
            return ok(views.html.channel.render(profileJson, videoList));
        });
    }
}
