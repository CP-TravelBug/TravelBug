package codepath.travelbug.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;
import android.util.Log;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

import codepath.travelbug.FacebookClient;
import codepath.travelbug.R;
import codepath.travelbug.Utils;
import codepath.travelbug.adapter.TimelineDisplayAdapter;
import codepath.travelbug.adapter.ViewPagerFragmentAdapter;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;
import static codepath.travelbug.Utils.PIC_URI_KEY;
import static codepath.travelbug.Utils.TAG;

public class ScrollingTimelineActivity extends AppCompatActivity {

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int CREATE_TIMELINE_WITH_PIC_REQUEST_CODE = 1001;

    private Uri lastCameraRequestUri;

    RoundedImageView ivProfileImage;
    TextView tvName;
    FloatingActionButton floatingActionButton;
    ViewPagerFragmentAdapter fadapter;
    LinkedList<Event> eventLinkedList;
    TimelineDisplayAdapter adapter;
    ViewPager viewPager;
    PagerSlidingTabStrip tabStrip;
    Toolbar toolbar;
    AccountHeader headerResult;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_timeline);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        activity = this;

        // Get View Pager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fadapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fadapter);
        loadHeader();
        setUpNavDrawer(this, headerResult, toolbar);

        // Give the PagerSlidingTab the viewpager
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Call into the fragment manager to display the different timelines
//                Toast.makeText(getApplication(), "Call the fragments to be displayed here", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ivProfileImage = (RoundedImageView)findViewById(R.id.ivProfileImage);
        tvName = (TextView)findViewById(R.id.tvName);
        // Performs a GET request to get the user's info
        FacebookClient.fetchUser(new FacebookClient.ResultCallback<User>() {
            @Override
            public void onResult(User user) {
                if (user != null) {
                    String helloTextWithFirstName = "Hello " + user.getFirstName();
                    tvName.setText(helloTextWithFirstName);
                    Backend.get().setCurrentUser(user);
                }
            }
        });

        // This fetches the high res image.
        FacebookClient.fetchUserPictureAtHighRes(new FacebookClient.ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                if (!result.isEmpty()) {
                    Picasso.with(ScrollingTimelineActivity.this).load(result).into(ivProfileImage);
                }
            }
        });

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_createTimeline);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCreateTimelineWithCamera();
            }
        });
    }

    private void setUpNavDrawer(ScrollingTimelineActivity scrollingTimelineActivity, AccountHeader headerResult, Toolbar toolbar) {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Log Out");
        new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(scrollingTimelineActivity)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return true;
                    }
                })
                .build();
    }

    // Launches the camera first to take a picture which is then passed to the
    //  for creating a new timeline.
    private void launchCreateTimelineWithCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        lastCameraRequestUri = Utils.getPhotoFileUri(this, Utils.generateUniqueFileName());
        Log.d(TAG, "Image file URI:" + lastCameraRequestUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, lastCameraRequestUri); // set the image file name

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void loadHeader() {
        final User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        tvName = (TextView)findViewById(R.id.tvName);
        ivProfileImage = (RoundedImageView) findViewById(R.id.ivProfileImage);
        final String helloTextWithFirstName = "Hello " + user.getFirstName();
        tvName.setText(helloTextWithFirstName);
        //

        // This fetches the high res image.
        FacebookClient.fetchUserPictureAtHighRes(new FacebookClient.ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                if (!result.isEmpty()) {
                    Picasso.with(getApplicationContext()).load(result).into(ivProfileImage);
                    headerResult = new AccountHeaderBuilder()
                            .withActivity(activity)
                            .withHeaderBackground(ivProfileImage.getDrawable())
                            .addProfiles(
                                    new ProfileDrawerItem().withName(user.getFullName()).withIcon(getResources().getDrawable(R.drawable.travelicon))
                            )
                            .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                                @Override
                                public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                                    return false;
                                }
                            })
                            .build();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = lastCameraRequestUri;
                Intent i = new Intent(this, CreateTimelineActivity.class);
                i.putExtra(PIC_URI_KEY, takenPhotoUri);
                startActivityForResult(i, CREATE_TIMELINE_WITH_PIC_REQUEST_CODE);
                // by this point we have the camera photo on disk
                // Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                // ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                // ivPreview.setImageBitmap(takenImage);

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CREATE_TIMELINE_WITH_PIC_REQUEST_CODE) {
            // ToDo: Refresh the views
            long idOfTimelineCreated = data.getLongExtra("idOfTimelineCreated", 0);
            Timeline tm = Backend.get().getTimeline(idOfTimelineCreated);
            // Toast.makeText(this, Long.toString(idOfTimelineCreated), Toast.LENGTH_LONG).show();
            fadapter.refreshAllTimelines();
        }
    }
}
