package codepath.travelbug.models;

import android.location.Geocoder;
import android.net.Uri;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.io.File;
import java.util.List;

/**
 * Represents the Event class
 * Is a unit of a user's creation and can be a video or a picture.
 * Please refer to database schema or wiki for more information
 */

@Parcel
public class Event {


    long eventId;
    String path; // Path to content or video
    String content;  // Name of the video or picture (aka Title).
    List<Timeline> timelineList;
    @Transient
    Geocoder location;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Timeline> getTimelineList() {
        return timelineList;
    }

    public void setTimelineList(List<Timeline> timelineList) {
        this.timelineList = timelineList;
    }

    public Geocoder getLocation() {
        return location;
    }

    public void setLocation(Geocoder location) {
        this.location = location;
    }

    public Uri getContentUri() {
        return Uri.fromFile(new File(path));
    }
}
