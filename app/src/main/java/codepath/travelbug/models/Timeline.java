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
@Parcel
public class Timeline {

    private static final String PARSE_OBJECT_TIMELINE = "Timeline";
    private static final String PARSE_FIELD_TIMELINEID = "TimelineId";
    private static final String PARSE_FIELD_EVENTLIST = "EventList";
    private static final String PARSE_FIELD_USERID = "userId";
    private static final String PARSE_FIELD_START_DATE = "startDate";
    private static final String PARSE_FIELD_END_DATE = "endDate";
    private static final String PARSE_FIELD_SHARED_WITH = "sharedWith";
    private static final String PARSE_FIELD_TIMELINE_TITLE = "TimelineTitle";

    long timelineId;
    List<Event> eventList;
    User userId;
    Date startDate;
    Date endDate;
    List<Long> sharedWith;
    String timelineTitle;

    public Timeline() {
        timelineId = Utils.RANDOM.nextLong();
        eventList = new ArrayList<>();
        sharedWith = new ArrayList<>();
    }

    public long getTimelineId() {
        return timelineId;
    }

    public void setTimelineId(long timelineId) {
        this.timelineId = timelineId;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return A list of user IDs that this timeline is shared with. The user IDs correspond to the
     * ones returned from Facebook auth.
     */
    public List<Long> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(List<Long> sharedWith) {
        this.sharedWith = sharedWith;
    }

    public String getTimelineTitle() {
        return timelineTitle;
    }

    public void setTimelineTitle(String timelineTitle) {
        this.timelineTitle = timelineTitle;
    }

    public ParseObject asParseObject() {
        ParseObject parseObject = new ParseObject(PARSE_OBJECT_TIMELINE);
        if (timelineTitle != null && ! timelineTitle.isEmpty()) {
            parseObject.put(PARSE_FIELD_TIMELINE_TITLE, timelineTitle);
        }
        parseObject.put(PARSE_FIELD_TIMELINEID, timelineId);
        parseObject.addAll(PARSE_FIELD_EVENTLIST, eventList);
        parseObject.addAll(PARSE_FIELD_SHARED_WITH, sharedWith);
        if (userId != null) {
            parseObject.put(PARSE_FIELD_USERID, userId.getUserId());
        }
        if (startDate != null) {
            parseObject.put(PARSE_FIELD_START_DATE, startDate);
        }
        if (endDate != null) {
            parseObject.put(PARSE_FIELD_END_DATE, endDate);
        }
        return parseObject;
    }
}
