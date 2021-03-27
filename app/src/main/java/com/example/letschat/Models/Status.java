package com.example.letschat.Models;

public class Status {
    private String image_uri;
    private long timestamp;

    public Status(String image_uri, long timestamp) {
        this.image_uri = image_uri;
        this.timestamp = timestamp;
    }

    public Status() {
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
