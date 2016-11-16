package codepath.travelbug.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.models.Event;

public class TimelineDisplayAdapter extends ArrayAdapter<Event> {
    private static class ViewHolder {
        ImageView ivTimeline;
        TextView tvContent;
    }


    public TimelineDisplayAdapter(Context context, List<Event> objects) {
        super(context, R.layout.item_timeline, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_timeline, parent, false);
            holder.ivTimeline = (ImageView) convertView.findViewById(R.id.ivPhoto);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvContent.setText(event.getContent());
        Uri uri = Uri.fromFile(new File(event.getPath()));
        Picasso.with(getContext()).load(uri).into(holder.ivTimeline);
        return convertView;
    }
}
