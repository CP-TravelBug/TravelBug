package codepath.travelbug.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Date;

import codepath.travelbug.R;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.User;

public class EventViewActivity extends AppCompatActivity {

    TextView tvEventText;
    ImageView ivEventImage;
    boolean hasGeoPoint = false;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        tvEventText = (TextView) findViewById(R.id.eventLabel);
        ivEventImage = (ImageView) findViewById(R.id.ivEventImage);

        String eventTitle = getIntent().getStringExtra("eventTitle");
        Uri eventUri = (Uri) getIntent().getParcelableExtra("eventUri");
        Date date = (Date) getIntent().getSerializableExtra("eventDate");
        if (getIntent().getBooleanExtra("hasGeoPoint", false)) {
            hasGeoPoint = true;
            lat = getIntent().getDoubleExtra("lat", 0);
            lng = getIntent().getDoubleExtra("lng", 0);
        }

        tvEventText.setText(eventTitle);
        Picasso.with(this).load(eventUri).centerCrop().fit().into(ivEventImage);
    }
}
