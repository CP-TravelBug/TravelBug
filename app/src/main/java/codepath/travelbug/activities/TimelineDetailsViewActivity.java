package codepath.travelbug.activities;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.raizlabs.android.dbflow.StringUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.adapter.TimelineDetailsViewAdapter;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;

import static android.view.View.Y;

public class TimelineDetailsViewActivity extends AppCompatActivity {
    private static final String TAG = "TimelineDetailsView";
    private List<Event> events;
    private TextView tvTimelineTitle;
    private ImageView coverImageview;
    private RoundedImageView ivProfileImage;
    private CardView cvInfoView;
    private TextView tvInfoText;
    private Handler uiHandler = new Handler();

    private User timelineOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_details_view);
        long timelineId = getIntent().getLongExtra("timelineId", 0);

        tvTimelineTitle = (TextView) findViewById(R.id.timeLineTitle);
        coverImageview = (ImageView) findViewById(R.id.ivPhoto);
        ivProfileImage = (RoundedImageView)findViewById(R.id.ivProfileImage);
        cvInfoView = (CardView) findViewById(R.id.timeline_info);
        tvInfoText = (TextView) findViewById(R.id.timeline_info_text);



        RecyclerView rvEvents = (RecyclerView) findViewById(R.id.rvTimeline);

        // Get a timeline based on Id and then get the events of the timeline
        Timeline timeline = Backend.get().getTimeline(timelineId);
        if (timeline == null) {
            timeline = Backend.get().getSharedTimeline(timelineId);
        }
        String timelineTitle = timeline.getTimelineTitle();
        tvTimelineTitle.setText(timelineTitle);
        events = timeline.getEventList();
        Log.d(TAG, events.toString());

        Event firstEvent = timeline.getEventList().get(0);
        Uri uri = timeline.getCoverImageUri();
        if (uri == null) {
            uri = firstEvent.getContentUri();
        }
        Picasso.with(this).load(uri).centerCrop().fit().into(coverImageview);

        String info = timeline.getInfo();
        if (info != null && !info.isEmpty()) {
            tvInfoText.setText(info);
            cvInfoView.setVisibility(View.VISIBLE);
        }

        TimelineDetailsViewAdapter adapter = new TimelineDetailsViewAdapter(this, events);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        Backend.get().fetchUser(timeline.getUserId(), uiHandler, new Backend.ResultRunnable<User>() {
            @Override
            public void run() {
                timelineOwner = getResult();
                if(StringUtils.isNotNullOrEmpty(timelineOwner.getPicture())) {
                    Picasso.with(TimelineDetailsViewActivity.this).load(timelineOwner.getPicture()).into(ivProfileImage);
                }
            }
        });
    }
}
