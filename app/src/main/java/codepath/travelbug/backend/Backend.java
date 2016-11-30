package codepath.travelbug.backend;

import android.content.Context;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;

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

    private ExecutorService executorService;

    // This is a lock for all operations on the existing user. So that we can serialize and use
    // local data only when server operations are complete.
    private Object userOperationsLock = new Object();
    private Object timelineOperationsLock = new Object();

    private HashSet<DataChangedCallback> callbackSet;

    public interface DataChangedCallback {
        void onDataChanged();
    }

    private Backend() {
        myTimelines = new HashMap<Long, Timeline>();
        myTimelinesList = new LinkedList<>();
        sharedTimelinesList = new LinkedList<>();
        sharedTimelines = new HashMap<Long, Timeline>();
        executorService = Executors.newCachedThreadPool();
        callbackSet = new HashSet<>();
    }

    public void createFakeTimelines(Context context, String userId) {
        FakeDataGenerator fakeDataGenerator = new FakeDataGenerator(context);
        fakeDataGenerator.createTimelines(userId);
    }

    public void generateFakeData(Context context) {
        FakeDataGenerator fakeDataGenerator = new FakeDataGenerator(context);
        fakeDataGenerator.generateFakeData();
    }

    public synchronized void addTimeline(final Timeline timeline) {
        myTimelines.put(timeline.getTimelineId(), timeline);
        myTimelinesList.addFirst(timeline);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                persistAddTimeline(timeline);
            }
        });
    }

    public synchronized void clearMyTimelines() {
        myTimelinesList.clear();
        myTimelines.clear();
    }

    public synchronized void clearSharedTimelines() {
        sharedTimelines.clear();
        sharedTimelinesList.clear();
    }

    public synchronized void addToSharedTimeline(Timeline timeline) {
        sharedTimelines.put(timeline.getTimelineId(), timeline);
        sharedTimelinesList.addFirst(timeline);
        // TODO: Probably not needed to persist this since the share action has
        // to originate from the owner of this timeline.
    }

    public static Backend get() {
        return INSTANCE;
    }

    public synchronized void setCurrentUser(final User user) {
        currentUser = user;
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                fetchOrCreateUser(user);
            }
        });
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
        return myTimelines.get(timelineId);
    }
    public synchronized Timeline getSharedTimeline(long timelineId) {
        return sharedTimelines.get(timelineId);
    }

    public synchronized void shareTimelineWithUser(long timelineId, final String userId) {
        final Timeline timeline = myTimelines.get(Long.valueOf(timelineId));
        if (timeline == null) {
            Log.e(TAG, "Share timeline failed, timeline with id " + timelineId +
            " does not exist.");
            return;
        }
        timeline.shareWith(userId);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                persistShareTimelineWithUser(timeline, userId);
            }
        });
    }

    public synchronized void setFriendsList(List<User> friendsList) {
        this.friendsList = friendsList;
    }

    public synchronized List<User> getFriendsList() {
        return friendsList;
    }

    public void registerCallback(DataChangedCallback callback) {
        callbackSet.add(callback);
    }

    public void unregisterCallback(DataChangedCallback callback) {
        callbackSet.remove(callback);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    /// Parse operations:

    private void fetchOrCreateUser(User user) {
        synchronized (userOperationsLock) {
            ParseQuery<User> query = ParseQuery.getQuery(User.class);
            query.whereEqualTo(User.PARSE_FIELD_USERID, user.getUserId());
            try {
                User fetchedUser = query.getFirst();
                Log.i(TAG, "Fetched user from server:" + user.getFullName());
                if (fetchedUser != null) {
                    currentUser = fetchedUser;
                    updateFetchedUserTimelines(fetchedUser);
                }
            } catch (ParseException e) {
                createUser(user);
            }
        }
    }
    private void createUser(User user) {
        try {
            Log.i(TAG, "Creating new user: " + user.getFullName());
            user.save();
        } catch (ParseException e) {
            Log.e(TAG, "Could not save user to the server.");
        }
    }

    /**
     * Populate timeline data for a user fetched from the server. Call this on a background thread.
     * @param user
     */
    private void updateFetchedUserTimelines(User user) {
        List<Timeline> result = fetchTimelinesFor(user.getTimelines());
        if (result != null) {
            for (Timeline timeline : result) {
                addTimeline(timeline);
            }
        }
        result = fetchTimelinesFor(user.getSharedTimelines());
        if (result != null) {
            for (Timeline timeline : result) {
                addToSharedTimeline(timeline);
            }
        }
        notifyDatasetChanged();
    }

    private List<Timeline> fetchTimelinesFor(List<Long> idList) {
        if (idList == null || idList.size() == 0) return null;
        ParseQuery<Timeline> parseQuery = ParseQuery.getQuery(Timeline.class);
        parseQuery.whereContainedIn(Timeline.PARSE_FIELD_TIMELINEID, idList);
        try {
            return parseQuery.find();
        } catch (ParseException e) {
            Log.e(TAG, "Fetch timelines for a list failed.");
            return null;
        }
    }

    private void persistAddTimeline(Timeline timeline) {
        synchronized (timelineOperationsLock) {
            try {
                timeline.save();
            } catch (ParseException e) {
                Log.e(TAG, "Timeline save failed.");
            }
        }
        synchronized (userOperationsLock) {
            currentUser.addTimeline(timeline.getTimelineId());
            try {
                currentUser.save();
            } catch (ParseException e) {
                Log.e(TAG, "User update failed.");
            }
        }
    }

    private void persistShareTimelineWithUser(Timeline timeline, String userId) {
        synchronized (timelineOperationsLock) {
            try {
                timeline.save();
            } catch (ParseException e) {
                Log.e(TAG, "Timeline save failed.");
            }
        }
        synchronized (userOperationsLock) {
            User user = fetchUserFor(userId);
            if (user != null) {
                user.addSharedTimeline(timeline.getTimelineId());
            }
            try {
                user.save();
            } catch (ParseException e) {
                Log.e(TAG, "User update failed.");
            }
        }
    }

    public User fetchUserFor(String userId) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo(User.PARSE_FIELD_USERID, userId);
        try {
            User fetchedUser = query.getFirst();
            Log.i(TAG, "Fetched user from server:" + fetchedUser.getFullName());
            return fetchedUser;
        } catch (ParseException e) {
            return null;
        }
    }

    public void updateTimeline(final Timeline timeline) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                synchronized (timelineOperationsLock) {
                    try {
                        timeline.save();
                    } catch (ParseException e) {
                        Log.e(TAG, "Timeline save failed.");
                    }
                }
            }
        });
    }

    private void notifyDatasetChanged() {
        for (DataChangedCallback callback : callbackSet) {
            callback.onDataChanged();
        }
    }
 }
