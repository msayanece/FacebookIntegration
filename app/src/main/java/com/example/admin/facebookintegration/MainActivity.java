package com.example.admin.facebookintegration;

import android.content.Intent;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

// tutorial: https://www.sitepoint.com/integrating-the-facebook-graph-api-in-android/
//graph api explorer: https://developers.facebook.com/tools/explorer?method=GET&path=me%3Ffields%3Dposts&version=v2.9
// go to jre bin folder start cmd enter:
//keytool -exportcert -alias androiddebugkey -keystore C:\Users\Admin\.android\debug.keystore | C:\Users\Admin\Desktop\s\bin\openssl.exe sha1 -binary | C:\Users\Admin\Desktop\s\bin\openssl.exe base64
public class MainActivity extends AppCompatActivity {

    private String email;
    private String name;
    private String gender;
    private String userId;
    private URL profilePicture;
    private String birthDay;
    private CallbackManager mcallbackManager;
    private LoginButton loginButton;
    private ImageView markerImage;
    private ImageView profileImageView;

    /*
    * setting callback for login results...
    * called in onActivityResult
    * called after result come back from facebook api
    * if user press back button, onCancel() called
    * if other error occurred, onError() called
    * if user accepts the permission, onSuccess() called
    */
    private FacebookCallback<LoginResult> mcallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
             /*
            * request profile information and get response as a JSONObject
            */
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());
                            // object has all the asked result in it
                            putFacebookProfileInformation(object);
                        }
                    });
            /*
            * this is required for setting which data app wants to get
            */
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, email, gender, birthday, first_name, last_name");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize FacebookSdk as early as possible
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        //create gallbackmanager object here
        mcallbackManager = CallbackManager.Factory.create();

        //initialize all the UI objects
        profileImageView = (ImageView) findViewById(R.id.imageView);
        loginButton = (LoginButton) findViewById(R.id.login_button);        //facebook login button
        loginButton.setHeight(100);
        /* set the permission, user will see what are the data app will use
         * these permission is a must if app wants anything except public info
         * otherwise callback will only return id, name and gender (if available)
         */
        loginButton.setReadPermissions("email", "user_birthday","user_posts");
        //set register callback for login button here
        loginButton.registerCallback(mcallbackManager, mcallBack);
    }

    /* putFacebookProfileInformation to local fields
     * @param JSONObject returned from graphRequest
     */
    private void putFacebookProfileInformation(JSONObject object) {
        try {
            profileImageView.setVisibility(View.VISIBLE);
            email = object.getString("email");
            birthDay = object.getString("birthday");
            name = object.getString("name");
            gender = object.getString("gender");
            userId = object.getString("id");
            profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=250&height=250");
            Picasso.with(MainActivity.this)
                    .load(profilePicture.toString()) /// if load from JSON .load(responseJSON.getString("image"))
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .into(profileImageView);
            sendDataToNextActivity(email, birthDay, name, gender,userId,profilePicture.toString());
            Log.d("sayan", "name: "+name+" and gender: "+gender+" and birthday: "+birthDay+" and email: "+email);
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void sendDataToNextActivity(String email, String birthDay, String name, String gender, String userId, String image) {
        Intent intent = new Intent(this, ShareDataToFB.class);
        intent.putExtra("email", email);
        intent.putExtra("birthDay", birthDay);
        intent.putExtra("name", name);
        intent.putExtra("gender", gender);
        intent.putExtra("userId", userId);
        intent.putExtra("image", image);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*
     * this method called after result comes from facebook login page
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
         * call mcallbackManager's onActivityResult method with the result data
         */
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * check if already logged in
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (AccessToken.getCurrentAccessToken() == null);           //means not logged in
            else {
            /*
             * means user already logged in
             * request profile information and get response as a JSONObject
             */
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());
                            // object has all the asked result in it
                            putFacebookProfileInformation(object);
                        }
                    });
            /*
            * this is required for setting which data app wants to get
            */
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, email, gender, birthday, first_name, last_name");
            request.setParameters(parameters);
            request.executeAsync();

            //set a 1 sec delay for loading data from facebook
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
