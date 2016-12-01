package codepath.travelbug.backend;


import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

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

    public static JSONObject parseObjectToJson(ParseObject parseObject) throws ParseException, JSONException {
        JSONObject jsonObject = new JSONObject();
        parseObject.fetchIfNeeded();
        Set<String> keys = parseObject.keySet();
        for (String key : keys) {
            Object objectValue = parseObject.get(key);
            if (objectValue instanceof ParseObject) {
                jsonObject.put(key, parseObjectToJson(parseObject.getParseObject(key)));
                // keep in mind about "pointer" to it self, will gain stackoverlow
            } else if (objectValue instanceof ParseRelation) {
                // handle relation
            } else if (objectValue instanceof List) {
                JSONArray array = new JSONArray();
                List<?> myList = (List<?>) objectValue;
                for (Object value : myList) {
                    if (value instanceof ParseObject) {
                        array.put(parseObjectToJson((ParseObject)value));
                    } else {
                        array.put(value.toString());
                    }
                }
                jsonObject.put(key, array);
            } else {
                jsonObject.put(key, objectValue.toString());
            }
        }
        return jsonObject;
    }
}
