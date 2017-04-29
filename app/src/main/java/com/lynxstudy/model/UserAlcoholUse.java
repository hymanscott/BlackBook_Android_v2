package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by safiq on 16/06/15.
 */
public class UserAlcoholUse {
    int	user_alcohol_use_id;
    int	user_drug_usage_id;
    int user_id;
    String	no_alcohol_in_week;
    String	no_alcohol_in_day;
    String	is_baseline;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public UserAlcoholUse() {
    }

    public UserAlcoholUse( int drugusage_id, int user_id,String no_alcohol_in_week, String no_alcohol_in_day, String is_baseline,String status_update,boolean status_encrypt) {

        this.user_drug_usage_id = drugusage_id;
        this.user_id = user_id;
        this.no_alcohol_in_week = no_alcohol_in_week;
        this.no_alcohol_in_day = no_alcohol_in_day;
        this.is_baseline    =   is_baseline;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getAlcohol_use_id() {
        return user_alcohol_use_id;
    }

    public void setAlcohol_use_id(int alcohol_use_id) {
        this.user_alcohol_use_id = alcohol_use_id;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public int getDrugusage_id() {
        return user_drug_usage_id;
    }

    public void setDrugusage_id(int drugusage_id) {
        this.user_drug_usage_id = drugusage_id;
    }

    public String getNo_alcohol_in_week() {
        return no_alcohol_in_week;
    }

    public void setNo_alcohol_in_week(String no_alcohol_in_week) {
        this.no_alcohol_in_week = no_alcohol_in_week;
    }

    public String getNo_alcohol_in_day() {
        return no_alcohol_in_day;
    }

    public void setNo_alcohol_in_day(String no_alcohol_in_day) {
        this.no_alcohol_in_day = no_alcohol_in_day;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
    public void decryptUserAlcoholUse(){
        if (this.status_encrypt) {
            this.no_alcohol_in_week = LynxManager.decryptString(no_alcohol_in_week);
            this.no_alcohol_in_day = LynxManager.decryptString(no_alcohol_in_day);
            this.is_baseline    =   LynxManager.decryptString(is_baseline);
            this.status_encrypt = false;
        }
    }
    public void encryptUserAlcoholUse(){
        if (!this.status_encrypt) {
            this.no_alcohol_in_week = LynxManager.encryptString(no_alcohol_in_week);
            this.no_alcohol_in_day = LynxManager.encryptString(no_alcohol_in_day);
            this.is_baseline    =   LynxManager.encryptString(is_baseline);
            this.status_encrypt = true;
        }
    }
    public JSONObject getJson(){

        JSONObject alcUse_jsonObj = new JSONObject();

        try {
            alcUse_jsonObj.put("user_alcohol_use_id", user_alcohol_use_id);
            alcUse_jsonObj.put("user_id", user_id);
            alcUse_jsonObj.put("user_drug_usage_id", user_drug_usage_id);
            alcUse_jsonObj.put("no_alcohol_in_week", LynxManager.decryptString(no_alcohol_in_week));
            alcUse_jsonObj.put("no_alcohol_in_day", LynxManager.decryptString(no_alcohol_in_day));
            alcUse_jsonObj.put("is_baseline", LynxManager.decryptString(is_baseline));
            alcUse_jsonObj.put("status_update", status_update );
            alcUse_jsonObj.put("status_encrypt", status_encrypt);
            alcUse_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return alcUse_jsonObj;
    }
}
