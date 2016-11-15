package codepath.travelbug.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;

import codepath.travelbug.R;
import codepath.travelbug.Utils;

import static codepath.travelbug.Utils.MAX_WIDTH;
import static codepath.travelbug.Utils.TAG;

public class CreateTimelineActivity extends AppCompatActivity {
    Uri pictureUri;
    ImageView picView;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timeline);
        pictureUri = getIntent().getExtras().getParcelable(Utils.PIC_URI_KEY);
        picView = (ImageView)findViewById(R.id.ivCameraImage);
        saveButton = (Button) findViewById(R.id.btnSaveTimeline);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateTimelineActivity.this, "Timeline created.", Toast.LENGTH_SHORT).show();
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
        String resizedFilePath = pictureUri.getPath() + "_resized";
        File resizedFile = new File(resizedFilePath);
        resizedFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(resizedFile);
        // Write the bytes of the bitmap to file
        fos.write(bytes.toByteArray());
        fos.close();
        imageView.setImageBitmap(scaledImage);
    }
}
