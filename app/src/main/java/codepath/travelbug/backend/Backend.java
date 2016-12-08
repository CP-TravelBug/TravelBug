package codepath.travelbug.backend;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.io.File;
import java.sql.Time;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;

import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static codepath.travelbug.TravelBugApplication.TAG;

public class Backend {

    private static final Backend INSTANCE = new Backend();
    private static final String LOCAL_CACHE = "TravelBugCache";

    // Timelines owned by the current user.
    private HashMap<Long, Timeline> myTimelines;
    private LinkedList<Timeline> myTimelinesList; // We stores this list in rev chrono order.

    // Timelines shared with me.
    private HashMap<Long, Timeline> sharedTimelines;
    private LinkedList<Timeline> sharedTimelinesList; // We stores this list in rev chrono order.
    private User currentUser;
    private List<User> friendsList;

    private HashMap<Long, User> userCache; // Cache of all other user objects.

    private ExecutorService executorService;

    // This is a lock for all operations on the existing user. So that we can serialize and use
    // local data only when server operations are complete.
    private Object userOperationsLock = new Object();
    private Object timelineOperationsLock = new Object();

    private HashSet<DataChangedCallback> callbackSet;
    private Context context;
    private boolean cacheEnabled = false;

    public interface DataChangedCallback {
        void onDataChanged();
    }

    private Backend() {
        myTimelines = new HashMap<Long, Timeline>();
        myTimelinesList = new LinkedList<>();
        sharedTimelinesList = new LinkedList<>();
        userCache = new HashMap<>();
        sharedTimelines = new HashMap<Long, Timeline>();
        executorService = Executors.newCachedThreadPool();
        callbackSet = new HashSet<>();
        cacheEnabled = false;
        // enableCache();
    }

    public void setApplicationContext(Context context) {
        this.context = context;
    }

    public void enableCache() {
        cacheEnabled = true;
    }

    public void createFakeTimelines(Context context, String userId) {
        FakeDataGenerator fakeDataGenerator = new FakeDataGenerator(context);
        fakeDataGenerator.createLocalTimelines(userId);
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
                // persistAddTimeline(timeline);
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

    public static abstract class ResultRunnable<T> implements Runnable {
        T result;
        void setResult(T result) {
            this.result = result;
        }

        public T getResult() {
            return result;
        }
    }

    public void fetchUser(final String userId, final Handler uiHandler, final ResultRunnable<User> resultRunnable) {
        User user = null;
        if (userId == currentUser.getUserId()) {
            user = currentUser;
            resultRunnable.setResult(user);
            uiHandler.post(resultRunnable);
        } else if (userCache.get(userId) != null) {
            user = userCache.get(userId);
            resultRunnable.setResult(user);
            uiHandler.post(resultRunnable);
        } else {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    User user2 = getUser(userId);
                    resultRunnable.setResult(user2);
                    uiHandler.post(resultRunnable);
                }
            });
            return;
        }
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
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo(User.PARSE_FIELD_USERID, user.getUserId());
        if (cacheEnabled) {
            query.fromLocalDatastore();
        }
        try {
            User fetchedUser = query.getFirst();
            Log.i(TAG, "Fetched user from server:" + fetchedUser.getFullName());
            if (fetchedUser != null) {
                fetchedUser.fbPictureUrl = currentUser.fbPictureUrl;
                fetchedUser.localPicturePath = currentUser.localPicturePath;
                currentUser = fetchedUser;
                try {
                    if (!cacheEnabled) currentUser.pin();
                } catch (RuntimeException e) {
                    Log.e(TAG, e.toString());
                }
                updateFetchedUserTimelines(fetchedUser);
            }
        } catch (ParseException e) {
            createUser(user);
        }
    }

    private User getUser(String userId) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo(User.PARSE_FIELD_USERID, userId);
        if (cacheEnabled) {
            query.fromLocalDatastore();
        }
        try {
            User fetchedUser = query.getFirst();
            Log.i(TAG, "Fetched user from server:" + fetchedUser.getFullName());
            if (fetchedUser != null) {
                if (!cacheEnabled) fetchedUser.pin();
                return fetchedUser;
            }
        } catch (ParseException e) {

        }
        return null;
    }

    private void createUser(User user) {
        try {
            Log.i(TAG, "Creating new user: " + user.getFullName());
            user.save();
            user.pin();
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
            Log.i(TAG, "Number of timelines received:" + result.size());
            for (Timeline timeline : result) {
                Log.i(TAG, "Adding to my timeline.");
                fetchEventsInTimeline(timeline);
                addTimeline(timeline);
            }
        }
        result = fetchTimelinesFor(user.getSharedTimelines());
        if (result != null) {
            Log.e(TAG, "Number of shared timelines received:" + result.size());
            for (Timeline timeline : result) {
                fetchEventsInTimeline(timeline);
                addToSharedTimeline(timeline);
                Log.i(TAG, "Adding to shared timeline.");
            }
        } else {
            Log.e(TAG, "Null result.");
        }
        notifyDatasetChanged();
    }

    private List<Timeline> fetchTimelinesFor(List<Long> idList) {
        if (idList == null || idList.size() == 0) {
            Log.e(TAG, "Null input timeline list.");
            return null;
        }
        ParseQuery<Timeline> parseQuery = ParseQuery.getQuery(Timeline.class);
        parseQuery.whereContainedIn(Timeline.PARSE_FIELD_TIMELINEID, idList);
        List<Timeline> resultList = null;
        if (cacheEnabled) {
            parseQuery.fromLocalDatastore();
        }
        try {
            resultList = parseQuery.find();
            if (resultList != null ) {
                Log.e(TAG, "Size of resultList: " + resultList.size());
                if (!cacheEnabled) ParseObject.pinAll(resultList);
            } else {
                Log.e(TAG, "Null resultList.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Fetch timelines for a list failed:" + e);
        }
        return resultList;
    }

    private void fetchEventsInTimeline(Timeline timeline) {
        List<Event> eventList = timeline.getEventList();
        if (eventList != null) {
            try {
                if (!fetchAllEventsThroughLocalDatastore(eventList)) {
                    ParseObject.fetchAll(eventList);
                }
                fixEventImagePath(eventList);
            } catch (ParseException e) {
                Log.e(TAG, "Fetch event failed.");
            }
        } else {
            Log.i(TAG, "Null event list received.");
        }
        fixTimelineImagePath(timeline);
    }

    /**
     *
     * @param eventList
     * @return True if local fetch suceeeded, false otherwise.
     */
    private boolean fetchAllEventsThroughLocalDatastore(List<Event> eventList) {
        if (cacheEnabled) {
            for (Event event : eventList) {
                try {
                    event.fetchFromLocalDatastore();
                } catch (ParseException e) {
                    return false;
                }
            }
            Log.i(TAG, "Local datastore worked !");
            return true;
        }
        return false;
    }

    private void fixEventImagePath(List<Event> eventList) {
        for (Event event : eventList) {
            int index = event.getImageHint() - 10;
            if (index >= 0) {
                int img = FakeDataGenerator.imageList[index];
                event.setPath("android.resource://" + context.getPackageName() + "/" + img);
            }
        }
    }

    private void fixTimelineImagePath(Timeline timeline) {
        int index = timeline.getCoverImageHint() - 10;
        if (index >= 0) {
            int img = FakeDataGenerator.imageList[index];
            timeline.setCoverImagePath("android.resource://" + context.getPackageName() + "/" + img);
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
      //  synchronized (userOperationsLock) {
            User user = fetchUserFor(userId);
            if (user != null) {
                user.addSharedTimeline(timeline.getTimelineId());
            }
            try {
                user.save();
            } catch (ParseException e) {
                Log.e(TAG, "User update failed.");
            }
        //}
    }

    public User fetchUserFor(String userId) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo(User.PARSE_FIELD_USERID, userId);
        try {
            User fetchedUser = query.getFirst();
            Log.i(TAG, "Fetched user from server, version 2:" + fetchedUser.getFullName());
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
        Log.i(TAG, "DATA set changed.");
        try {
            Log.i(TAG, toStringMyTimelines());
            Log.i(TAG, toStringUser());
            Log.i(TAG, toStringSharedTimelines());
        } catch (Exception e) {
            Log.e(TAG, "Exception printing data: " + e);
        }
        for (DataChangedCallback callback : callbackSet) {
            callback.onDataChanged();
        }
    }

    public static void mergeUserDataInto(User source, User dest) {
        // Friends list, timelines, shared timelines.
        if (source.getFriendList() != null) {
            dest.addToFriendsList(source.getFriendList());
        }
        if (source.getSharedTimelines() != null) {
            dest.addToSharedTimelines(source.getSharedTimelines());
        }
        if (source.getTimelines() != null) {
            dest.addToTimelines(source.getTimelines());
        }
    }

    public static void mergeTimelinesInto(Timeline source, Timeline dest) {
        if (source.getSharedWith() != null) {
            dest.addShareWith(source.getSharedWith());
        }
        if (source.getEventList() != null) {
            dest.addEvents(source.getEventList());
        }
    }

    public String toStringUser() throws Exception {
        if (currentUser != null) {
            return ParseUtil.parseObjectToJson(currentUser).toString();
        } else {
            return "Null user.";
        }
    }

    public String toStringMyTimelines() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (Timeline timeline : myTimelinesList) {
            builder.append("\n" + ParseUtil.parseObjectToJson(timeline).toString());
        }
        return builder.toString();
    }

    public String toStringSharedTimelines() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (Timeline timeline : sharedTimelinesList) {
            builder.append("\n" + ParseUtil.parseObjectToJson(timeline).toString());
        }
        return builder.toString();
    }

    public static ParseFile uploadImage(String localFilePath, ProgressCallback progressCallback, SaveCallback saveCallback) {
        ParseFile parseFile = new ParseFile(new File(localFilePath));
        parseFile.saveInBackground(saveCallback, progressCallback);
        return parseFile;
    }

    public static ParseFile uploadImageSync(File file) {
        ParseFile parseFile = new ParseFile(file);
        try {
            parseFile.save();
        } catch (ParseException e) {
            Log.e(TAG, "Error uploading image file.");
        }
        return parseFile;
    }
 }
