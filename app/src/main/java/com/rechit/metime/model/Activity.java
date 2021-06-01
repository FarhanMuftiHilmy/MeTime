package com.rechit.metime.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Activity implements Parcelable {
    public String id,title, description, date, time;

    public Activity(){
    }

    public Activity(String id, String title, String description, String date, String time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    protected Activity(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        time = in.readString();
    }

    public static final Creator<Activity> CREATOR = new Creator<Activity>() {
        @Override
        public Activity createFromParcel(Parcel in) {
            return new Activity(in);
        }

        @Override
        public Activity[] newArray(int size) {
            return new Activity[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(time);
    }
}
