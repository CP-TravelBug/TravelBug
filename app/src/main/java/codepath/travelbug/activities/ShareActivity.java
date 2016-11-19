package codepath.travelbug.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        List<Timeline> timelines = Parcels.unwrap(getIntent().getParcelableExtra("timeline"));
        if(AccessToken.getCurrentAccessToken() != null) {
            String userId = AccessToken.getCurrentAccessToken().getUserId();
            userId = userId + "/friends";
            GraphRequest request = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    userId,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Log.d("DEBUG", response.toString());
                        }

                    }
            );
            Bundle parameters = new Bundle();
            request.setParameters(parameters);
            request.executeAsync();
        }
    }
}
