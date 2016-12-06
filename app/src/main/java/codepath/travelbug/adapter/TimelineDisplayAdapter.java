package codepath.travelbug.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import codepath.travelbug.R;
import codepath.travelbug.activities.ShareActivity;
import codepath.travelbug.activities.TimelineDetailsViewActivity;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;

public class TimelineDisplayAdapter extends
        RecyclerView.Adapter<TimelineDisplayAdapter.ViewHolder> {

    // ... constructor and member variables
    List<Timeline> timelineList;
    Context mContext;

    public TimelineDisplayAdapter(Context context, List<Timeline> timelines) {
        timelineList = timelines;
        mContext = context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TimelineDisplayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_timeline, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TimelineDisplayAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        Event event = timelineList.get(position).getEventList().get(0);

        viewHolder.tvContent.setText(timelineList.get(position).getTimelineTitle());
        Uri uri = event.getContentUri();
        Picasso.with(mContext).load(uri).resize(400 /* width */, 200).into(viewHolder.ivTimeline);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy", Locale.US);
        String date = sdf.format(event.getUpdatedAt());
        viewHolder.datePosted.setText(date);
        viewHolder.ivTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, TimelineDetailsViewActivity.class);
                i.putExtra("timelineId", timelineList.get(position).getTimelineId());
                mContext.startActivity(i);
            }
        });
        viewHolder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) mContext;
                Intent i = new Intent(mContext, ShareActivity.class);
                i.putExtra("timelineId", timelineList.get(position).getTimelineId());
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        List<Long> listOfSharedTimelineIds = Backend.get().getCurrentUser().getSharedTimelines();
        for (Long timelineId : listOfSharedTimelineIds) {
            if (timelineId == timelineList.get(position).getTimelineId()) {
                viewHolder.btnShare.setVisibility(View.GONE);
            } else {
                viewHolder.btnShare.setVisibility(View.VISIBLE);
            }
        }
    }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivTimeline;
            TextView tvContent;
            TextView datePosted;
            Button btnShare;

            public ViewHolder(View itemView) {
                super(itemView);

                ivTimeline = (ImageView) itemView.findViewById(R.id.ivPhoto);
                tvContent = (TextView) itemView.findViewById(R.id.tvContent);
                btnShare = (Button) itemView.findViewById(R.id.shareButton);
                datePosted = (TextView) itemView.findViewById(R.id.datePosted);
            }
        }
    @Override
    public int getItemCount() {
        return timelineList.size();
    }
}