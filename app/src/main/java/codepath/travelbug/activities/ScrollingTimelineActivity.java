package codepath.travelbug.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import java.util.LinkedList;

import codepath.travelbug.R;
import codepath.travelbug.adapter.TimelineDisplayAdapter;
import codepath.travelbug.adapter.ViewPagerFragmentAdapter;
import codepath.travelbug.models.Event;

public class ScrollingTimelineActivity extends AppCompatActivity {
        //implements ViewPagerFragment.OnFragmentInteractionListener{
    LinkedList<Event> eventLinkedList;
    TimelineDisplayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_timeline);

        // Get View Pager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager()));

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

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
}
