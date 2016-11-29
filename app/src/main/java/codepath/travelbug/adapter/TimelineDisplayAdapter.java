package codepath.travelbug.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.activities.ShareActivity;
import codepath.travelbug.activities.TimelineDetailsViewActivity;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;

public class TimelineDisplayAdapter extends ArrayAdapter<Timeline> {
    private List<Timeline> timelines;

    private static class ViewHolder {
        ImageView ivTimeline;
        TextView tvContent;
        Button btnShare;
    }


    public TimelineDisplayAdapter(Context context, List<Timeline> objects) {
        super(context, R.layout.item_timeline, objects);
        timelines = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Displaying the first event for a Timeline
        Event event = getItem(position).getEventList().get(0);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_timeline, parent, false);
            holder.ivTimeline = (ImageView) convertView.findViewById(R.id.ivPhoto);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.btnShare = (Button)convertView.findViewById(R.id.shareButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.tvContent.setText(event.getContent());
        holder.tvContent.setText(getItem(position).getTimelineTitle());
        Uri uri = event.getContentUri();
        Picasso.with(getContext()).load(uri).into(holder.ivTimeline);
        holder.ivTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TimelineDetailsViewActivity.class);
                i.putExtra("timelineId", getItem(position).getTimelineId());
                getContext().startActivity(i);
            }
        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ShareActivity.class);
                i.putExtra("timelineId", getItem(position).getTimelineId());
                getContext().startActivity(i);
            }
        });
        return convertView;
    }

    public List<Timeline> getTimelines() {
        return timelines;
    }
}
