package com.lynxstudy.model;

/**
 * Created by Hari on 2017-05-06.
 */

public class ChatMessage {
    private int id;
    private String message;
    private String sender;
    private String datetime;
    private String sender_pic;
    private String created_at;
    private String statusUpdate;

    public ChatMessage() {
    }

    public ChatMessage(int id, String message, String sender, String sender_pic,String datetime,String statusUpdate) {
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.datetime = datetime;
        this.sender_pic = sender_pic;
        this.statusUpdate = statusUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSender_pic() {
        return sender_pic;
    }

    public void setSender_pic(String sender_pic) {
        this.sender_pic = sender_pic;
    }

    public String getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(String statusUpdate) {
        this.statusUpdate = statusUpdate;
    }
}
