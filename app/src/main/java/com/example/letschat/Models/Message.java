package com.example.letschat.Models;

import android.graphics.Bitmap;

public class Message {
    private String senderID,message,messageID,image_uri;
    private long timestamp;
    Bitmap setbitMapImage_uri;
    private int feeling;
    private  String is_seen;

    public Message(String senderID, String message, long timestamp) {
        this.senderID = senderID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message() {
    }

    public String getIs_seen() {
        return is_seen;
    }

    public void setIs_seen(String is_seen) {
        this.is_seen = is_seen;
    }

    public Bitmap getSetbitMapImage_uri() {
        return setbitMapImage_uri;
    }

    public void setSetbitMapImage_uri(Bitmap setbitMapImage_uri) {
        this.setbitMapImage_uri = setbitMapImage_uri;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageID() {
        return messageID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}
