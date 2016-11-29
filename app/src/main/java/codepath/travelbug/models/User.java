package codepath.travelbug.models;

import com.facebook.AccessToken;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;


@Parcel
public class User {
    String name;

    // This is the user id given by the Facebook API. We also use this as the primary key in the
    // Parse database.
    String userId;
    Entity entity;
    List<User> friendList;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String firstName;

    public List<User> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }


    public Entity getEntity() {
        return entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static User fromJSONObject(JSONObject jsonObject) {
        if (jsonObject == null) return null;
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
