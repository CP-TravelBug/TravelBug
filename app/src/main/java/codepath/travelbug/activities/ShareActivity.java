package codepath.travelbug.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
        final List<User> listOfFriends = populateFriendsList();
        // Adds current user to list for testing, would be removed before the demo
        listOfFriends.add(Backend.get().getCurrentUser());
        final ShareAdapter adapter = new ShareAdapter(this, listOfFriends);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        rvContacts.addItemDecoration(new SimpleDividerItemDecoration(this));
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(ShareActivity.this, 2);

        // Unlike ListView, you have to explicitly give a LayoutManager to the RecyclerView to position items on the screen.
        // There are three LayoutManager provided at the moment: GridLayoutManager, StaggeredGridLayoutManager and LinearLayoutManager.
        rvContacts.setLayoutManager(gridLayoutManager);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timelineId != 0) {
                    for(User user : listOfFriends) {
                        if(user.isSelected) {
                            Backend.get().shareTimelineWithUser(timelineId, user.getUserId());
                            Toast.makeText(getApplicationContext(), "You have shared your timeline with " + user.getFullName() + "!", Toast.LENGTH_LONG).show();
                        }
                    }
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
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
