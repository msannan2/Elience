package com.HCI.elience.models;

import java.util.Date;

public class UserModel {
    private String Username;
    private String FirebaseUID;
    private String Name;
    private String Email;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getFirebaseUID() {
        return FirebaseUID;
    }

    public void setFirebaseUID(String firebaseUID) {
        FirebaseUID = firebaseUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    private Date DOB;

    public UserModel(String username, String firebaseUID, String name, String email, Date dob) {
        Username = username;
        FirebaseUID = firebaseUID;
        Name = name;
        Email = email;
        DOB = dob;
    }
}
