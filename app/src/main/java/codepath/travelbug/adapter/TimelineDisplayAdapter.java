package codepath.travelbug.adapter;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.List;

import codepath.travelbug.models.Event;

public class TimelineDisplayAdapter extends ArrayAdapter<Event> {
    public TimelineDisplayAdapter(Context context, int resource, List<Event> objects) {
        super(context, resource, objects);
    }
}
