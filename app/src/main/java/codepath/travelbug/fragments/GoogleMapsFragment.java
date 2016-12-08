package codepath.travelbug.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;

import codepath.travelbug.R;
import codepath.travelbug.adapter.CustomWindowAdapter;
import codepath.travelbug.models.Event;

import static android.location.LocationManager.GPS_PROVIDER;

public class GoogleMapsFragment extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String EVENTID = "eventID";
    private GoogleMap mMap;
    private GoogleApiClient mclientAPI;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private Marker mMarker;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((mclientAPI == null) || (!mclientAPI.isConnected())) {
            buildGoogleClientAPI();
            mclientAPI.connect();
        }

        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    protected synchronized void buildGoogleClientAPI() {
        mclientAPI = new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .build();

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(10000);
        //mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mclientAPI, mLocationRequest, this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerDragListener(this);

        if (getIntent().hasExtra("LATITUDE") && getIntent().hasExtra("LONGITUDE")) {
            latitude = getIntent().getDoubleExtra("LATITUDE", 0);
            longitude = getIntent().getDoubleExtra("LONGITUDE", 0);
        }

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        mLocation.setLatitude(marker.getPosition().latitude);
        mLocation.setLatitude(marker.getPosition().longitude);
        LatLng currLatLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        mMarker = mMap.addMarker(new MarkerOptions().position(currLatLng).title("Event Location")
                .icon(defaultMarker));
        mMarker.setDraggable(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 12));
        mMap.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
        if(mMarker != null) {
            dropPinEffect(mMarker);
        }

        Log.d("onMarkerDragEnd:Lat", Double.toString(latitude));
        Log.d("onMarkerDragEnd:Long", Double.toString(longitude));

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;

        if (mMarker != null) {
            mMarker.remove();
        }

        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();

        LatLng currLatLng = new LatLng(latitude, longitude);
        BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        mMarker = mMap.addMarker(new MarkerOptions().position(currLatLng).title("Event Location")
                .icon(defaultMarker));
        mMarker.setDraggable(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 12));
        mMap.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
        if(mMarker != null) {
            dropPinEffect(mMarker);
        }
        Log.d("onLocationChanged:Lat", Double.toString(latitude));
        Log.d("onLocationChanged:Long", Double.toString(longitude));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mclientAPI != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mclientAPI, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        Log.d("onBackPressed:Latitude", Double.toString(latitude));
        Log.d("onBackPressed:Longitude", Double.toString(longitude));

        data.putExtra("LATITUDE", latitude);
        data.putExtra("LONGITUDE", longitude);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }
}
