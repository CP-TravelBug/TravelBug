package codepath.travelbug.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.adapter.TimelineDisplayAdapter;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;

public class ScrollingTimelineActivity extends AppCompatActivity {
    final String path = Environment.DIRECTORY_DCIM;
    ListView lvTimeline;
    LinkedList<Event> eventLinkedList;
    TimelineDisplayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        lvTimeline = (ListView) findViewById(R.id.lvTimeline);
        eventLinkedList = new LinkedList<>();
        adapter = new TimelineDisplayAdapter(this, eventLinkedList);
        lvTimeline.setAdapter(adapter);

        displayTimeline();

        if (adapter.isEmpty()) {
            Toast.makeText(this, "No timelines created yet", Toast.LENGTH_LONG);
        }
    }

    private void displayTimeline() {
        // Get Event objects and displays as timelines
        // Filler code until we read values from DB or server
        List<Timeline> timelines = Backend.get().getTimelines();
        eventLinkedList.clear();
        for (int i = 0; i < timelines.size(); i++) {
            Timeline tm = timelines.get(i);
            // ToDo add to specific timelines feature yet to be implemented
            // For now each event is added to a new timeline
            Event e = tm.getEventList().get(0);
            eventLinkedList.add(e);
        }
        //adapter.clear();
        adapter.addAll(eventLinkedList);



    }
}
