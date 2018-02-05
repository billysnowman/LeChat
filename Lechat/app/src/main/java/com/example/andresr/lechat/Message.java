package com.example.andresr.lechat;

/**
 * Represent a single message sended by an user
 *
 * Created by Hugo Gresse on 26/11/2017.
 */

public class Message {

    public String key;
    public String content;
    public String userName;
    public String userEmail;
    public Long   timestamp;

    public Message() {
        // Empty constructor for Firebase
        this(null, null, null, null, null);
    }

    public Message(String content, String userName, String userEmail, Long timestamp) {
        this(null, content, userName, userEmail, timestamp);
    }

    public Message(String key, String content, String userName, String userEmail, Long timestamp) {
        this.key = key;
        this.content = content;
        this.userName = userName;
        this.userEmail = userEmail;
        this.timestamp = timestamp;
    }
}

