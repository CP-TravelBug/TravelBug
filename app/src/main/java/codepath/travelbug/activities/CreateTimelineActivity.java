package codepath.travelbug.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.Utils;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.fragments.AddToTimelineFragment;
import codepath.travelbug.fragments.GoogleMapsFragment;
import codepath.travelbug.fragments.NewTimelineFragment;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;

import static android.location.LocationManager.GPS_PROVIDER;
import static codepath.travelbug.Utils.MAX_WIDTH;
import static codepath.travelbug.Utils.TAG;

public class CreateTimelineActivity extends AppCompatActivity
        implements AddToTimelineFragment.AddedToTimelineListener,
        NewTimelineFragment.NewTimelineListener {
    Uri pictureUri;
    ImageView picView;
    ImageView location;
    Button saveButton;
    EditText pictureTitle;
    String resizedFilePath;
    Long idOfTimelineCreated;
    List<Long> myTimelineIds;
    FloatingActionButton fbAddToTimeline;
    FloatingActionButton fbCreateNewTimeline;
    int spinnerSelectedPosition;
    boolean isNewTimeLine;
    boolean isNothingSelected = true;
    String newTimelineName;
    private Location mLocation;

    private static final int ADD_TO_TIMELINE_REQUEST_CODE = 1001;
    private static final int CREATE_NEW_TIMELINE_REQUEST_CODE = 1002;
    private static final int GEO_LOCATION_ACTIVITY_REQUEST_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timeline);
        myTimelineIds = new LinkedList<>();
        polulateTimelineIdList();

        location = (ImageView) findViewById(R.id.ivLocationIcon);

        pictureUri = getIntent().getExtras().getParcelable(Utils.PIC_URI_KEY);
        picView = (ImageView)findViewById(R.id.ivCameraImage);
        pictureTitle = (EditText)findViewById(R.id.editText);

        saveButton = (Button) findViewById(R.id.btnSaveTimeline);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNothingSelected) {
                    Toast.makeText(CreateTimelineActivity.this, "Add event to a new or existing timeline", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateTimelineActivity.this, "New Event added.", Toast.LENGTH_SHORT).show();
                    if (resizedFilePath != null) {
                        persistTimeline(resizedFilePath);
                        Intent i = new Intent();
                        i.putExtra("idOfTimelineCreated", idOfTimelineCreated);
                        setResult(RESULT_OK, i);
                    }
                    finish();
                }
            }
        });
        try {
            readImageIntoView(pictureUri, picView);
        } catch (IOException e) {
            Log.d(TAG, "Error reading image taken.");
        }

        fbAddToTimeline = (FloatingActionButton) findViewById(R.id.fab_addToExistingTimeline);
        fbAddToTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddToExistingTimeline();
            }
        });

        fbCreateNewTimeline = (FloatingActionButton) findViewById(R.id.fab_createNewTimeline);
        fbCreateNewTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCreateNewTimeline();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GoogleMapsFragment.class);
                if(mLocation != null) {
                    i.putExtra("LATITUDE", mLocation.getLatitude());
                    i.putExtra("LONGITUDE", mLocation.getLongitude());
                }
                startActivityForResult(i, GEO_LOCATION_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void launchAddToExistingTimeline() {
        FragmentManager fm = getSupportFragmentManager();
        AddToTimelineFragment addToTimelineFragment =
                AddToTimelineFragment.newInstance(getTimelineLists());
        addToTimelineFragment.show(fm, "add_to_timeline_fragment");
    }

    private void launchCreateNewTimeline() {
        FragmentManager fm = getSupportFragmentManager();
        NewTimelineFragment newTimelineFragment =
                NewTimelineFragment.newInstance();
        newTimelineFragment.show(fm, "create_new_timeline_fragment");
    }

    private void polulateTimelineIdList() {
        Collection<Timeline> tmList = Backend.get().getMyTimelines();
        for (Timeline t : tmList) {
            myTimelineIds.add(t.getTimelineId());
        }
    }

    private ArrayList<String> getTimelineLists() {
        ArrayList<String> tmNames = new ArrayList<>();
        if (myTimelineIds.size() != 0) {
            for (Long id : myTimelineIds) {
                tmNames.add(Backend.get().getTimeline(id).getTimelineTitle());
            }
        } else {
            tmNames.add("No Timeline yet");
        }
        return tmNames;
    }

    // Takes the given image from the camera, resizes it and writes it to another file.
    private void readImageIntoView(Uri pictureUri, ImageView imageView) throws IOException {
        Bitmap takenImage = BitmapFactory.decodeFile(pictureUri.getPath());
        Bitmap scaledImage = Utils.scaleToFitWidth(takenImage, MAX_WIDTH);
        // Configure byte output stream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // Compress the image further
        scaledImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
        resizedFilePath = pictureUri.getPath() + "_resized";
        File resizedFile = new File(resizedFilePath);
        resizedFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(resizedFile);
        // Write the bytes of the bitmap to file
        fos.write(bytes.toByteArray());
        fos.close();
        imageView.setImageBitmap(scaledImage);
    }

    @Override
    public void onAddingTimeline(int position) {
        spinnerSelectedPosition = position;
        isNewTimeLine = false;
        isNothingSelected = false;
    }

    @Override
    public void onCancel() {
        isNothingSelected = true;
    }

    @Override
    public void onCreatingNewTimeline(String newTimelineName) {
        this.newTimelineName = newTimelineName;
        isNewTimeLine = true;
        isNothingSelected = false;
    }

    @Override
    public void onCancelNewTimeline() {
        isNothingSelected = true;
    }

    private void persistTimeline(String imagePath) {
        //boolean isNewTimeline = ((RadioButton) findViewById(R.id.rbCreateNewTimeline)).isChecked();
        if (isNewTimeLine) { // New Time Line
            createNewTimeline(imagePath);
        } else { // Add to existing timeline
            addToExistingTimeline(imagePath);
        }
    }

//    private void getTimelineTitleString() {
//        onRadioButtonClicked(findViewById(R.id.rbAddToTimeline));
//        onRadioButtonClicked(findViewById(R.id.rbCreateNewTimeline));
//    }

//    public void onRadioButtonClicked(View view) {
//        boolean checked = ((RadioButton)view).isChecked();
//        switch (view.getId()) {
//            case R.id.rbAddToTimeline:
//                timelineTitle = spTmLists.getSelectedItem().toString();
//                break;
//            case R.id.rbCreateNewTimeline:
//                timelineTitle = tvTimelineTitle.getText().toString();
//                break;
//        }
//    }

    private void addToExistingTimeline(String imagePath) {
        int position = spinnerSelectedPosition;
        Timeline timeline = Backend.get().getTimeline(myTimelineIds.get(position));
        if (timeline == null) {
            timeline = Backend.get().getSharedTimeline(myTimelineIds.get(position));
        }
        Event event = new Event();
        event.setPath(imagePath);
        event.setContent(pictureTitle.getText().toString());
        event.setGeoPoint(new ParseGeoPoint(mLocation.getLatitude(), mLocation.getLongitude()));
        timeline.getEventList().add(event);
        idOfTimelineCreated = myTimelineIds.get(position);
        Log.d("Existing Timeline",
                timeline.getTimelineTitle() + " " + Long.toString(myTimelineIds.get(position)));
    }

    private void createNewTimeline(String imagePath) {
        Timeline timeline = Timeline.createWithUniqueId();
        timeline.setUserId(Backend.get().getCurrentUser().getUserId());
        Event event = new Event();
        event.setPath(imagePath);
        event.setContent(pictureTitle.getText().toString());
        event.setGeoPoint(new ParseGeoPoint(mLocation.getLatitude(), mLocation.getLongitude()));
        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(event);
        timeline.addEvents(eventList);
        // Get the correct timeline title string
        //getTimelineTitleString();
        timeline.setTimelineTitle(newTimelineName);
        Backend.get().addTimeline(timeline);
        idOfTimelineCreated = timeline.getTimelineId();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == GEO_LOCATION_ACTIVITY_REQUEST_CODE) {
            if(mLocation == null) {
                mLocation = new Location(GPS_PROVIDER);
            }
            mLocation.setLongitude(data.getDoubleExtra("LATITUDE", 0));
            mLocation.setLongitude(data.getDoubleExtra("LONGITUDE", 0));
        }
    }

}
