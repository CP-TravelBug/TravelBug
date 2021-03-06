package codepath.travelbug.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import codepath.travelbug.R;
import codepath.travelbug.Utils;
import codepath.travelbug.activities.ShareActivity;
import codepath.travelbug.activities.TimelineDetailsViewActivity;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;

import static codepath.travelbug.R.id.tvName;

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
    public void onBindViewHolder(final TimelineDisplayAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        // Event event = timelineList.get(position).getEventList().get(0);

        Timeline timeline = timelineList.get(position);
        Event firstEvent = timeline.getEventList().get(0);
        viewHolder.tvContent.setText(timelineList.get(position).getTimelineTitle());
        Uri uri = timeline.getCoverImageUri();
        if (uri == null) {
            uri = firstEvent.getContentUri();
        }
        Picasso.with(mContext).load(uri).centerCrop().fit().into(viewHolder.ivTimeline);

        Date timelineDate = timeline.getStartDate();
        if (timelineDate == null) {
            timelineDate = firstEvent.getEventDate();
        }
        String date = Utils.formatDate(timelineDate);
        viewHolder.datePosted.setText(date);
        viewHolder.ivTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, TimelineDetailsViewActivity.class);
                i.putExtra("timelineId", timelineList.get(position).getTimelineId());
                Pair<View, String> p1 = Pair.create((View)viewHolder.tvContent, "title");
                Pair<View, String> p2 = Pair.create((View)viewHolder.ivTimeline, "coverPhoto");
                Pair<View, String> p3 = Pair.create((View)viewHolder.gradientView, "gradient");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, p1, p2, p3);
                mContext.startActivity(i, options.toBundle());
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
        if(timeline.getUserId().equals(Backend.get().getCurrentUser().getUserId())) {
            viewHolder.btnShare.setVisibility(View.VISIBLE);
            viewHolder.ivProfileImage.setVisibility(View.GONE);
        }
        else {
            User user = Backend.get().fetchUserFor(timeline.getUserId());
            if(user.getFirstName() != null) {
                viewHolder.sharedByText.setText("Shared By " + user.getFirstName());
            }
            viewHolder.btnShare.setVisibility(View.GONE);
            String picture = user.getPicture();
            if (!picture.isEmpty()) {
                viewHolder.ivProfileImage.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(picture).into(viewHolder.ivProfileImage);
            }
        }

    }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivTimeline;
            TextView tvContent;
            TextView datePosted;
            TextView sharedByText;
            Button btnShare;
            View gradientView;
            ImageView ivProfileImage;

            public ViewHolder(View itemView) {
                super(itemView);

                ivTimeline = (ImageView) itemView.findViewById(R.id.ivPhoto);
                tvContent = (TextView) itemView.findViewById(R.id.tvContent);
                btnShare = (Button) itemView.findViewById(R.id.shareButton);
                datePosted = (TextView) itemView.findViewById(R.id.datePosted);
                sharedByText = (TextView)itemView.findViewById(R.id.sharedByText);
                gradientView = itemView.findViewById(R.id.gradientView);
                ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            }
        }
    @Override
    public int getItemCount() {
        return timelineList.size();
    }
}