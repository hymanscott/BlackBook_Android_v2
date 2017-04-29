package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by safiq on 16/06/15.
 */
public class UserDrugUse {
    int	druguse_id;
    int	user_id;
    int	drug_id;
    String	is_baseline;
    String created_at;
    String status_update;
    boolean status_encrypt;

    public UserDrugUse() {
    }

    public UserDrugUse( int user_id, int drug_id, String is_baseline,String status_update,boolean status_encrypt) {

        this.user_id = user_id;
        this.drug_id = drug_id;
        this.is_baseline = is_baseline;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getDruguse_id() {
        return druguse_id;
    }

    public void setDruguse_id(int druguse_id) {
        this.druguse_id = druguse_id;
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

    public int getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(int drug_id) {
        this.drug_id = drug_id;
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

    public void decryptUserDrugUse(){
        if (this.status_encrypt) {
            this.is_baseline    =   LynxManager.decryptString(is_baseline);
            this.status_encrypt = false;
        }
    }
    public void encryptUserDrugUse(){
        if (!this.status_encrypt) {
            this.is_baseline    =   LynxManager.encryptString(is_baseline);
            this.status_encrypt = true;
        }
    }
    public JSONObject getJson(){

        JSONObject drugUse_jsonObj = new JSONObject();

        try {
            drugUse_jsonObj.put("druguse_id", druguse_id);
            drugUse_jsonObj.put("user_id", user_id);
            drugUse_jsonObj.put("drug_id", drug_id);
            drugUse_jsonObj.put("is_baseline", LynxManager.decryptString(is_baseline));
            drugUse_jsonObj.put("status_update", status_update );
            drugUse_jsonObj.put("status_encrypt", status_encrypt);
            drugUse_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drugUse_jsonObj;
    }
}
