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
    LinkedList<Event> eventLinkedList;
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


        eventLinkedList = new LinkedList<>();
        adapter = new TimelineDisplayAdapter(getContext(), eventLinkedList);
        lvTimeline.setAdapter(adapter);
        if (mPage == 1) {
            displayAllTimelines();
        } else {
            displayMyTimeline();
        }
        return view;
    }

    private void displayAllTimelines() {
        Toast.makeText(getContext(), "Displaying all timelines", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    private void displayMyTimeline() {
        // Get Event objects and displays as timelines
        // Filler code until we read values from DB or server
        Collection<Timeline> timelines = Backend.get().getTimelines();
        if (timelines.size() == 0) {
            Toast.makeText(getActivity(), "No timelines created yet", Toast.LENGTH_LONG).show();
        }
        eventLinkedList.clear();
        for (Timeline tm : timelines) {
            // ToDo add to specific timelines feature yet to be implemented
            // For now each event is added to a new timeline
            Event e = tm.getEventList().get(0);
            eventLinkedList.add(e);
        }
        //adapter.clear();
//        adapter.addAll(eventLinkedList);
        adapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
