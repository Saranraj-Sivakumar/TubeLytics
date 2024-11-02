package controllers;

import play.mvc.*;
import services.YouTubeServices;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;

public class ChannelProfileController extends Controller {

    private final YouTubeServices youtubeServices;

    // Use @Inject to enable dependency injection for YouTubeServices
    @Inject
    public ChannelProfileController(YouTubeServices youtubeServices) {
        this.youtubeServices = youtubeServices;
    }

    public CompletionStage<Result> showChannelProfile(String id) {
        return youtubeServices.getChannelProfile(id)
                .thenApply(json -> ok(views.html.channel.render(json))); // Render the JSON data in the view
    }
}
