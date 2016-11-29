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

import static android.R.attr.name;


@Parcel(analyze = User.class)
@ParseClassName("User")
public class User extends ParseObject {

    // This is the user id given by the Facebook API. We also use this as the primary key in the
    // Parse database.
    String userId;
    List<Long> friendList;
    String firstName;
    String lastName;

    public User() {
        super();
        friendList = new ArrayList<>();
    }

    public String getFirstName() {
        return getString("firstName");
    }
    public void setFirstName(String firstName) {
        put("firstName", firstName);
    }

    public List<Long> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Long> friendList) {
        this.friendList = friendList;
    }

    public String getLastName() {
        return getString("lastName");
    }

    public void setLastName(String lastName) {
        put("lastName", lastName);
    }

    public String getUserId() {
        return getString("userId");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    public static User fromJSONObject(JSONObject jsonObject) {
        if (jsonObject == null) return null;
        User user = new User();
        try {
            user.setFirstName(jsonObject.getString("first_name"));
            user.setLastName(jsonObject.getString("last_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
