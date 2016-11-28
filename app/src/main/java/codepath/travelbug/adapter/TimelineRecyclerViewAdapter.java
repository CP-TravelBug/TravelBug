package codepath.travelbug.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;

/**
 * @author Pragyan
 */

public class TimelineRecyclerViewAdapter extends
        RecyclerView.Adapter<TimelineRecyclerViewAdapter.ViewHolder>{

    private List<Event> timelineEvents;
    private Context mContext;

    public TimelineRecyclerViewAdapter(Context context, List<Event> events) {
        timelineEvents = events;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEvent;

        public ViewHolder(View itemView) {
            super(itemView);

            tvEvent = (TextView) itemView.findViewById(R.id.eventLabel);
        }
    }
    @Override
    public TimelineRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View customView = inflater.inflate(R.layout.timeline_event_items, parent, false);

        ViewHolder holder = new ViewHolder(customView);
        return holder;
    }

    @Override
    public void onBindViewHolder(TimelineRecyclerViewAdapter.ViewHolder holder, int position) {
        Event event = timelineEvents.get(position);

        TextView tvEvent = holder.tvEvent;
        tvEvent.setText(event.getContent());
    }

    @Override
    public int getItemCount() {
        return timelineEvents.size();
    }
}
