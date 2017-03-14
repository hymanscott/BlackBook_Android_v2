package com.aptmobility.model;

import com.aptmobility.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hariv_000 on 11/28/2015.
 */
public class CloudMessages {

    int cloud_message_id;
    int user_id;
    String user_email;
    String token_id;
    String device;
    String device_info;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public CloudMessages() {
    }

    public CloudMessages(int user_id, String user_email, String token_id, String device, String device_info, String status_update, boolean status_encrypt ) {
        this.user_id = user_id;
        this.user_email = user_email;
        this.token_id = token_id;
        this.device = device;
        this.device_info = device_info;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;

    }

    public int getCloud_message_id() {
        return cloud_message_id;
    }

    public void setCloud_message_id(int cloud_message_id) {
        this.cloud_message_id = cloud_message_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
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

    public void decryptCloudMessages(){
        if (this.status_encrypt) {
            this.user_email = LynxManager.decryptString(user_email);
            this.token_id = LynxManager.decryptString(token_id);
            this.device = LynxManager.decryptString(device);
            this.device_info = LynxManager.decryptString(device_info);
            this.status_encrypt = false;
        }
    }
    public void encryptCloudMessages() {
        if (!this.status_encrypt) {
            this.user_email = LynxManager.encryptString(user_email);
            this.token_id = LynxManager.encryptString(token_id);
            this.device = LynxManager.encryptString(device);
            this.device_info = LynxManager.encryptString(device_info);
            this.status_encrypt = true;
        }
    }
    public JSONObject getJson(){

        JSONObject cm_jsonObj = new JSONObject();

        try {
            cm_jsonObj.put("cloud_message_id", cloud_message_id);
            cm_jsonObj.put("user_id", user_id);
            cm_jsonObj.put("user_email", LynxManager.decryptString(user_email));
            cm_jsonObj.put("token_id", LynxManager.decryptString(token_id));
            cm_jsonObj.put("device", LynxManager.decryptString(device));
            cm_jsonObj.put("device_info", LynxManager.decryptString(device_info));
            cm_jsonObj.put("status_update", status_update );
            cm_jsonObj.put("status_encrypt", status_encrypt);
            cm_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cm_jsonObj;
    }
}
