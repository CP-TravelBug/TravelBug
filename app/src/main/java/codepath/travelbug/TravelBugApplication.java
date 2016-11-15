package codepath.travelbug;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.interceptors.ParseLogInterceptor;

import codepath.travelbug.backend.ParseUtil;
import codepath.travelbug.models.User;

public class TravelBugApplication extends Application {
    public static String TAG = "TravelBug";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(ParseUtil.APP_ID) // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server(ParseUtil.PARSE_URL).build());

        // Test parse, remove later.
        ParseUtil.testLogin();
    }
}
