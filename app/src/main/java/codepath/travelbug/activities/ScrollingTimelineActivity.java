package codepath.travelbug.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.LinkedList;

import codepath.travelbug.FacebookClient;
import codepath.travelbug.R;
import codepath.travelbug.adapter.TimelineDisplayAdapter;
import codepath.travelbug.adapter.ViewPagerFragmentAdapter;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.User;

import static codepath.travelbug.R.id.tvName;

public class ScrollingTimelineActivity extends AppCompatActivity {
        //implements ViewPagerFragment.OnFragmentInteractionListener{
    LinkedList<Event> eventLinkedList;
    TimelineDisplayAdapter adapter;
    TextView tvName;
    ImageView ivProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_timeline);

        // Get View Pager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager()));
        loadHeader();

        // Give the PagerSlidingTab the viewpager
        final PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
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



        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //        Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
        //        intent.putExtra("timeline", Parcels.wrap(eventLinkedList));
        //        startActivity(intent);
        //    }
        //});

    }

    private void loadHeader() {
        User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        tvName = (TextView)findViewById(R.id.tvName);
        ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        String helloTextWithFirstName = "Hello " + user.getFirstName();
        tvName.setText(helloTextWithFirstName);
        // This fetches the high res image.
        FacebookClient.fetchUserPictureAtHighRes(new FacebookClient.ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                if (!result.isEmpty()) {
                    Picasso.with(getApplicationContext()).load(result).into(ivProfileImage);
                }
            }
        });
    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
}
