package com.td.mreza.betterthanblog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Post> ITEMS = new ArrayList<>();
    public JSONArray posts;

    public DummyContent(JSONArray posts){
        this.posts = posts;
        for (int i = 0; i < posts.length(); i++) {
            JSONObject jsonobject = null;
            int id = 0;
            int userID = 0;
            String title = "";
            String body = "";
            try {
                jsonobject = posts.getJSONObject(i);
                id = jsonobject.getInt("id");
                userID = jsonobject.getInt("userId");
                title = jsonobject.getString("title");
                body = jsonobject.getString("body");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Post post = new Post(userID, id, title, body);
            addItem(post);

        }

    }

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Post> ITEM_MAP = new HashMap<String, Post>();

    private static final int COUNT = 25;

//    static {
        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createPost(i));
//        }
//    }

    private static void addItem(Post item) {
        ITEMS.add(item);
    }

//    private static Post createPost(int position) {
//        return new Post(String.valueOf(position), "Item " + position, makeDetails(position));
//    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Post {
        public final int id;
        public final int userID;
        public final String title;
        public final String body;

        public Post(int userID, int id, String title, String body) {
            this.id = id;
            this.userID = userID;
            this.title = title;
            this.body = body;

        }

        @Override
        public String toString() {
            return title;
        }
    }
}
