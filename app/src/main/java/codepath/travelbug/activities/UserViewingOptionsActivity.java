package codepath.travelbug.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.Date;
import java.util.Random;

import codepath.travelbug.FacebookClient;
import codepath.travelbug.R;
import codepath.travelbug.Utils;
import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.User;

import static codepath.travelbug.Utils.PHOTO_FILE_PREFIX;
import static codepath.travelbug.Utils.PIC_URI_KEY;
import static codepath.travelbug.Utils.TAG;

public class UserViewingOptionsActivity extends AppCompatActivity {
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int CREATE_TIMELINE_WITH_PIC_REQUEST_CODE = 1001;

    private Random random;
    private Uri lastCameraRequestUri;

    RoundedImageView ivProfileImage;
    TextView tvName;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random = new Random();
        setContentView(R.layout.activity_timeline);
        ivProfileImage = (RoundedImageView)findViewById(R.id.ivProfileImage);
        tvName = (TextView)findViewById(R.id.tvName);
        // Performs a GET request to get the user's info
        if(AccessToken.getCurrentAccessToken() != null) {
            String userId = AccessToken.getCurrentAccessToken().getUserId();
            GraphRequest request = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    userId,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            User user = User.fromJSONObject(response.getJSONObject());
                            String helloTextWithFirstName = "Hello " + user.getFirstName();
                            tvName.setText(helloTextWithFirstName);
                            Backend.get().setCurrentUser(user);
                            // Picasso.with(getApplicationContext()).load(user.getEntity().getMediaUrl()).into(ivProfileImage);
                        }
                    }
            );
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,email,first_name,last_name,picture");
            request.setParameters(parameters);
            request.executeAsync();
            // This fetches the high res image.
            FacebookClient.fetchUserPictureAtHighRes(new FacebookClient.ResultCallback<String>() {
                @Override
                public void onResult(String result) {
                    Picasso.with(UserViewingOptionsActivity.this).load(result).into(ivProfileImage);
                }
            });
        }

        setUpHomeViewCards();

    }

    private void setUpHomeViewCards() {
        View tileView = findViewById(R.id.timeline_tile);
        TextView tvTitle = (TextView) tileView.findViewById(R.id.tvTitle);
        tvTitle.setText("My Timelines");
        ImageView ivDisplayIcon = (ImageView) tileView.findViewById(R.id.gridImage);
        ivDisplayIcon.setImageResource(R.drawable.travelicon);
        // Set up onclick handlers
        tileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ScrollingTimelineActivity.class);
                startActivity(i);
            }
        });

        tileView = findViewById(R.id.friends_timeline_tile);
        tvTitle = (TextView) tileView.findViewById(R.id.tvTitle);
        tvTitle.setText("Friends Timelines");
        ivDisplayIcon = (ImageView) tileView.findViewById(R.id.gridImage);
        ivDisplayIcon.setImageResource(R.drawable.friendstimeline);

        tileView = findViewById(R.id.search_timelines);
        tvTitle = (TextView) tileView.findViewById(R.id.tvTitle);
        tvTitle.setText("Search Timelines");
        ivDisplayIcon = (ImageView) tileView.findViewById(R.id.gridImage);
        ivDisplayIcon.setImageResource(R.drawable.searchtimelines);

        tileView = findViewById(R.id.add_friends);
        tvTitle = (TextView) tileView.findViewById(R.id.tvTitle);
        tvTitle.setText("Add Friends");
        ivDisplayIcon = (ImageView) tileView.findViewById(R.id.gridImage);
        ivDisplayIcon.setImageResource(R.drawable.addfriends);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_createTimeline);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCreateTimelineWithCamera();
            }
        });

    }

    // Launches the camera first to take a picture which is then passed to the
    //  for creating a new timeline.
    private void launchCreateTimelineWithCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        lastCameraRequestUri = Utils.getPhotoFileUri(this, Utils.generateUniqueFileName(random));
        Log.d(TAG, "Image file URI:" + lastCameraRequestUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, lastCameraRequestUri); // set the image file name

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = lastCameraRequestUri;
                Intent i = new Intent(this, CreateTimelineActivity.class);
                i.putExtra(PIC_URI_KEY, takenPhotoUri);
                startActivityForResult(i, CREATE_TIMELINE_WITH_PIC_REQUEST_CODE);
                // by this point we have the camera photo on disk
                // Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                // ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                // ivPreview.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
