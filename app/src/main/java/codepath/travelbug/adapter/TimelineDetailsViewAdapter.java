package codepath.travelbug.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import codepath.travelbug.R;
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

        public ViewHolder(View itemView) {
            super(itemView);

            tvEvent = (TextView) itemView.findViewById(R.id.eventLabel);
            ivEventImage = (RoundedImageView) itemView.findViewById(R.id.ivEventImage);
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
    public void onBindViewHolder(TimelineDetailsViewAdapter.ViewHolder holder, int position) {
        Event event = timelineEvents.get(position);

        TextView tvEvent = holder.tvEvent;
        tvEvent.setText(event.getContent());
        RoundedImageView ivEventImage = holder.ivEventImage;
        Picasso.with(getContext()).load(event.getContentUri()).into(ivEventImage);
    }

    @Override
    public int getItemCount() {
        return timelineEvents.size();
    }
}
