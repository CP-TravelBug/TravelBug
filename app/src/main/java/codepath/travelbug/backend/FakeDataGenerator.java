package codepath.travelbug.backend;

import android.content.Context;
import android.net.Uri;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import codepath.travelbug.R;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;

/**
 * Created by arunesh on 11/29/16.
 */

public class FakeDataGenerator {
    private static final String FAKE_SUBPATH = "/fakes/";
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
    private static final int[] imageList = {
          R.raw.dt_pic1,
          R.raw.dt_pic2,
          R.raw.dt_pic3,
          R.raw.dt_pic4,
          R.raw.dt_pic5,
          R.raw.dt_pic6,
          R.raw.dt_pic7,
          R.raw.dt_pic8,
  };
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

    /**
     * A fake event sub-class so that we can override some of the parameters.
     */
    public static class FakeEvent extends Event {
        private Uri fakeImageUri;

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
            FakeEvent event = new FakeEvent();
            event.setFakeImageUri(Uri.parse("android.resource://" + context.getPackageName() + "/" + img));
            event.setContent(imageTitles[index]);
            index ++;
            fakeEventList.add(event);
        }
    }

    public void createTimelines() {
        for(Event event : fakeEventList) {
            Timeline timeline = new Timeline();
            timeline.setUserId(Backend.get().getCurrentUser());
            timeline.setTimelineTitle(event.getContent());
            ArrayList<Event> eventList = new ArrayList<>();
            eventList.add(event);
            timeline.setEventList(eventList);
            Backend.get().addTimeline(timeline);
        }
    }
}
