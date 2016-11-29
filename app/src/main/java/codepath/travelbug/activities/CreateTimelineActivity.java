package codepath.travelbug.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;

import codepath.travelbug.R;
import codepath.travelbug.Utils;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;

import static codepath.travelbug.Utils.MAX_WIDTH;
import static codepath.travelbug.Utils.TAG;

public class CreateTimelineActivity extends AppCompatActivity {
    Uri pictureUri;
    ImageView picView;
    Button saveButton;
    EditText pictureTitle;
    String resizedFilePath;
    Long idOfTimelineCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timeline);
        pictureUri = getIntent().getExtras().getParcelable(Utils.PIC_URI_KEY);
        picView = (ImageView)findViewById(R.id.ivCameraImage);
        pictureTitle = (EditText)findViewById(R.id.editText);
        saveButton = (Button) findViewById(R.id.btnSaveTimeline);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateTimelineActivity.this, "Timeline created.", Toast.LENGTH_SHORT).show();
                if (resizedFilePath != null) {
                    persistTimeline(resizedFilePath);
                    Intent i = new Intent();
                    i.putExtra("idOfTimelineCreated", idOfTimelineCreated);
                    setResult(RESULT_OK, i);
                }
                finish();
            }
        });
        try {
            readImageIntoView(pictureUri, picView);
        } catch (IOException e) {
            Log.d(TAG, "Error reading image taken.");
        }
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

    private void persistTimeline(String imagePath) {
        Timeline timeline = new Timeline();
        timeline.setUserId(Backend.get().getCurrentUser());
        Event event = new Event();
        event.setPath(imagePath);
        event.setContent(pictureTitle.getText().toString());
        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(event);
        timeline.setEventList(eventList);
        Backend.get().addTimeline(timeline);
        idOfTimelineCreated = timeline.getTimelineId();
    }
}
