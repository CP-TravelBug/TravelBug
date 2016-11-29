package codepath.travelbug.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.activities.ShareActivity;
import codepath.travelbug.adapter.TimelineDisplayAdapter;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ViewPagerFragment extends Fragment {

    private static final String PAGE_NUM = "PAGE_NUM";
    //private OnFragmentInteractionListener mListener;
    private int mPage;
    final String path = Environment.DIRECTORY_DCIM;
    ListView lvTimeline;
    List<Timeline> timelineList;
    TimelineDisplayAdapter adapter;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ViewPagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerFragment newInstance(int mPage) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_NUM, mPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(PAGE_NUM);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_fragment, container, false);
        lvTimeline = (ListView) view.findViewById(R.id.lvTimeline);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.shareButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
                startActivity(intent);
            }
        });

        timelineList = new LinkedList<>();
        adapter = new TimelineDisplayAdapter(getContext(), timelineList);
        lvTimeline.setAdapter(adapter);
        if (mPage == 1) {
            displayAllTimelines();
        } else {
            displayMyTimeline();
        }
        return view;
    }

    private void displayAllTimelines() {
        //Toast.makeText(getContext(), "Displaying all timelines", Toast.LENGTH_SHORT).show();
        displayMyTimeline();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void displayMyTimeline() {
        // Get Event objects and displays as timelines
        // Filler code until we read values from DB or server
        Collection<Timeline> timelines = Backend.get().getTimelines();
        if (timelines.size() == 0) {
            //Toast.makeText(getActivity(), "No timelines created yet", Toast.LENGTH_LONG).show();
            // Todo Display a message to create timeline
        }
        if (timelineList != null) {
            timelineList.clear();
        } else {
            timelineList = new LinkedList<>();
        }
        timelineList.addAll(timelines);
        adapter.notifyDataSetChanged();
    }

    public ListView getListView() {
        return lvTimeline;
    }

    public void refreshTimeline() {
        // TODO: This should figure out whether we are doing mytimeline or all timelines and
        // refresh accordingly.
        displayMyTimeline();
    }
}
