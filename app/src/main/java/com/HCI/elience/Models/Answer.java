package com.HCI.elience.models;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Answer {

    private String title;
    private String userId;
    private String createdOn;

    public Answer() {
    }

    public Answer(String title) {
        this.title = title;
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // get current user here
        this.createdOn = new Date().toString();
    }

    public Answer(String title, String userId, String createdOn) {
        this.title = title;
        this.userId = userId;
        this.createdOn = createdOn;
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

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", userId='" + userId + '\'' +
                ", createdOn='" + createdOn + '\'' +
                '}';
    }
}
