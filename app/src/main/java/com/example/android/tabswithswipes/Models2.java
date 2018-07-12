package com.example.android.tabswithswipes;

import java.util.ArrayList;

public class Models2 {

    private String email_adr;
    private String username;
    //private ArrayList<Post> activities;
    private String bio;
    private Long friends;
    private String profile_photo;
    private String name;

    public Models2(String email_adr, String name, String username){
        this.email_adr = email_adr;
        this.name = name;
        this.username = username;
        //this.activities = new ArrayList<Post>();
        this.friends = 0L;
        this.bio = "post your bio here";
        this.profile_photo = "gs://reactions-4f81f.appspot.com/ANDROID.png";
    }

    public String getEmail_adr() {
        return email_adr;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public Long getFriends() {
        return friends;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public Models2(){

    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Model{" +
                "email_adr=" + email_adr+" ," + "username" +username+" }";

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail_adr(String email_adr){
        this.email_adr = email_adr;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setFriends(Long friends) {
        this.friends = friends;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public void setName(String name) {
        this.name = name;
    }
}