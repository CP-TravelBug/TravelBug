package codepath.travelbug;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.interceptors.ParseLogInterceptor;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import codepath.travelbug.backend.Backend;
import codepath.travelbug.backend.FakeDataGenerator;
import codepath.travelbug.backend.ParseUtil;
import codepath.travelbug.models.Event;
import codepath.travelbug.models.Timeline;
import codepath.travelbug.models.User;
import io.fabric.sdk.android.Fabric;

public class TravelBugApplication extends Application {
    public static String TAG = "TravelBug";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(FakeDataGenerator.FakeEvent.class);
        ParseObject.registerSubclass(Timeline.class);
        // Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(ParseUtil.APP_ID) // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server(ParseUtil.PARSE_URL).build());

       // Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Test parse, remove later.
        // ParseUtil.testLogin();
        FlowManager.init(new FlowConfig.Builder(this).build());
        Backend.get().setApplicationContext(getApplicationContext());

    }
}
