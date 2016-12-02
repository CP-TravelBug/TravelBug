package codepath.travelbug.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.Utils;
import codepath.travelbug.activities.decorators.SimpleDividerItemDecoration;
import codepath.travelbug.adapter.ShareAdapter;

import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.User;

public class ShareActivity extends AppCompatActivity {
    Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        shareButton = (Button)findViewById(R.id.btnShareWithFriends);

        final Long timelineId = getIntent().getLongExtra("timelineId", 0);
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
        //Backend.get().getCurrentUser().getFriendList()
        // Create adapter passing in the sample user data
        final ShareAdapter adapter = new ShareAdapter(this, populateFriendsList());
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        rvContacts.addItemDecoration(new SimpleDividerItemDecoration(this));
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timelineId != 0) {
                    Backend.get().shareTimelineWithUser(timelineId, Backend.get().getCurrentUser().getUserId());
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private List<User> populateFriendsList() {
        List<String> friendListUserIds = Backend.get().getCurrentUser().getFriendList();
        List<User> friendList = new ArrayList<>();
        for(String userId: friendListUserIds) {
            friendList.add(Backend.get().fetchUserFor(userId));
        }
        return friendList;
    }
}
