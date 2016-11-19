package codepath.travelbug.models;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

/**
 * Represents a user's timeline
 * User can share a timeline with other users
 * A timeline is viewed on the Timeline screen
 */
@Parcel
public class Timeline {

    long timelineId;
    List<Event> eventList;
    User userId;
    Date startDate;
    Date endDate;
    List<User> sharedWith;
    String timelineTitle;

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

    public List<User> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(List<User> sharedWith) {
        this.sharedWith = sharedWith;
    }

    public String getTimelineTitle() {
        return timelineTitle;
    }

    public void setTimelineTitle(String timelineTitle) {
        this.timelineTitle = timelineTitle;
    }
}
