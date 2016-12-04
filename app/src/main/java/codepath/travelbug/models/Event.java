package codepath.travelbug.models;

import android.location.Geocoder;
import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.io.File;
import java.util.List;

/**
 * Represents the Event class
 * Is a unit of a user's creation and can be a video or a picture.
 * Please refer to database schema or wiki for more information
 */

@Parcel(analyze = Event.class)
@ParseClassName("Event")
public class Event extends ParseObject {
    long eventId;

    public Event() {
        super();
    }

    public long getEventId() {
        return getLong("eventId");
    }

    public void setEventId(long eventId) {
        put("eventId", eventId);
    }

    // We do not persist path to the Parse backend.
    public String getPath() {
        return getString("path");
    }

    public void setPath(String path) {
        put("path", path);
    }

    public void setImageHint(int hint) {
        put ("imageHint", hint);
    }

    public int getImageHint() {
        return getInt("imageHint");
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }

    public Uri getContentUri() {
        return Uri.parse(getPath());
        //return Uri.fromFile(new File(getPath()));
    }

    public void setGeoPoint(ParseGeoPoint geoPoint) {
        put("geoPoint", geoPoint);
    }

    public ParseGeoPoint getGeoPoint() {
        return getParseGeoPoint("geoPoint");
    }
}
