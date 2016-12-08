package codepath.travelbug.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.ParseGeoPoint;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.activities.EventViewActivity;
import codepath.travelbug.models.Event;

/**
 * @author Pragyan
 */

public class TimelineDetailsViewAdapter extends
        RecyclerView.Adapter<TimelineDetailsViewAdapter.ViewHolder>{

    private List<Event> timelineEvents;
    private Context mContext;

    public TimelineDetailsViewAdapter(Context context, List<Event> events) {
        timelineEvents = events;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEvent;
        public RoundedImageView ivEventImage;
        public ImageView ivGeoPointIcon;
        public View cvParentCardView;

        public ViewHolder(View itemView) {
            super(itemView);

            tvEvent = (TextView) itemView.findViewById(R.id.eventLabel);
            ivEventImage = (RoundedImageView) itemView.findViewById(R.id.ivEventImage);
            ivGeoPointIcon = (ImageView) itemView.findViewById(R.id.ivGeopointIcon);
            cvParentCardView = itemView;
        }
    }
    @Override
    public TimelineDetailsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View customView = inflater.inflate(R.layout.timeline_event_items, parent, false);

        ViewHolder holder = new ViewHolder(customView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TimelineDetailsViewAdapter.ViewHolder holder, int position) {
        final Event event = timelineEvents.get(position);

        TextView tvEvent = holder.tvEvent;
        tvEvent.setText(event.getContent());
        RoundedImageView ivEventImage = holder.ivEventImage;
        Picasso.with(getContext()).load(event.getContentUri()).centerCrop().fit().into(ivEventImage);
        if (event.getGeoPoint() != null) {
            holder.ivGeoPointIcon.setVisibility(View.VISIBLE);
        }

        holder.cvParentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, EventViewActivity.class);
                i.putExtra("eventTitle", event.getContent());
                i.putExtra("eventUri", event.getContentUri());
                i.putExtra("eventDate", event.getEventDate());
                if (event.getGeoPoint() != null) {
                    ParseGeoPoint geoPoint = event.getGeoPoint();
                    i.putExtra("hasGeoPoint", true);
                    i.putExtra("lat", geoPoint.getLatitude());
                    i.putExtra("lng", geoPoint.getLongitude());
                }
                Pair<View, String> p1 = Pair.create((View)holder.tvEvent, "eventText");
                Pair<View, String> p2 = Pair.create((View)holder.ivEventImage, "eventImage");
                // Pair<View, String> p3 = Pair.create((View)viewHolder.gradientView, "gradient");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, p1, p2);
                mContext.startActivity(i, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return timelineEvents.size();
    }
}
