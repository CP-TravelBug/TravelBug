package codepath.travelbug.models;

import com.facebook.AccessToken;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import codepath.travelbug.backend.ParseUtil;

import static android.R.attr.name;


@Parcel(analyze = User.class)
@ParseClassName("User")
public class User extends ParseObject {
    public static final String PARSE_FIELD_USERID = "userId";


    public User() {
        super();
    }

    public String getFirstName() {
        return getString("firstName");
    }
    public void setFirstName(String firstName) {
        put("firstName", firstName);
    }

    public List<Long> getFriendList() {
        return getList("friendsList");
    }

    public List<Long> getTimelines() {
        return getList("timelines");
    }

    public List<Long> getSharedTimelines() {
        return getList("sharedTimelines");
    }

    public void addToSharedTimelines(List<Long> timelines) {
        addAll("sharedTimelines", timelines);
    }

    public void addToTimelines(List<Long> timelines) {
        addAll("timelines", timelines);
    }

    public String getFullName() {
        return getString("fullName");
    }

    public void addToFriendsList(List<Long> friendList) {
        addAll("friendsList", friendList);
    }

    public String getLastName() {
        return getString("lastName");
    }

    public void setLastName(String lastName) {
        put("lastName", lastName);
    }

    public String getUserId() {
        return getString(PARSE_FIELD_USERID);
    }

    public void setUserId(String userId) {
        put(PARSE_FIELD_USERID, userId);
    }

    public static User fromJSONObject(JSONObject jsonObject) {
        if (jsonObject == null) return null;
        User user = new User();
        try {
            user.setFirstName(jsonObject.getString("first_name"));
            user.setLastName(jsonObject.getString("last_name"));
            user.setFullName(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void setFullName(String fullName) {
        put("fullName", fullName);
    }
}
