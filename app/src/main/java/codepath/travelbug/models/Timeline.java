package codepath.travelbug.models;

import java.util.List;

/**
 * Represents a user's timeline
 * User can share a timeline with other users
 * A timeline is viewed on the Timeline screen
 */

public class Timeline {

    private long timelineId;
    private List<Event> eventList;
    private User userId;

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
    
}
