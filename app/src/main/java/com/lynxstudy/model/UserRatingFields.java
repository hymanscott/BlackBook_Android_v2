package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by safiq on 12/06/15.
 */
public class UserRatingFields {

    int user_ratingfield_id;
    int user_id;
    String name;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public UserRatingFields(){

    }

    public UserRatingFields(int id,int user_id, String name, String status_update,boolean status_encrypt) {
        this.user_ratingfield_id = id;
        this.name = name;
        this.user_id = user_id;
        this.status_update= status_update;
        this.status_encrypt = status_encrypt;
    }

    public UserRatingFields(int user_id , String name, String status_update,boolean status_encrypt) {

        this.name = name;
        this.user_id = user_id;
        this.status_update= status_update;
        this.status_encrypt = status_encrypt;

    }

    public int getUser_ratingfield_id() {
        return user_ratingfield_id;
    }

    public void setUser_ratingfield_id(int user_ratingfield_id) {
        this.user_ratingfield_id = user_ratingfield_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public boolean isStatus_encrypt() {
        return status_encrypt;
    }

    public void setStatus_encrypt(boolean status_encrypt) {
        this.status_encrypt = status_encrypt;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void decryptUserRatingFields(){
        if (this.status_encrypt) {
            this.name = LynxManager.decryptString(name);
            this.status_encrypt = false;
        }
    }
    public void encryptUserRatingFields(){
        if (!this.status_encrypt) {
            this.name = LynxManager.encryptString(name);
            this.status_encrypt = true;
        }
    }
    public JSONObject getJson(){

        JSONObject ratField_jsonObj = new JSONObject();

        try {
            ratField_jsonObj.put("user_ratingfield_id", user_ratingfield_id);
            ratField_jsonObj.put("user_id", user_id);
            ratField_jsonObj.put("name", LynxManager.decryptString(name));
            ratField_jsonObj.put("status_update", status_update );
            ratField_jsonObj.put("status_encrypt", status_encrypt);
            ratField_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ratField_jsonObj;
    }
}