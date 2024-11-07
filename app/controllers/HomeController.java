package controllers;

import play.mvc.*;
import models.ChannelProfileService;
import com.fasterxml.jackson.databind.JsonNode;
import scala.jdk.CollectionConverters;
import scala.collection.immutable.List;
import scala.collection.immutable.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.concurrent.CompletionStage;
import java.util.ArrayList;

public class HomeController extends Controller {

    private final ChannelProfileService channelProfileService;

    @Inject
    public HomeController(ChannelProfileService channelProfileService) {
        this.channelProfileService = channelProfileService;
    }

    public CompletionStage<Result> channelProfile(String id) {
        return channelProfileService.fetchChannelProfile(id).thenApply(profileData -> {
            // Prepare channel info as a Map<String, String>
            java.util.Map<String, String> channelInfo = new HashMap<>();
            JsonNode channelInfoNode = profileData.get("channelInfo").get("snippet");
            if (channelInfoNode != null) {
                channelInfo.put("title", channelInfoNode.get("title").asText());
                channelInfo.put("description", channelInfoNode.get("description").asText());
                channelInfo.put("thumbnailUrl", channelInfoNode.get("thumbnails").get("default").get("url").asText());
            }

            // Prepare videos list as List<Map<String, String>>
            java.util.List<java.util.Map<String, String>> videos = new ArrayList<>();
            JsonNode videosNode = profileData.get("videos");
            if (videosNode != null && videosNode.isArray()) {
                videos = StreamSupport.stream(videosNode.spliterator(), false)
                        .map(videoNode -> {
                            java.util.Map<String, String> video = new HashMap<>();
                            JsonNode snippet = videoNode.get("snippet");
                            if (snippet != null) {
                                video.put("title", snippet.get("title").asText());
                                video.put("description", snippet.get("description").asText());
                                video.put("videoId", videoNode.get("id").get("videoId").asText());
                                video.put("thumbnailUrl", snippet.get("thumbnails").get("default").get("url").asText());
                            }
                            return video;
                        })
                        .collect(Collectors.toList());
            }

            // Convert Java collections to Scala immutable collections
            Map<String, String> scalaChannelInfo = CollectionConverters.MapHasAsScala(channelInfo).asScala().toMap();
            List<Map<String, String>> scalaVideos = CollectionConverters.ListHasAsScala(videos)
                    .asScala()
                    .toList()
                    .map(video -> CollectionConverters.MapHasAsScala(video).asScala().toMap());

            return ok(views.html.channelProfile.render(scalaChannelInfo, scalaVideos));
        });
    }
}
