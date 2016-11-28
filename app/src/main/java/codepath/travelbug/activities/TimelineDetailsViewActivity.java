package codepath.travelbug.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.adapter.TimelineDisplayAdapter;
import codepath.travelbug.adapter.TimelineRecyclerViewAdapter;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.Event;

public class TimelineDetailsViewActivity extends AppCompatActivity {
    private static final String TAG = "TimelineDetailsView";
    private List<Event> events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_details_view);
        long timelineId = getIntent().getLongExtra("timelineId", 0);
        RecyclerView rvEvents = (RecyclerView) findViewById(R.id.rvTimeline);
        // Get a timeline based on Id and then get the events of the timeline
        events = Backend.get().getTimeline(timelineId).getEventList();
        Log.d(TAG, events.toString());

        TimelineRecyclerViewAdapter adapter = new TimelineRecyclerViewAdapter(this, events);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
    }
}
