package codepath.travelbug.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.widget.RecyclerView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.activities.ShareActivity;
import codepath.travelbug.activities.decorators.SimpleDividerItemDecoration;
import codepath.travelbug.adapter.TimelineDisplayAdapter;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.Timeline;

import static com.facebook.FacebookSdk.getApplicationContext;
import static codepath.travelbug.TravelBugApplication.TAG;


public class ViewPagerFragment extends Fragment {

    private static final String PAGE_NUM = "PAGE_NUM";
    //private OnFragmentInteractionListener mListener;
    private int mPage;
    final String path = Environment.DIRECTORY_DCIM;
    RecyclerView lvTimeline;
    List<Timeline> timelineList;
    TimelineDisplayAdapter adapter;
    SwipeRefreshLayout swipeContainer;

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
        Log.d("PAGE_NUM", Integer.toString(mPage));
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
        lvTimeline = (RecyclerView) view.findViewById(R.id.lvTimeline);
        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Displays Home Timeline
                if(mPage == 1) {
                    refreshHomeTimeline();
                }
                // Displays My Timeline
                else if(mPage == 2) {
                    refreshTimeline();
                }
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));

        timelineList = new LinkedList<>();
        adapter = new TimelineDisplayAdapter(getContext(), timelineList);
        lvTimeline.setAdapter(adapter);
        lvTimeline.setLayoutManager(new LinearLayoutManager(getContext()));
        lvTimeline.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        return view;
    }

    private void displayAllTimelines() {
        Log.e(TAG, "Refreshing TIMELINES 2");
        //Toast.makeText(getContext(), "Displaying all timelines", Toast.LENGTH_SHORT).show();
        Collection<Timeline> timelines = Backend.get().getSharedTimelines();
        if (timelines.size() == 0) {
            // ToDo Display a message to create a timeline
            Log.e(TAG, "No shared timelines yet ?");
        }
        Log.e(TAG, "Size of timelines to add:" + timelines.size());
        timelineList.clear();
        timelineList.addAll(timelines);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
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
        Log.e(TAG, "Refreshing TIMELINES 1s");
        // Get Event objects and displays as timelines
        // Filler code until we read values from DB or server
        Collection<Timeline> timelines = Backend.get().getMyTimelines();
        if (timelines.size() == 0) {
            //Toast.makeText(getActivity(), "No timelines created yet", Toast.LENGTH_LONG).show();
            // Todo Display a message to create timeline
            Log.e(TAG, "No timelines yet ?");
        }

        Log.e(TAG, "Size of timelines to add:" + timelines.size());
        timelineList.clear();

        timelineList.addAll(timelines);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void refreshHomeTimeline() {
        Log.e(TAG, "Got refresh request for home timeline");
        // Lets refresh all the timelines at this point.
        displayAllTimelines();
    }

    public void refreshTimeline() {
        // TODO: This should figure out whether we are doing mytimeline or all timelines and
        // refresh accordingly.
        displayMyTimeline();
    }
}
