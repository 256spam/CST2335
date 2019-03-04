package com.example.androidlabs;

public class Message {
    public String messageContents;
    public int imageID;
    public int side;
    public long id;

    public Message (String s, int i, int b, long d){
        messageContents = s;
        imageID = i;
        side = b;
        id = d;
    }

    public String getMessageContents() {
        return messageContents;
    }
    public int getImageID() {
        return imageID;
    }
    public int getside() {
        return side;
    }
    public long getID() {return id;}
}
