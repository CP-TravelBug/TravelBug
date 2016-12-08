package codepath.travelbug.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.io.IOException;

import codepath.travelbug.R;
import codepath.travelbug.Utils;

import static codepath.travelbug.Utils.MAX_WIDTH;
import static codepath.travelbug.Utils.TAG;

public class AddEventActivity extends AppCompatActivity {

    Uri pictureUri;
    ImageView picView;
    Button saveButton;
    EditText eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        pictureUri = getIntent().getExtras().getParcelable(Utils.PIC_URI_KEY);
        picView = (ImageView) findViewById(R.id.ivCameraImageLarge);
        try {
            readImageIntoView(pictureUri, picView);
        } catch (IOException e) {
            Log.d(TAG, "Error reading image taken.");
        }
        eventName = (EditText) findViewById(R.id.etEventName);
        saveButton = (Button) findViewById(R.id.btnNext);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("event_name", eventName.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
        // Takes the given image from the camera, resizes it and writes it to another file.
    private void readImageIntoView(Uri pictureUri, ImageView imageView) throws IOException {
        Bitmap takenImage = BitmapFactory.decodeFile(pictureUri.getPath());
        Bitmap scaledImage = Utils.scaleToFitWidth(takenImage, MAX_WIDTH);
        // Configure byte output stream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // Compress the image further
        scaledImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        imageView.setImageBitmap(scaledImage);
    }
}
