package com.example.admin.facebookintegration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShowFacebookPost extends AppCompatActivity {

    private List<FacebookPostModel> posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_facebook_post);

        ListView postListView = (ListView) findViewById(R.id.postlist);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        posts = bundle.getParcelableArrayList("posts");
        for (int i = 0; i<posts.size(); i++) {
            setUrlToFacebookPostModel(i, posts.get(i).getId());
        }
        postListView.setAdapter(new PostListViewAdapter());
    }

    public class PostListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return posts.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.list_view_element, parent, false);
            TextView listviewTime = (TextView)row.findViewById(R.id.time_tv);
            TextView listviewTitle = (TextView)row.findViewById(R.id.title_tv);

            listviewTime.setText(posts.get(position).getCreated_time());
            listviewTitle.setText(posts.get(position).getStory());

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(ShowFacebookPost.this, ShowPostDetails.class);
                    try {
                        webIntent.putExtra("url", posts.get(position).getUrl());
                        startActivity(webIntent);
                    }catch (NullPointerException e){
                        Toast.makeText(ShowFacebookPost.this, "No details available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return row;
        }

    }

    private void setUrlToFacebookPostModel(final int position, String id){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/"+id+"?fields=permalink_url", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject responseObject = response.getJSONObject();
                        try {
                            String url = responseObject.getString("permalink_url");
                            posts.get(position).setUrl(url);
                            Log.e("sayan===>","responseObject: "+url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("sayan======>",response.toString());
                    }
                }
        ).executeAsync();
    }

    private void getPostUrl(final int position, String id) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/"+id+"?fields=permalink_url", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject responseObject = response.getJSONObject();
                        try {
                            String url = responseObject.getString("permalink_url");
                            posts.get(position).setUrl(url);
                            Log.e("sayan===>","responseObject: "+url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("sayan======>",response.toString());
                    }
                }
        ).executeAsync();
    }

}
