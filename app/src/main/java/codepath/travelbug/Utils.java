package codepath.travelbug;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import codepath.travelbug.backend.Backend;
import codepath.travelbug.models.User;

import static codepath.travelbug.R.id.tvLocation;

/**
 * Created by arunesh on 11/14/16.
 */

public class Utils {
    public static final Random RANDOM = new Random(System.currentTimeMillis());
    public static final String PHOTO_FILE_PREFIX = "TravelBug";
    public static final String APP_TAG = "TravelBug";
    public static final String TAG = "TravelBug";
    public static final String PIC_URI_KEY = "picUriKey";
    public static final int MAX_WIDTH = 360 * 3; // xxxhdpi.

    private static final double TEST_LAT = 37.354108;
    private static final double TEST_LNG = -121.955236;

    public static  String generateUniqueFileName() {
        String filename = "";
        long millis = System.currentTimeMillis();
        String rndchars = String.valueOf(RANDOM.nextLong());
        filename = PHOTO_FILE_PREFIX + rndchars +  "_" + millis + ".jpg";
        Log.d(TAG, "Random filename = " + filename);
        return filename;
    }

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public static Uri getPhotoFileUri(Context context, String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    public static Bitmap scaleToFitWidth(Bitmap b, int width)
    {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
    }


    // scale and keep aspect ratio
    public static Bitmap scaleToFitHeight(Bitmap b, int height)
    {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
    }


    // scale and keep aspect ratio
    public static Bitmap scaleToFill(Bitmap b, int width, int height)
    {
        float factorH = height / (float) b.getWidth();
        float factorW = width / (float) b.getWidth();
        float factorToUse = (factorH > factorW) ? factorW : factorH;
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse),
                (int) (b.getHeight() * factorToUse), true);
    }


    // scale and don't keep aspect ratio
    public static Bitmap strechToFill(Bitmap b, int width, int height)
    {
        float factorH = height / (float) b.getHeight();
        float factorW = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorW),
                (int) (b.getHeight() * factorH), true);
    }


    public static void testGeocoder(Context context) {
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> address = geoCoder.getFromLocation(TEST_LAT,
                    TEST_LNG, 1);
            if (address != null) {
                String city = address.get(0).getLocality();
                String name = address.get(0).getFeatureName();
                String country = address.get(0).getCountryName();
                String location = name + ", " + city + ", " + country;
            }
        } catch (IOException e) {
            Log.e("IOEXCEPTION", e.toString());
        }
    }
}
