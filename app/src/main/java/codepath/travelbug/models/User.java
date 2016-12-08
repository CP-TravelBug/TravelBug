package codepath.travelbug.models;

import android.util.Log;

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

import static codepath.travelbug.TravelBugApplication.TAG;

@Parcel(analyze = User.class)
@ParseClassName("User")
public class User extends ParseObject {
    public static final String PARSE_FIELD_USERID = "userId";

    public boolean isSelected;

    public String fbPictureUrl;
    public String localPicturePath;
    public String highResPicturePath;


    public User() {
        super();
    }

    public String getFirstName() {
        return getString("firstName");
    }
    public void setFirstName(String firstName) {
        put("firstName", firstName);
    }

    public List<String> getFriendList() {
        return getList("friendsList");
    }

    public List<Long> getTimelines() {
        return getList("timelines");
    }

    public List<Long> getSharedTimelines() {
        return getList("sharedTimelines");
    }

    public void addToSharedTimelines(List<Long> timelines) {
        addAllUnique("sharedTimelines", timelines);
    }

    public void addProfileHint(int hint) {
        put("profileHint", hint);
    }

    public int getProfileHint() {
        return getInt("profileHint");
    }

    public void addToTimelines(List<Long> timelines) {
        addAllUnique("timelines", timelines);
    }

    public void addTimeline(long timelineId) {
        addUnique("timelines", timelineId);
    }

    public void addSharedTimeline(long timelineId) {
        addUnique("sharedTimelines", timelineId);
    }

    public String getFullName() {
        return getString("fullName");
    }

    public void addToFriendsList(List<String> friendList) {
        addAllUnique("friendsList", friendList);
    }

    public void addFriend(String friendId) { addUnique("friendsList", friendId);}

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

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public static User fromJSONObject(JSONObject jsonObject) {
        if (jsonObject == null) return null;
        User user = new User();
        try {
            user.setFirstName(jsonObject.getString("first_name"));
            user.setLastName(jsonObject.getString("last_name"));
            user.setFullName(jsonObject.getString("name"));
            JSONObject pictureObject = jsonObject.getJSONObject("picture");
            if (pictureObject != null) {
                JSONObject data = pictureObject.getJSONObject("data");
                if (data != null) {
                    user.fbPictureUrl = data.getString("url");
                    Log.i(TAG, "fb picture: " + user.fbPictureUrl);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void setFullName(String fullName) {
        put("fullName", fullName);
    }

    public String getPicture() {
        if (fbPictureUrl != null) return fbPictureUrl;
        if (localPicturePath != null) return localPicturePath;
        return "";
    }

    public String getPicturePreferHighRes() {
        if (highResPicturePath != null) return  highResPicturePath;
        if (fbPictureUrl != null) return fbPictureUrl;
        if (localPicturePath != null) return localPicturePath;
        return "";
    }
}
