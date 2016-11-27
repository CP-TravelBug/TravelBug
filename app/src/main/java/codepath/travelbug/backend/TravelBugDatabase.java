package codepath.travelbug.backend;


import com.raizlabs.android.dbflow.annotation.Database;


@Database(name = TravelBugDatabase.NAME, version = TravelBugDatabase.VERSION)
public class TravelBugDatabase {

    public static final String NAME = "TravelBugDb";
    public static final int VERSION = 1;
}
