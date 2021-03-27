package com.example.letschat.Models;

import java.util.ArrayList;

public class UserStaus {
    private String name,profileImage;
    private long last_updated;
    ArrayList<Status> statuses;

    public UserStaus(String name, String profileImage, long last_updated, ArrayList<Status> statuses) {
        this.name = name;
        this.profileImage = profileImage;
        this.last_updated = last_updated;
        this.statuses = statuses;
    }

    public UserStaus() {
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(long last_updated) {
        this.last_updated = last_updated;
    }
}
