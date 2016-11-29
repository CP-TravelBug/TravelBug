package codepath.travelbug.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import codepath.travelbug.Utils;

/**
 * Represents a user's timeline
 * User can share a timeline with other users
 * A timeline is viewed on the Timeline screen
 */
@Parcel(analyze = Timeline.class)
@ParseClassName("Timeline")
public class Timeline extends ParseObject {
    private static final String PARSE_FIELD_TIMELINEID = "TimelineId";
    private static final String PARSE_FIELD_EVENTLIST = "EventList";
    private static final String PARSE_FIELD_USERID = "userId";
    private static final String PARSE_FIELD_START_DATE = "startDate";
    private static final String PARSE_FIELD_END_DATE = "endDate";
    private static final String PARSE_FIELD_SHARED_WITH = "sharedWith";
    private static final String PARSE_FIELD_TIMELINE_TITLE = "TimelineTitle";


    public Timeline() {
        super();
        setTimelineId(Utils.RANDOM.nextLong());
    }

    public long getTimelineId() {
        return getLong(PARSE_FIELD_TIMELINEID);
    }

    public void setTimelineId(long timelineId) {
        put(PARSE_FIELD_TIMELINEID, timelineId);
    }

    public List<Event> getEventList() {
        return getList(PARSE_FIELD_EVENTLIST);
    }

    public void addEvents(List<Event> eventList) {
        addAll(PARSE_FIELD_EVENTLIST, eventList);
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
        add(PARSE_FIELD_SHARED_WITH, userId);
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
