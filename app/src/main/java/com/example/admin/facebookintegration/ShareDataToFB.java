package com.example.admin.facebookintegration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShareDataToFB extends AppCompatActivity {

    private String email, birthDay, name, gender, userId, image;
    private TextView nameTv, genderTv, birthDayTv;
    private ImageView imageView;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_data_to_fb);
        initializeViews();
        initializeDataFromIntent();
        setDataIntoViews();
    }

    //initialize views
    private void initializeViews(){
        nameTv = (TextView) findViewById(R.id.nameAndSurname);
        genderTv = (TextView) findViewById(R.id.gender);
        birthDayTv = (TextView) findViewById(R.id.dob);
        imageView = (ImageView) findViewById(R.id.profileImage);
    }

    //get data from intent
    private void initializeDataFromIntent(){
        email = getIntent().getStringExtra("email");
        birthDay = getIntent().getStringExtra("birthDay");
        name = getIntent().getStringExtra("name");
        gender = getIntent().getStringExtra("gender");
        userId = getIntent().getStringExtra("userId");
        image = getIntent().getStringExtra("image");
    }

    //set data from intent into layout views
    private void setDataIntoViews(){
        nameTv.setText(name);
        genderTv.setText(gender);
        birthDayTv.setText(birthDay);
        Picasso.with(this)
                .load(image)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(imageView);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.share:
                share();
                break;
            case R.id.getPosts:
                getPosts();
                break;
            case R.id.logout:
                logout();
                break;
        }
    }

    /*
     *  The method above shares a link content. You can use the similar methods like SharePhotoContent,
     *  ShareVideoContent, ShareFeedContent, ShareMediaContent etc.
     *  The content should always be shown inside the ShareDialog.
     */
    private void share(){
        shareDialog = new ShareDialog(this);
        List<String> taggedUserIds= new ArrayList<String>();
        taggedUserIds.add("{USER_ID}");
        taggedUserIds.add("{USER_ID}");
        taggedUserIds.add("{USER_ID}");

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://www.sitepoint.com"))
                .setContentTitle("This is a content title")
                .setContentDescription("This is a description")
                .setShareHashtag(new ShareHashtag.Builder().setHashtag("#sitepoint").build())
                .setPeopleIds(taggedUserIds)
                .setPlaceId("{PLACE_ID}")
                .build();

        shareDialog.show(content);
    }

    //In order to get the user timeline posts, we need to make a GraphRequest:
    // for understanding: https://developers.facebook.com/tools/explorer/
    private void getPosts(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/posts", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.e("sayan",response.toString());
                    }
                }
        ).executeAsync();
    }

    //log out the user and send to main activity
    private void logout(){
        LoginManager.getInstance().logOut();
        Intent logoutIntent = new Intent(this, MainActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logoutIntent);
        finish();
    }
}
