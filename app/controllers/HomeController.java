package controllers;

import play.mvc.*;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

public class HomeController extends Controller {

    @Inject
    public HomeController() {
        // If youtubeServices is no longer needed, you can remove it
    }

    // Default index method to render the main search page without any specific data
    public Result index() {
        // Pass empty/default values to match the types expected by the `index.scala.html`
        return ok(views.html.index.render("", Collections.emptyList(), 0.0, 0.0, ""));
    }
}
