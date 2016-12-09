package codepath.travelbug.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.Date;

import codepath.travelbug.R;
import codepath.travelbug.Utils;
import codepath.travelbug.adapter.CustomWindowAdapter;

import static codepath.travelbug.TravelBugApplication.TAG;

public class EventViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView tvEventText;
    ImageView ivEventImage;
    TextView tvDate;
    boolean hasGeoPoint = false;
    double lat, lng;
    private GoogleMap mMap;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        tvEventText = (TextView) findViewById(R.id.eventLabel);
        ivEventImage = (ImageView) findViewById(R.id.ivEventImage);
        tvDate = (TextView) findViewById(R.id.eventDate);

        String eventTitle = "\"" + getIntent().getStringExtra("eventTitle") +"\"";
        Uri eventUri = (Uri) getIntent().getParcelableExtra("eventUri");
        Date date = (Date) getIntent().getSerializableExtra("eventDate");

        if (date != null) {
            tvDate.setText(Utils.formatDate(date));
        }

        if (getIntent().getBooleanExtra("hasGeoPoint", false)) {
            hasGeoPoint = true;
            lat = getIntent().getDoubleExtra("lat", 0);
            lng = getIntent().getDoubleExtra("lng", 0);
        }

        tvEventText.setText(eventTitle);
        Picasso.with(this).load(eventUri).centerCrop().fit().into(ivEventImage);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (hasGeoPoint) {
            mapFragment.getMapAsync(this);
        } else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mapFragment);
            fragmentTransaction.commit();
        }
    }

    private void addMarkerToMap() {
        if (!hasGeoPoint) return;
        LatLng currLatLng = new LatLng(lat, lng);
        BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        mMarker = mMap.addMarker(new MarkerOptions().position(currLatLng)
                .icon(defaultMarker));
        mMarker.setDraggable(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 12));
        mMap.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
        Log.d(TAG, Double.toString(lat));
        Log.d(TAG, Double.toString(lng));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (hasGeoPoint) {
            mMap = googleMap;
            addMarkerToMap();
        }
    }
}
