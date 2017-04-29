package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hariv_000 on 9/30/2015.
 */
public class TestingHistoryInfo {
    int testing_history_info_id;
    int testing_history_id;
    int sti_id;
    int user_id;
    String test_status;
    String attachment;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public TestingHistoryInfo(){}
    public TestingHistoryInfo(int testing_history_id,int user_id, int sti_id, String test_status,String attachment,String status_update,boolean status_encrypt) {
        this.testing_history_id = testing_history_id;
        this.sti_id = sti_id;
        this.user_id = user_id;
        this.test_status = test_status;
        this.attachment = attachment;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTesting_history_info_id() {
        return testing_history_info_id;
    }

    public void setTesting_history_info_id(int testing_history_info_id) {
        this.testing_history_info_id = testing_history_info_id;
    }

    public int getTesting_history_id() {
        return testing_history_id;
    }

    public void setTesting_history_id(int testing_history_id) {
        this.testing_history_id = testing_history_id;
    }

    public int getSti_id() {
        return sti_id;
    }

    public void setSti_id(int sti_id) {
        this.sti_id = sti_id;
    }

    public String getTest_status() {
        return test_status;
    }

    public void setTest_status(String test_status) {
        this.test_status = test_status;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
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

    public void decryptTestingHistoryInfo(){
        if (this.status_encrypt) {
            this.test_status = LynxManager.decryptString(test_status);
            this.attachment = LynxManager.decryptString(attachment);
            this.status_encrypt = false;
        }
    }
    public void encryptTestingHistoryInfo(){
        if (!this.status_encrypt) {
            this.test_status = LynxManager.encryptString(test_status);
            this.attachment = LynxManager.encryptString(attachment);
            this.status_encrypt = true;
        }
    }
    public JSONObject getJson(){

        JSONObject thi_jsonObj = new JSONObject();

        try {
            thi_jsonObj.put("testing_history_info_id", testing_history_info_id);
            thi_jsonObj.put("testing_history_id", testing_history_id);
            thi_jsonObj.put("sti_id", sti_id);
            thi_jsonObj.put("user_id", user_id);
            thi_jsonObj.put("test_status", LynxManager.decryptString(test_status));
            thi_jsonObj.put("attachment", LynxManager.decryptString(attachment));
            thi_jsonObj.put("status_update", status_update );
            thi_jsonObj.put("status_encrypt", status_encrypt);
            thi_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return thi_jsonObj;
    }
}
