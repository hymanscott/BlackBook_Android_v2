package com.aptmobility.model;

import com.aptmobility.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hariv_000 on 7/2/2015.
 */
public class TestingReminder {
    int	testing_reminder_id;
    int	user_id;
    int	reminder_flag;
    String notification_day;
    String notification_time;
    String reminder_notes;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public TestingReminder() {
    }

    public TestingReminder(int id, int user_id, int reminder_flag, String notification_day,String notification_time, String reminder_notes,String status_update,boolean status_encrypt) {
        this.testing_reminder_id = id;
        this.user_id = user_id;
        this.reminder_flag = reminder_flag;
        this.notification_day = notification_day;
        this.notification_time = notification_time;
        this.reminder_notes = reminder_notes;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public TestingReminder(int user_id, int reminder_flag, String notification_day,String notification_time, String reminder_notes,String status_update,boolean status_encrypt) {
        this.user_id = user_id;
        this.reminder_flag = reminder_flag;
        this.notification_day = notification_day;
        this.notification_time = notification_time;
        this.reminder_notes = reminder_notes;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getTesting_reminder_id() {
        return testing_reminder_id;
    }

    public void setTesting_reminder_id(int testing_reminder_id) {
        this.testing_reminder_id = testing_reminder_id;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public int getReminder_flag() {
        return reminder_flag;
    }

    public void setReminder_flag(int reminder_flag) {
        this.reminder_flag = reminder_flag;
    }

    public int getUser_id(){return user_id;}

    public void setUser_id(int user_id){ this.user_id = user_id;}

    public String getNotification_day() { return notification_day; }

    public void setNotification_day(String notification_day) { this.notification_day = notification_day; }

    public String getNotification_time() { return notification_time; }

    public void setNotification_time(String notification_time) { this.notification_time = notification_time; }

    public String getReminder_notes(){return reminder_notes;}

    public void setReminder_notes(String reminder_notes){ this.reminder_notes = reminder_notes;}

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean getStatus_encrypt() {
        return status_encrypt;
    }

    public void setStatus_encrypt(boolean status_encrypt) {
        this.status_encrypt = status_encrypt;
    }
    public void decryptTestingReminder(){
        if (this.status_encrypt) {
            this.notification_day = LynxManager.decryptString(notification_day);
            this.notification_time = LynxManager.decryptString(notification_time);
            this.reminder_notes = LynxManager.decryptString(reminder_notes);
            this.status_encrypt = false;
        }
    }
    public void encryptTestingReminder(){
        if (!this.status_encrypt) {
            this.notification_day = LynxManager.encryptString(notification_day);
            this.notification_time = LynxManager.encryptString(notification_time);
            this.reminder_notes = LynxManager.encryptString(reminder_notes);
            this.status_encrypt = true;
        }
    }
    public JSONObject getJson(){

        JSONObject tr_jsonObj = new JSONObject();

        try {
            tr_jsonObj.put("testing_reminder_id", testing_reminder_id);
            tr_jsonObj.put("user_id", user_id);
            tr_jsonObj.put("reminder_flag", reminder_flag);
            tr_jsonObj.put("notification_day", LynxManager.decryptString(notification_day));
            tr_jsonObj.put("notification_time", LynxManager.decryptString(notification_time));
            tr_jsonObj.put("reminder_notes", LynxManager.decryptString(reminder_notes));
            tr_jsonObj.put("status_update", status_update );
            tr_jsonObj.put("status_encrypt", status_encrypt);
            tr_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tr_jsonObj;
    }
}
