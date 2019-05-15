package com.example.forumapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Question implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };


    private String id;
    private long numUpVotes;
    private long numDownVotes;
    private String title;
    private String userId;
    private String createdOn;
    private int votedUpByUser;
    private int votedDownByUser;

    public Question(Parcel in) {
        this.id = in.readString();
        this.numUpVotes = in.readLong();
        this.numDownVotes = in.readLong();
        this.title = in.readString();
        this.userId = in.readString();
        this.createdOn = in.readString();
        this.votedUpByUser = in.readInt();
        this.votedDownByUser = in.readInt();
    }

    public Question() {

    }

    public Question(String title) {
        this.title = title;
        this.userId = "69"; // get current user here FirebaseAuth.getFALANA();
        this.createdOn = new Date().toString();
        this.votedDownByUser = 0;
        this.votedDownByUser = 0;
    }


    public Question(String title, String userId, String createdOn) {
        this.title = title;
        this.userId = userId;
        this.createdOn = createdOn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.numUpVotes);
        dest.writeLong(this.numDownVotes);
        dest.writeString(this.title);
        dest.writeString(this.userId);
        dest.writeString(this.createdOn);
        dest.writeInt(this.votedUpByUser);
        dest.writeInt(this.votedDownByUser);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedOn() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm aaa");
        return simpleDateFormat.format(new Date(this.createdOn));
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public long getNumUpVotes() {
        return numUpVotes;
    }

    public void setNumUpVotes(long numUpVotes) {
        this.numUpVotes = numUpVotes;
    }

    @Exclude
    public long getNumDownVotes() {
        return numDownVotes;
    }

    public void setNumDownVotes(long numDownVotes) {
        this.numDownVotes = numDownVotes;
    }

    @Exclude
    public int getVotedUpByUser() {
        return votedUpByUser;
    }

    public void setVotedUpByUser(int votedUpByUser) {
        this.votedUpByUser = votedUpByUser;
    }

    @Exclude
    public int getVotedDownByUser() {
        return votedDownByUser;
    }

    public void setVotedDownByUser(int votedDownByUser) {
        this.votedDownByUser = votedDownByUser;
    }

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", userId='" + userId + '\'' +
                ", createdOn='" + createdOn + '\'' +
                '}';
    }
}
