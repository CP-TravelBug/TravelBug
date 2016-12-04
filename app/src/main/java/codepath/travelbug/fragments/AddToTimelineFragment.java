package codepath.travelbug.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import codepath.travelbug.R;

/**
 * DialogFragment to display the spinnerlist to choose
 * from existing timelines
 */
public class AddToTimelineFragment extends DialogFragment {

    private Spinner spTmLists;
    private ArrayAdapter<String> tmListsArray;
    private static final String TAG = AddToTimelineFragment.class.getSimpleName();
    private static final String TIMELINENAMES = "timelinenames";
    private static final String TITLE = "ADD TO AN EXISTING TIMELINE";
    public AddToTimelineFragment() {
        // Required empty public constructor
    }

    public interface AddedToTimelineListener {
        void onAddingTimeline(int position);
        void onCancel();
    }
    public static AddToTimelineFragment newInstance(ArrayList<String> timelineNames) {
        AddToTimelineFragment frag = new AddToTimelineFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(TIMELINENAMES, timelineNames);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_to_timeline, container, false);
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_to_timeline, null);
        alertDialogBuilder.setView(view);
        spTmLists = (Spinner) view.findViewById(R.id.spTimelines);
        tmListsArray = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item);
        spTmLists.requestFocus();
        tmListsArray.addAll(getArguments().getStringArrayList(TIMELINENAMES));
        tmListsArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTmLists.setAdapter(tmListsArray);
        alertDialogBuilder.setTitle(TITLE).setPositiveButton("ADD", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddedToTimelineListener listener = (AddedToTimelineListener) getActivity();
                    listener.onAddingTimeline(spTmLists.getSelectedItemPosition());
                    getDialog().dismiss();
                }
            });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                AddedToTimelineListener listener = (AddedToTimelineListener) getActivity();
                listener.onCancel();
                getDialog().dismiss();
            }
        });

        return alertDialogBuilder.create();
    }

}
