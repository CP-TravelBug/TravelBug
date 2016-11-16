package codepath.travelbug.backend;

import java.util.LinkedList;

import codepath.travelbug.models.Timeline;

public class Backend {

    private static final Backend INSTANCE = new Backend();

    private LinkedList<Timeline> timelines;

    private Backend() {
        timelines = new LinkedList<>();
    }

    public void addTimeline(Timeline timeline) {
        timelines.add(timeline);
    }

    public static Backend get() {
        return INSTANCE;
    }
}
