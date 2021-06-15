package com.rechit.metime.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Time implements Parcelable {
    private String id;
    private Timestamp timeStamps;
    private String titleTime;
    private String trackingTime;

    public Time(){
    }

    public Time(String id, String titleTime, String trackingTime, Timestamp timeStamps) {
        this.id = id;
        this.titleTime = titleTime;
        this.trackingTime = trackingTime;
        this.timeStamps = timeStamps;
    }


    protected Time(Parcel in) {
        id = in.readString();
        timeStamps = in.readParcelable(Timestamp.class.getClassLoader());
        titleTime = in.readString();
        trackingTime = in.readString();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitleTime() {
        return titleTime;
    }

    public void setTitleTime(String titleTime) {
        this.titleTime = titleTime;
    }

    public String getTrackingTime() {  return trackingTime;}

    public void setTrackingTime(String trackingTime) { this.trackingTime = trackingTime; }

    public Timestamp getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(Timestamp timeStamps) {
        this.timeStamps = timeStamps;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(timeStamps, flags);
        dest.writeString(titleTime);
        dest.writeString(trackingTime);
    }
}
