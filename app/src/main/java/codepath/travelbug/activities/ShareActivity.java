package codepath.travelbug.activities;

import android.content.Intent;
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

import org.parceler.Parcels;

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
                    boolean  isAtLeastOneFriendSelected = false;
                    for(User user : listOfFriends) {
                        if(user.isSelected) {
                            isAtLeastOneFriendSelected = true;
                            Backend.get().shareTimelineWithUser(timelineId, user.getUserId());
                            Toast.makeText(getApplicationContext(), "You have shared your timeline with " + user.getFullName() + "!", Toast.LENGTH_LONG).show();
                        }
                    }
                    if(!isAtLeastOneFriendSelected) {
                        Toast.makeText(getApplicationContext(), "Please select at least one friend to share with", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(ShareActivity.this, ScrollingTimelineActivity.class);
                    intent.putExtra("user", Parcels.wrap(Backend.get().getCurrentUser()));
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_out, R.anim.left_in);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<User> populateFriendsList() {
        List<String> friendListUserIds = Backend.get().getCurrentUser().getFriendList();
        List<User> friendList = new ArrayList<>();
        for(String userId: friendListUserIds) {
            User friendUser = Backend.get().checkUserCache(userId);
            if (friendUser == null) {
                friendUser = Backend.get().fetchUserFor(userId);
            }
            friendList.add(friendUser);
        }
        return friendList;
    }
}
