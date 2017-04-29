package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by safiq on 16/06/15.
 */
public class UserSTIDiag {
    int	sti_diag_id;
    int	user_id;
    int	sti_id;
    String	is_baseline;
    String created_at;
    String status_update;
    boolean status_encrypt;

    public UserSTIDiag() {
    }

    public UserSTIDiag( int user_id, int sti_id, String is_baseline, String status_update,boolean status_encrypt) {
        this.user_id = user_id;
        this.sti_id = sti_id;
        this.is_baseline = is_baseline;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getSti_diag_id() {
        return sti_diag_id;
    }

    public void setSti_diag_id(int sti_diag_id) {
        this.sti_diag_id = sti_diag_id;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSti_id() {
        return sti_id;
    }

    public void setSti_id(int sti_id) {
        this.sti_id = sti_id;
    }

    public String getIs_baseline() {
        return is_baseline;
    }

    public void setIs_baseline(String is_baseline) {
        this.is_baseline = is_baseline;
    }

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
    public void decryptUserSTIDiag() {
        if (this.status_encrypt) {
            this.is_baseline = LynxManager.decryptString(is_baseline);
            this.status_encrypt = false;
        }
    }
    public void encryptUserSTIDiag() {
        if (!this.status_encrypt) {
            this.is_baseline = LynxManager.encryptString(is_baseline);
            this.status_encrypt = true;
        }
    }
    public JSONObject getJson(){

        JSONObject sti_jsonObj = new JSONObject();

        try {
            sti_jsonObj.put("sti_diag_id", sti_diag_id);
            sti_jsonObj.put("user_id", user_id);
            sti_jsonObj.put("sti_id", sti_id);
            sti_jsonObj.put("is_baseline", LynxManager.decryptString(is_baseline));
            sti_jsonObj.put("status_update", status_update );
            sti_jsonObj.put("status_encrypt", status_encrypt);
            sti_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sti_jsonObj;
    }
}