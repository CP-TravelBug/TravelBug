package codepath.travelbug;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import codepath.travelbug.models.User;

import static codepath.travelbug.TravelBugApplication.TAG;

/**
 * A single point for all Facebook related API calls.
 */
public class FacebookClient {
    private static final int PIC_SIZE = 400; // Note height = width for a square picture.
    private static final String EMPTY_STRING = "";

    /** List of permissions to request when logging in for our app. */
    public static final List<String> FACEBOOK_PERMISSIONS = Arrays.asList("public_profile", "user_friends", "read_custom_friendlists");

    private static AccessToken accessToken;

    private FacebookClient() {}  // Cannot instantiate.
    /**
     * Generic result callback for facebook APIs that fetch data from the facebook servers.
     * A null result indicates some sort of failure.
     */
    public interface ResultCallback<R> {
        void onResult(R result);
    }

    public static void fetchUserPictureAtHighRes(final ResultCallback<String> pictureUrlCallback) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            pictureUrlCallback.onResult("");
            return;
        }
        /* make the API call */
        GraphRequest request = new GraphRequest(
                accessToken,
                "/" + accessToken.getUserId() + "/picture",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "GraphResponse for fetchUserPictureAtHighRes: " + response.toString());
                        pictureUrlCallback.onResult(fetchPictureUrlFromResponse(response));
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putInt("height", PIC_SIZE);
        parameters.putInt("width", PIC_SIZE);
        parameters.putBoolean("redirect", false);
        request.setParameters(parameters);
        request.executeAsync();
    }

    private static String fetchPictureUrlFromResponse(GraphResponse response) {
        try {
            if (response == null) {
                return EMPTY_STRING;
            }
            JSONObject objectA = response.getJSONObject();
            if (objectA == null) {
                return EMPTY_STRING;
            }
            JSONObject dataObject = objectA.getJSONObject("data");
            if (dataObject == null) {
                return EMPTY_STRING;
            }
            String urlValue = dataObject.getString("url");
            return urlValue != null ? urlValue : EMPTY_STRING;
        } catch (JSONException e) {
            return "";
        }
    }

    public static void fetchUser(final ResultCallback<User> userCallback) {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            Log.e(TAG, "Null access token, dropping fetchUser() request.");
            userCallback.onResult(null);
            return;
        }
        GraphRequest request = new GraphRequest(
                accessToken,
                accessToken.getUserId(),
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            User user = User.fromJSONObject(response.getJSONObject());
                            user.setUserId(accessToken.getUserId());
                            userCallback.onResult(user);
                        } else {
                            Log.e(TAG, "Received null response from Facebook API for fetchUser().");
                            userCallback.onResult(null);
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,first_name,last_name,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public static synchronized  void setAccessToken(AccessToken token) {
        accessToken = token;
    }

    public static synchronized AccessToken accessToken() {
        return accessToken;
    }
}
