package codepath.travelbug.activities;

import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import codepath.travelbug.R;
import codepath.travelbug.models.User;

public class TimelineActivity extends AppCompatActivity {
    ImageView ivProfileImage;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        tvName = (TextView)findViewById(R.id.tvName);
        // Performs a GET request to get the user's info
        if(AccessToken.getCurrentAccessToken() != null) {
            String userId = AccessToken.getCurrentAccessToken().getUserId();
            GraphRequest request = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    userId,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            User user = User.fromJSONObject(response.getJSONObject());
                            tvName.setText(user.getName());
                            Picasso.with(getApplicationContext()).load(user.getEntity().getMediaUrl()).into(ivProfileImage);
                        }
                    }
            );
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,email,first_name,last_name,picture");
            request.setParameters(parameters);
            request.executeAsync();
        }


    }
}
