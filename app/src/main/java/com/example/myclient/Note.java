package com.example.myclient;


import com.google.firebase.firestore.Exclude;

import javax.annotation.Nullable;

public class Note {
    private String docId;
    private String email;
    private String password;

    public Note(){

    }

    public Note(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public void setDocId(String docId) {
        this.docId = docId;
    }
}
