package codepath.travelbug.backend;


import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import static codepath.travelbug.TravelBugApplication.TAG;

public class ParseUtil {
    public static final String APP_ID = "theTravelBug";
    public static final String PARSE_URL = "http://travelbug-backend.herokuapp.com/parse";


    public static void testLogin() {
        ParseUser.logInInBackground("Jingo Bingo", "travelBug", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.d(TAG, "Login succeeded.");
                } else {
                    Log.d(TAG, "Login Failed." + e);
                }
            }
        });
    }
}
