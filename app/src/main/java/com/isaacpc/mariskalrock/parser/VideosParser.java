package com.isaacpc.mariskalrock.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.isaacpc.mariskalrock.bd.VideoEntity;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.utils.DateUtils;

/**
 * This is the task that will ask YouTube for a list of videos for a specified
 * user</br> This class implements Runnable meaning it will be ran on its own
 * Thread</br> Because it runs on it's own thread we need to pass in an object
 * that is notified when it has finished
 * 
 * @author paul.blundell
 */
public class VideosParser {

        private static final String LOG_TAG = "VideosParser";

        protected URL feedUrl;
        protected String feedUrlString;

        // A reference to retrieve the data when this task finishes
        public static final String LIBRARY = "Library";

        public VideosParser() {

        }

        public List<VideoEntity> parse(JSONObject json) {

                // Create a list to store are videos in
                final List<VideoEntity> itemList = new ArrayList<VideoEntity>();

                // For further information about the syntax of this request and
                // JSON-C
                // see the documentation on YouTube
                // http://code.google.com/apis/youtube/2.0/developers_guide_jsonc.html

                // Get are search result items
                JSONArray jsonArray = null;

                try {
                        jsonArray = json.getJSONObject("feed").getJSONArray("entry");

                } catch (final JSONException e) {
                        Log.e(LOG_TAG, e.getMessage());
                }

                if (jsonArray == null) {
                        return itemList;
                }

                // Loop round our JSON list of videos creating Video objects to
                // use within our app
                for (int i = 0; i < jsonArray.length(); i++) {

                        try {

                                final JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // The title of the video
                                final String title = jsonObject.getJSONObject("title").getString("$t");
                                final String description = jsonObject.getJSONObject("content").getString("$t");
                                final String duration = jsonObject.getJSONObject("media$group").getJSONObject("yt$duration").getString("seconds");
                                final String viewCount = jsonObject.getJSONObject("yt$statistics").getString("viewCount");
                                final String published = jsonObject.getJSONObject("published").getString("$t");

                                // The url link back to YouTube, this checks if
                                // it has a mobile url
                                // if it doesnt it gets the standard url

                                // url
                                // if it doesnt it gets the standard url
                                String url = null;
                                try {
                                        // TODO poner la url mobile
                                        final JSONArray link = jsonObject.getJSONArray("link");

                                        if (link != null && link.length() > 0) {

                                                url = link.getJSONObject(0).getString("href");
                                        }

                                } catch (final JSONException ignore) {
                                        ignore.printStackTrace();
                                }

                                String thumbUrl = null;

                                final JSONArray thumbArray =
                                                jsonObject.getJSONObject("media$group").getJSONArray("media$thumbnail");

                                thumbUrl = thumbArray.getJSONObject(0).getString("url");


                                final VideoEntity video = new VideoEntity();

                                video.setTitle(title);
                                video.setLogo(thumbUrl);
                                video.setDescription(description);
                                video.setImageURL(thumbUrl);
                                video.setDuration(duration);
                                video.setViewCount(viewCount);
                                video.setRating(1.0f);
                                video.setUrl(url);
                                video.setPublished(DateUtils.stringToCalendar(published).getTime());
                                video.setUploaded(video.getPublishedString());
                                video.setFavorite(false);

                                itemList.add(video);

                        } catch (final JSONException e) {
                                Log.e(LOG_TAG, e.getMessage());
                        }
                }

                return itemList;
        }
}
