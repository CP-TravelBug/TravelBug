package codepath.travelbug.models;

import com.facebook.AccessToken;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


@Parcel
public class User {

    AccessToken accessToken;
    String name;
    Entity entity;

    public String getFirstName() {
        return firstName;
    }

    String firstName;


    public Entity getEntity() {
        return entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
            user.firstName = jsonObject.getString("first_name");
            user.entity = Entity.fromJSONObject(jsonObject.getJSONObject("picture"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * @return A ParseUser version of this user.
     */
    public ParseUser asParseUser() {
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(name);
        return parseUser;
    }
}
