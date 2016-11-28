package codepath.travelbug.backend;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class Backend {

    private static final Backend INSTANCE = new Backend();

    private HashMap<Long, Timeline> timelines;
    private User currentUser;

    private Backend() {
        timelines = new HashMap<Long, Timeline>();
    }

    public void addTimeline(Timeline timeline) {
        timelines.put(timeline.getTimelineId(), timeline);
    }

    public static Backend get() {
        return INSTANCE;
    }


    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Collection<Timeline> getTimelines() {
        return timelines.values();
    }

    public Timeline getTimeline(long timelineId) {
        return timelines.get(Long.valueOf(timelineId));
    }
 }
