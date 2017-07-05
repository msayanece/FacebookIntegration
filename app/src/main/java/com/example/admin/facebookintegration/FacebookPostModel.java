package com.example.admin.facebookintegration;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 05-07-2017.
 */

public class FacebookPostModel implements Parcelable {

    private String story, created_time, id, url;

    public FacebookPostModel(String story, String created_time, String id){
        this.story = story;
        this.created_time = created_time;
        this.id = id;
    }

    protected FacebookPostModel(Parcel in) {
        story = in.readString();
        created_time = in.readString();
        id = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(story);
        dest.writeString(created_time);
        dest.writeString(id);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FacebookPostModel> CREATOR = new Creator<FacebookPostModel>() {
        @Override
        public FacebookPostModel createFromParcel(Parcel in) {
            return new FacebookPostModel(in);
        }

        @Override
        public FacebookPostModel[] newArray(int size) {
            return new FacebookPostModel[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
