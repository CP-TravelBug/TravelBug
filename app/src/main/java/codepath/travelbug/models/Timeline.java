package codepath.travelbug.models;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import codepath.travelbug.Utils;

import static android.R.attr.path;

/**
 * Represents a user's timeline
 * User can share a timeline with other users
 * A timeline is viewed on the Timeline screen
 */
@Parcel(analyze = Timeline.class)
@ParseClassName("Timeline")
public class Timeline extends ParseObject {
    public static final String PARSE_FIELD_TIMELINEID = "TimelineId";
    public static final String PARSE_FIELD_EVENTLIST = "EventList";
    public static final String PARSE_FIELD_USERID = "userId";
    public static final String PARSE_FIELD_START_DATE = "startDate";
    public static final String PARSE_FIELD_END_DATE = "endDate";
    public static final String PARSE_FIELD_SHARED_WITH = "sharedWith";
    public static final String PARSE_FIELD_TIMELINE_TITLE = "TimelineTitle";

    private boolean isCoverImagePath = false;
    private String coverImagePath;

    public Timeline() {
        super();
    }

    public Timeline(long timelineId) {
        super();
        setTimelineId(timelineId);
    }

    public static Timeline createWithUniqueId() {
        long id = Utils.RANDOM.nextLong();
        Timeline timeline = new Timeline(id);
        timeline.setStartDate(new Date());
        return timeline;
    }

    public long getTimelineId() {
        return getLong(PARSE_FIELD_TIMELINEID);
    }

    public void setCoverImageHint(int hint) {
        put ("coverImageHint", hint);
    }

    public int getCoverImageHint() {
        return getInt("coverImageHint");
    }

    public void setCoverImageAsFilePath() {
        isCoverImagePath = true;
    }

    public void setCoverImagePath(String path) {
        coverImagePath = path;
    }
    public Uri getCoverImageUri() {
        if (coverImagePath == null) return null;
        return isCoverImagePath ? Uri.fromFile(new File(coverImagePath)) : Uri.parse(coverImagePath);
    }

    public void setTimelineId(long timelineId) {
        put(PARSE_FIELD_TIMELINEID, timelineId);
    }

    public void setInfo(String info) {
        put("info", info);
    }
    public String getInfo() {
        return getString("info");
    }

    public List<Event> getEventList() {
        return getList(PARSE_FIELD_EVENTLIST);
    }

    public void addEvents(List<Event> eventList) {
        addAllUnique(PARSE_FIELD_EVENTLIST, eventList);
    }

    public String getUserId() {
        return getString(PARSE_FIELD_USERID);
    }

    public void setUserId(String userId) {
        put(PARSE_FIELD_USERID, userId);
    }

    public Date getStartDate() {
        return getDate(PARSE_FIELD_START_DATE);
    }

    public void setStartDate(Date startDate) {
        put(PARSE_FIELD_START_DATE, startDate);
    }

    public Date getEndDate() {
        return getDate(PARSE_FIELD_END_DATE);
    }

    public void setEndDate(Date endDate) {
        put(PARSE_FIELD_END_DATE, endDate);
    }

    public void shareWith(String userId) {
        addUnique(PARSE_FIELD_SHARED_WITH, userId);
    }

    public void addShareWith(List<String> userList) {
        addAllUnique(PARSE_FIELD_SHARED_WITH, userList);
    }

    /**
     *
     * @return A list of user IDs that this timeline is shared with. The user IDs correspond to the
     * ones returned from Facebook auth.
     */
    public List<String> getSharedWith() {
        return getList(PARSE_FIELD_SHARED_WITH);
    }

    public void shareWith(List<String> sharedWith) {
        addAll(PARSE_FIELD_SHARED_WITH, sharedWith);
    }

    public String getTimelineTitle() {
        return getString(PARSE_FIELD_TIMELINE_TITLE);
    }

    public void setTimelineTitle(String timelineTitle) {
        put(PARSE_FIELD_TIMELINE_TITLE, timelineTitle);
    }
}
