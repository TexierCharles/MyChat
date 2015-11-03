package com.arthurbochard.cctexier.mychatapplication;

/**
 * Created by Arthur on 13/10/15.
 */
public class Message {

    private String uuid;
    private String login;
    private String message;

    public String getLogin() {
        return login;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(String uid, String message, String login) {
        this.uuid = uid;
        this.message = message;
        this.login = login;
    }

}

