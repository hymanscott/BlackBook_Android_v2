package com.aptmobility.model;

import com.aptmobility.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by safiq on 26/06/15.
 */
public class EncounterSexType {
    int encounter_sex_type_id;
    int encounter_id;
    int user_id;
    String sex_type;
    String condom_use;
    String note;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public EncounterSexType() {
    }


    public EncounterSexType(int encounter_id,int user_id, String sex_type, String condom_use, String note, String status_update,boolean status_encrypt) {
        this.encounter_id = encounter_id;
        this.user_id = user_id;
        this.sex_type = sex_type;
        this.condom_use = condom_use;
        this.note = note;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getEncounter_sex_type_id() {
        return encounter_sex_type_id;
    }

    public void setEncounter_sex_type_id(int encounter_sex_type_id) {
        this.encounter_sex_type_id = encounter_sex_type_id;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public int getEncounter_id() {
        return encounter_id;
    }

    public void setEncounter_id(int encounter_id) {
        this.encounter_id = encounter_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSex_type() {
        return sex_type;
    }

    public void setSex_type(String sex_type) {
        this.sex_type = sex_type;
    }

    public String getCondom_use() {
        return condom_use;
    }

    public void setCondom_use(String condom_use) {
        this.condom_use = condom_use;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
    public void decryptEncounterSexType(){
        if (this.status_encrypt) {
            this.sex_type = LynxManager.decryptString(sex_type);
            this.condom_use = LynxManager.decryptString(condom_use);
            this.note = LynxManager.decryptString(note);
            this.status_encrypt = false;
        }
    }
    public void encryptEncounterSexType(){
        if (!this.status_encrypt) {
            this.sex_type = LynxManager.encryptString(sex_type);
            this.condom_use = LynxManager.encryptString(condom_use);
            this.note = LynxManager.encryptString(note);
            this.status_encrypt = true;
        }
    }

    public JSONObject getJson(){

        JSONObject encSexType_jsonObj = new JSONObject();

        try {
            encSexType_jsonObj.put("encounter_sex_type_id", encounter_sex_type_id);
            encSexType_jsonObj.put("encounter_id", encounter_id);
            encSexType_jsonObj.put("user_id", user_id);
            encSexType_jsonObj.put("sex_type", LynxManager.decryptString( sex_type));
            encSexType_jsonObj.put("condom_use", LynxManager.decryptString( condom_use));
            encSexType_jsonObj.put("note", LynxManager.decryptString(note));
            encSexType_jsonObj.put("status_update", status_update );
            encSexType_jsonObj.put("status_encrypt", status_encrypt);
            encSexType_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encSexType_jsonObj;
    }
}
