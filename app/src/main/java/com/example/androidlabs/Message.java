package com.example.androidlabs;

public class Message {
    public String messageContents;
    public int imageID;
    public boolean fake;

    public Message (String s, int i, boolean b){
        messageContents = s;
        imageID = i;
        fake = b;
    }

    public String getMessageContents() {
        return messageContents;
    }
    public int getImageID() {
        return imageID;
    }

    public boolean isFake() {
        return fake;
    }
}
