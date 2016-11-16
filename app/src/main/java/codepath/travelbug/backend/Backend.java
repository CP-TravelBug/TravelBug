package codepath.travelbug.backend;

import java.util.LinkedList;

import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;

public class Backend {

    private static final Backend INSTANCE = new Backend();

    private LinkedList<Timeline> timelines;
    private User currentUser;

    private Backend() {
        timelines = new LinkedList<>();
    }

    public void addTimeline(Timeline timeline) {
        timelines.add(timeline);
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
 }
