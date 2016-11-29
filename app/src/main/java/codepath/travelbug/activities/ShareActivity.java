package codepath.travelbug.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.Utils;
import codepath.travelbug.adapter.ShareAdapter;

import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Collection<Timeline> timelines = Parcels.unwrap(getIntent().getParcelableExtra("timeline"));
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
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvShareFriends);
        // Create adapter passing in the sample user data
        ShareAdapter adapter = new ShareAdapter(this, Utils.generateFriends());
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
    }
}
