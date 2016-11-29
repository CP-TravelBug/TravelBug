package codepath.travelbug.backend;

import android.content.Context;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;

import static codepath.travelbug.TravelBugApplication.TAG;

public class Backend {

    private static final Backend INSTANCE = new Backend();

    // Timelines owned by the current user.
    private HashMap<Long, Timeline> myTimelines;
    private LinkedList<Timeline> myTimelinesList; // We stores this list in rev chrono order.

    // Timelines shared with me.
    private HashMap<Long, Timeline> sharedTimelines;
    private LinkedList<Timeline> sharedTimelinesList; // We stores this list in rev chrono order.
    private User currentUser;
    private List<User> friendsList;

    private Backend() {
        myTimelines = new HashMap<Long, Timeline>();
        myTimelinesList = new LinkedList<>();
        sharedTimelinesList = new LinkedList<>();
        sharedTimelines = new HashMap<Long, Timeline>();
    }

    public void createFakeTimelines(Context context) {
        FakeDataGenerator fakeDataGenerator = new FakeDataGenerator(context);
        fakeDataGenerator.createTimelines();
    }

    public synchronized void addTimeline(Timeline timeline) {
        myTimelines.put(timeline.getTimelineId(), timeline);
        myTimelinesList.addFirst(timeline);
    }

    public synchronized void addToSharedTimeline(Timeline timeline) {
        sharedTimelines.put(timeline.getTimelineId(), timeline);
        sharedTimelinesList.addFirst(timeline);
    }

    public static Backend get() {
        return INSTANCE;
    }

    public synchronized void setCurrentUser(User user) {
        currentUser = user;
    }

    public synchronized User getCurrentUser() {
        return currentUser;
    }

    /**
     * Returns the current user's myTimelines (that the user owns).
     * @return
     */
    public synchronized Collection<Timeline> getMyTimelines() {
        return myTimelinesList;
    }

    /**
     * Returns the set of timelines shared with the current user.
     */
    public synchronized Collection<Timeline> getSharedTimelines() {
        return sharedTimelinesList;
    }

    public synchronized Timeline getTimeline(long timelineId) {
        return myTimelines.get(Long.valueOf(timelineId));
    }

    public synchronized void shareTimelineWithUser(long timelineId, String userId) {
        Timeline timeline = myTimelines.get(Long.valueOf(timelineId));
        if (timeline == null) {
            Log.e(TAG, "Share timeline failed, timeline with id " + timelineId +
            " does not exist.");
            return;
        }

        timeline.shareWith(userId);
        // TODO: Persist to Parse.
    }

    public synchronized void setFriendsList(List<User> friendsList) {
        this.friendsList = friendsList;
    }

    public synchronized List<User> getFriendsList() {
        return friendsList;
    }
 }
