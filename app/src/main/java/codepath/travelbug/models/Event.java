package codepath.travelbug.models;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.io.File;
import java.util.Date;

/**
 * Represents the Event class
 * Is a unit of a user's creation and can be a video or a picture.
 * Please refer to database schema or wiki for more information
 */

@Parcel(analyze = Event.class)
@ParseClassName("Event")
public class Event extends ParseObject {
    private boolean isFilePath = false; // If the image is an actual file path and not a URI.

    public Event() {
        super();
    }

    /**
     * Creates an Event with the date set to "now".
     * @return
     */
    public static Event createNow() {
        Event event = new Event();
        event.setEventDate(new Date());
        event.isFilePath = false;
        return event;
    }

    public long getEventId() {
        return getLong("eventId");
    }

    public void addImage(ParseFile imageFile) {
        put("imageFile", imageFile);
    }

    public ParseFile getImageFile() {
        return getParseFile("imageFile");
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

    public void setEventDate(Date date) {
        put("eventDate", date);
    }

    public Date getEventDate() {
        return getDate("eventDate");
    }

    public void setImageAsFilePath() {
        isFilePath = true;
    }

    public Uri getContentUri() {
        return isFilePath ? Uri.fromFile(new File(getPath())) : Uri.parse(getPath());
    }

    public void setGeoPoint(ParseGeoPoint geoPoint) {
        put("geoPoint", geoPoint);
    }

    public ParseGeoPoint getGeoPoint() {
        return getParseGeoPoint("geoPoint");
    }
}
