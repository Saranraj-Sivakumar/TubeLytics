package controllers;

import play.mvc.*;
import services.YouTubeServices;
import com.fasterxml.jackson.databind.JsonNode;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class HomeController extends Controller {

    private final YouTubeServices youtubeServices;

    @Inject
    public HomeController(YouTubeServices youtubeServices) {
        this.youtubeServices = youtubeServices;
    }

    // Default index method to render the main search page without any specific data
    public Result index() {
        // Pass empty/default values to match the types expected by the `index.scala.html`
        return ok(views.html.index.render("", Collections.emptyList(), 0.0, 0.0, ""));
    }

    /**
     * Method to display a YouTube channel profile based on the channel ID.
     ** @author Saranraj Sivakumar (ID:40306771)
     * @param id The ID of the YouTube channel.
     * @return Result rendering the channel profile page.
     */
    public CompletionStage<Result> showChannelProfile(String id) {
        CompletionStage<JsonNode> profile = youtubeServices.getChannelProfile(id);
        CompletionStage<List<JsonNode>> videos = youtubeServices.getChannelVideos(id);

        return profile.thenCombine(videos, (profileJson, videoList) ->
                ok(views.html.channel.render(profileJson, videoList))
        );
    }

}
