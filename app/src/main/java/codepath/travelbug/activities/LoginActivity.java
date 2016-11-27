package codepath.travelbug.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import codepath.travelbug.R;
import codepath.travelbug.models.User;
import io.fabric.sdk.android.Fabric;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.parceler.Parcels;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        if(isAlreadyLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, UserViewingOptionsActivity.class);
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            User user = new User();
            user.setAccessToken(accessToken);
            intent.putExtra("user", Parcels.wrap(user));
            startActivity(intent);
        }
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "read_custom_friendlists"));
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
                    Intent intent = new Intent(LoginActivity.this, UserViewingOptionsActivity.class);
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    User user = new User();
                    user.setAccessToken(accessToken);
                    intent.putExtra("user", Parcels.wrap(user));
                    startActivity(intent);
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

    /**
     * Checks if the user is already logged in
     * @return true if the access token exists for the user, false otherwise
     */
    private boolean isAlreadyLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken == null) {
            return false;
        }
        else {
            return true;
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
