package com.example.admin.facebookintegration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

// go to jre bin folder start cmd enter:
//keytool -exportcert -alias androiddebugkey -keystore C:\Users\Admin\.android\debug.keystore | C:\Users\Admin\Desktop\s\bin\openssl.exe sha1 -binary | C:\Users\Admin\Desktop\s\bin\openssl.exe base64
public class MainActivity extends AppCompatActivity {

    private String email;
    private FacebookCallback<LoginResult> mcallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                putFacebookProfileInformation(profile);
            }
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {

                            Log.v("LoginActivity", response.toString());
                            // Application code
                            try {
                                email = object.getString("email");
//                                sendEmail();
//                                String birthday = object.getString("birthday");
//                                Toast.makeText(getApplicationContext(), "email: " + email + " and birthday: " + birthday, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
            Toast.makeText(getApplicationContext(), "Log in Successful!", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "Log in Cancel!", Toast.LENGTH_LONG).show();

        }
        @Override
        public void onError(FacebookException error) {
            Toast.makeText(getApplicationContext(), "Something went wrong, Log in Failed!", Toast.LENGTH_LONG).show();
        }
    };
    private CallbackManager mcallbackManager;
    private LoginButton loginButton;
    private ImageView markerImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        markerImage = (ImageView) findViewById(R.id.image);
        mcallbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(mcallbackManager, mcallBack);
    }

    //putFacebookProfileInformation
    private void putFacebookProfileInformation(Profile profile) {
        String type = "signUp";
        String accountType = "facebook";
        String firstName = profile.getFirstName();
        String lastName = profile.getLastName();
        Uri image = profile.getProfilePictureUri(250,250);
        markerImage.setVisibility(View.VISIBLE);
        Picasso.with(MainActivity.this)
                .load(image) /// if load from JSON .load(responseJSON.getString("image"))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(markerImage);
        Toast.makeText(this, "type="+type+", acc="+accountType+"name: "+profile.getFirstName()+" "+profile.getLastName(), Toast.LENGTH_LONG).show();
        Log.e("fb: ", type+accountType+firstName+lastName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
