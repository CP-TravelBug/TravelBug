package codepath.travelbug.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import codepath.travelbug.R;
import codepath.travelbug.backend.Backend;

/**
 * @author Pragyan
 */

public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater mInflater;

    public CustomWindowAdapter(LayoutInflater inflater) {
        mInflater = inflater;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = mInflater.inflate(R.layout.custom_info_window, null);
        ImageView ivEventPic = (ImageView) v.findViewById(R.id.ivTravelEventPoints);
        // TODO set searched images here.
        // Get Geo Tagged pictures using Flicker API

        return v;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }


}
