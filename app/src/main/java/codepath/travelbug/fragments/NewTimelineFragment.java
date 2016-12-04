package codepath.travelbug.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import codepath.travelbug.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewTimelineFragment extends DialogFragment {

    private EditText etTimelineTitle;
    private static final String TAG = NewTimelineFragment.class.getSimpleName();
    private static final String TITLE = "CREATE A NEW TIMELINE";

    public NewTimelineFragment() {
        // Required empty public constructor
    }

    public interface NewTimelineListener {
        public void onCreatingNewTimeline(String timelineName);
        public void onCancelNewTimeline();
    }

    public static NewTimelineFragment newInstance() {
        NewTimelineFragment frag = new NewTimelineFragment();
        return frag;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_new_timeline, container);
//
//        return view;
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_new_timeline, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle(TITLE);
        etTimelineTitle = (EditText) view.findViewById(R.id.etTimelineName);

        alertDialogBuilder.setPositiveButton("CREATE", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewTimelineFragment.NewTimelineListener listener =
                                (NewTimelineFragment.NewTimelineListener) getActivity();
                        listener.onCreatingNewTimeline(etTimelineTitle.getText().toString());
                        getDialog().dismiss();
                    }
                });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                NewTimelineFragment.NewTimelineListener listener =
                        (NewTimelineFragment.NewTimelineListener) getActivity();
                listener.onCancelNewTimeline();
                getDialog().dismiss();
            }
        });

        return alertDialogBuilder.create();
    }
}