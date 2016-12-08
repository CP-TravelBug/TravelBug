package codepath.travelbug.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import codepath.travelbug.FacebookClient;
import codepath.travelbug.R;
import codepath.travelbug.models.User;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.parceler.Parcels;

import static com.facebook.AccessToken.getCurrentAccessToken;

public class LoginActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(isAlreadyLoggedIn()) {
            startUserViewingOptionsActivity();
        }
        layout = (RelativeLayout)findViewById(R.id.activity_main);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(FacebookClient.FACEBOOK_PERMISSIONS);
        registerCallBack();
    }

    /**
     * Method that performs a callback on FB Login
     */
    private void registerCallBack() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult.getAccessToken() != null) {
                    Log.d("DEBUG", loginResult.toString());
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    layout.startAnimation(anim);
                    startUserViewingOptionsActivity();
                }
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void startUserViewingOptionsActivity() {
        Intent intent = new Intent(LoginActivity.this, ScrollingTimelineActivity.class);
        AccessToken accessToken = getCurrentAccessToken();
        Log.i("USERID", "USERID:" + accessToken.getUserId());
        // Toast.makeText(this, "USERID:" + accessToken.getUserId(), Toast.LENGTH_LONG).show();
        // Backend.get().createFakeTimelines(getApplicationContext(), accessToken.getUserId());
        // Backend.get().generateFakeData(getApplicationContext());
        User user = new User();
        intent.putExtra("user", Parcels.wrap(user));
        startActivity(intent);
    }

    /**
     * Checks if the user is already logged in
     * @return true if the access token exists for the user, false otherwise
     */
    private boolean isAlreadyLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
