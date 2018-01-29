package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

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

    public static class CompDate implements Comparator<ChatMessage> {
        private int mod = 1;
        public CompDate(boolean desc) {
            if (desc) mod =-1;
        }
        @Override
        public int compare(ChatMessage arg1, ChatMessage arg0) {
            DateFormat formatter ;
            Date date0 = null;
            Date date1 = null;
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date0 = (Date)formatter.parse(LynxManager.decryptString(arg0.datetime));
                date1 = (Date)formatter.parse(LynxManager.decryptString(arg1.datetime));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return mod*date0.compareTo(date1);
        }
    }
}
