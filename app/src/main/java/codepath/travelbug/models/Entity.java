package codepath.travelbug.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by ocarty on 11/12/2016.
 */

@Parcel
public class Entity {

    String mediaUrl;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public static Entity fromJSONObject(JSONObject jsonObject) {
        Entity entity = new Entity();
        try {
            JSONObject media = jsonObject.getJSONObject("data");
            if(media != null) {
                entity.mediaUrl = media.getString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }
}