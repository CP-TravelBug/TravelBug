package codepath.travelbug.models;

import com.facebook.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by ocarty on 11/12/2016.
 */
@Parcel
public class User {

    AccessToken accessToken;

    public Entity getEntity() {
        return entity;
    }

    Entity entity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public static User fromJSONObject(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.entity = Entity.fromJSONObject(jsonObject.getJSONObject("picture"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
