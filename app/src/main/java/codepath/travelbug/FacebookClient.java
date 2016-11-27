package codepath.travelbug;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.io.IOException;

import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.User;

import static android.R.attr.id;
import static codepath.travelbug.R.id.tvName;
import static codepath.travelbug.TravelBugApplication.TAG;

/**
 * A single point for all Facebook related API calls.
 */
public class FacebookClient {
    private static final int PIC_SIZE = 400; // Note height = width for a square picture.

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
                        pictureUrlCallback.onResult(fetchPicturUrlFromResponse(response));
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

    private static String fetchPicturUrlFromResponse(GraphResponse response) {
        try {
            return response.getJSONObject().getJSONObject("data").getString("url");
        } catch (JSONException e) {
            return "";
        }
    }

    public static void fetchUser(final ResultCallback<User> userCallback) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
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

                        User user = User.fromJSONObject(response.getJSONObject());
                        userCallback.onResult(user);
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,first_name,last_name,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
