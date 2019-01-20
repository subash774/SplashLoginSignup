package com.md.splashloginsignup;


import java.util.ArrayList;

public class User {

    private String userId;
    private String fullName;
    private String email;
    private ArrayList<Notes> notes;


    public User(String userId, String fullName, String email, ArrayList<Notes> notes) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.notes = notes;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Notes> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Notes> notes) {
        this.notes = notes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
