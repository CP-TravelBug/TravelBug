package codepath.travelbug.backend;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;

import static codepath.travelbug.TravelBugApplication.TAG;

/**
 * Created by arunesh on 11/29/16.
 */

public class FakeDataGenerator {
    private static final String FAKE_SUBPATH = "/fakes/";
    private static final String ARUNESH_USERID = "10154691136329655";
    private static final String ORON_USERID = "10157640087935648";
    private static final String PRAGYAN_USERID = "128890540922344";
    private static final String[] FAKE_USERS = new String [] {
        "10157645629934648",
        "10134360987535610",
        "19164461947931640",
        "16134360987535619",
        "12138360387535614",
        "17135360287735692",
    };

    private static final String[] FAKE_USER_FIRST_NAMES = new String [] {
            "Kevin",
            "Mike",
            "Paula",
            "Correy",
            "Rich",
            "Smith",
    };

    private static final String[] FAKE_USER_LAST_NAMES = new String [] {
            "Ames",
            "Lancey",
            "Twin",
            "Bacon",
            "Harvey",
            "Jason",
    };

  /*
    private static final String[] imageList = {
            "dt_pic1.jpg",
            "dt_pic2.jpg",
            "dt_pic3.jpg",
            "dt_pic4.jpg",
            "dt_pic5.jpg",
            "dt_pic6.jpg",
            "dt_pic7.jpg",
            "dt_pic8.jpg",
    };
*/
    public static final int[] imageList = {
          R.raw.dt_pic1,
          R.raw.dt_pic2,
          R.raw.dt_pic3,
          R.raw.dt_pic4,
          R.raw.dt_pic5,
          R.raw.dt_pic6,
          R.raw.dt_pic7,
          R.raw.dt_pic8,
  };

        public static final int [] profileImageList = {
                R.raw.contact_one,
                R.raw.contact_two,
                R.raw.contact_three,
                 R.raw.contact_four,
                 R.raw.contact_five,
                 R.raw.contact_six,
                 R.raw.contact_seven,
                 R.raw.contact_eight,
                 R.raw.contact_nine,
                 R.raw.contact_ten,
        };

    public static int generateRandomIndex() {
        return (int)(Math.random() * ((9) + 1));
    }

    private static final String[] imageTitles = {
            "Breathtaking Ledge in Preikestolen, Norway",
            "The most unique Honeymoon destinations: Seychelles Honeymoon.",
            "Snorkeling, Bahamas",
            "Wild Orcas at sunset",
            "Battery Park at Manhattan",
            "Spot in an African Safari",
            "Hinchinbrook Island, Australia",
            "Kovachevitsa, Bulgaria",
    };
    private List<Event> fakeEventList = new ArrayList<>();
    private List<Timeline> fakeTimelines = new LinkedList<>();
    private User aruneshUser;
    private User oronUser;
    private User pragyanUser;

    /**
     * A fake event sub-class so that we can override some of the parameters.
     */
    @ParseClassName("FakeEvent")
    public static class FakeEvent extends Event {
        private Uri fakeImageUri;

        public FakeEvent() {
            super();
        }

        public void setFakeImageUri(Uri uri) {
            fakeImageUri = uri;
        }

        @Override
        public Uri getContentUri() {
            return fakeImageUri;
        }
    }

    FakeDataGenerator(Context context) {
        createEventList(context);
    }

    private void createEventList(Context context) {
        int index = 0;
        for (int img : imageList) {
            Event event = new Event();
            event.setImageHint(index + 10);
            event.setPath("android.resource://" + context.getPackageName() + "/" + img);
            event.setContent(imageTitles[index]);
            index ++;
            fakeEventList.add(event);
        }
    }

    public void fetchOrCreateRealUserObjects() throws com.parse.ParseException {
        aruneshUser = Backend.get().fetchUserFor(ARUNESH_USERID);
        oronUser = Backend.get().fetchUserFor(ORON_USERID);
        if (oronUser == null) {
            oronUser = new User();
            oronUser.setUserId(ORON_USERID);
            oronUser.save();
        }
        pragyanUser = Backend.get().fetchUserFor(PRAGYAN_USERID);
        if (pragyanUser == null) {
            pragyanUser = new User();
            pragyanUser.setUserId(PRAGYAN_USERID);
            pragyanUser.save();
        }
    }

    private void createFakeUsers() throws com.parse.ParseException {
        for (int i = 0; i < 6; i++) {
            User user = Backend.get().fetchUserFor(FAKE_USERS[i]);
            if (user == null) {
                user = new User();
                user.setUserId(FAKE_USERS[i]);
                user.setFirstName(FAKE_USER_FIRST_NAMES[i]);
                user.setLastName(FAKE_USER_LAST_NAMES[i]);
                user.setFullName(FAKE_USER_FIRST_NAMES[i] + " " + FAKE_USER_LAST_NAMES[i]);
                user.save();
            }
        }
    }

    public void createLocalTimelines(String userId) {
        boolean flipCoin = false;
        int index = 0;
        for(Event event : fakeEventList) {
            Timeline timeline = Timeline.createWithUniqueId();
            timeline.setUserId(userId);
            timeline.setTimelineTitle("Title:" + event.getContent());
            ArrayList<Event> eventList = new ArrayList<>();
            eventList.add(event);
            timeline.addEvents(eventList);
            fakeTimelines.add(timeline);
            if (flipCoin) {
                Backend.get().addTimeline(timeline);
            } else {
                Backend.get().addToSharedTimeline(timeline);
            }
            flipCoin = !flipCoin;
            index ++;
        }
    }

    public void createTimelines(String userId) {
        boolean flipCoin = false;
        int index = 0;
        for(Event event : fakeEventList) {
            Timeline timeline = Timeline.createWithUniqueId();
            String myUserId = getFakeUserIdAndShare(index % 3, timeline);
            timeline.setUserId(myUserId);
            timeline.setTimelineTitle("Title:" + event.getContent());
            ArrayList<Event> eventList = new ArrayList<>();
            eventList.add(event);
            timeline.addEvents(eventList);
            fakeTimelines.add(timeline);

            //if (flipCoin) {
              //  Backend.get().addTimeline(timeline);
            //} else {
              //  Backend.get().addToSharedTimeline(timeline);
            //}
            // flipCoin = !flipCoin;
            index ++;
        }
    }

    private void addFriends() throws com.parse.ParseException {
        aruneshUser.addToFriendsList(Arrays.asList(FAKE_USERS));
        oronUser.addToFriendsList(Arrays.asList(FAKE_USERS));

        pragyanUser.addToFriendsList(Arrays.asList(FAKE_USERS));

    }

    private String getFakeUserIdAndShare(int index, Timeline timeline) {
        switch (index) {
            case 0:
                aruneshUser.addSharedTimeline(timeline.getTimelineId());
                pragyanUser.addSharedTimeline(timeline.getTimelineId());
                timeline.shareWith(ARUNESH_USERID);
                timeline.shareWith(PRAGYAN_USERID);
                oronUser.addTimeline(timeline.getTimelineId());
                return ORON_USERID;
            case 1:
                oronUser.addSharedTimeline(timeline.getTimelineId());
                timeline.shareWith(ORON_USERID);
                pragyanUser.addSharedTimeline(timeline.getTimelineId());
                timeline.shareWith(PRAGYAN_USERID);
                aruneshUser.addTimeline(timeline.getTimelineId());
                return ARUNESH_USERID;
            case 2:
                oronUser.addSharedTimeline(timeline.getTimelineId());
                timeline.shareWith(ORON_USERID);
                aruneshUser.addSharedTimeline(timeline.getTimelineId());
                timeline.shareWith(ARUNESH_USERID);
                pragyanUser.addTimeline(timeline.getTimelineId());
                return PRAGYAN_USERID;
            default: return "";
        }
    }

    private void persistOrCreateData() throws com.parse.ParseException {
        persistUserData(aruneshUser);
        persistUserData(oronUser);
        persistUserData(pragyanUser);
        for (Timeline timeline : fakeTimelines) {
            persistTimeline(timeline);
        }
    }

    // Call this only once.
    public void generateFakeData() {
        try {
            fetchOrCreateRealUserObjects();
            createFakeUsers();
            createTimelines("blah");
            addFriends();
            persistOrCreateData();
        } catch (com.parse.ParseException e) {
            Log.e("ERROR", "Error can't generate fake data.");
        }
    }

    private void persistUserData(User user) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo(User.PARSE_FIELD_USERID, user.getUserId());
        User fetchedUser = null;
        try {
            fetchedUser = query.getFirst();
            Log.i(TAG, "Fetched user from server:" + fetchedUser.getFullName());

            // If we fetch the user, nothing to do here.
        } catch (com.parse.ParseException e) {
            Log.i(TAG, "Parse exception in saveOrCreateUser:" + e);
            saveUser(user);
            return;
        }
        Backend.mergeUserDataInto(user, fetchedUser);
        saveUser(fetchedUser);
    }

    private void saveUser(User user) {
        try {
            Log.i(TAG, "Saving/creating user: " + user.getFullName());
            user.save();
        } catch (com.parse.ParseException e) {
            Log.e(TAG, "Could not save user to the server:" + user.getFullName());
        }
    }

    private void persistTimeline(Timeline timeline) {
        ParseQuery<Timeline> timelineQuery = ParseQuery.getQuery(Timeline.class);
        timelineQuery.whereEqualTo(Timeline.PARSE_FIELD_TIMELINEID, timeline.getTimelineId());
        Timeline fetchedTimeline = null;
        try {
            fetchedTimeline = timelineQuery.getFirst();
        } catch (com.parse.ParseException e) {
            // Save timeline
            saveTimeline(timeline);
            return;
        }
        Backend.mergeTimelinesInto(timeline /* source */, fetchedTimeline /* destination */);
    }

    private void saveTimeline(Timeline timeline) {
        Log.i(TAG, "Saving timeline with id:" + timeline.getTimelineId());
        try {
            timeline.save();
        } catch (ParseException e) {
            Log.e(TAG, "Could not save timeline to server.");
        }
    }
}
