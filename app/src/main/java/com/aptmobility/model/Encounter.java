package com.aptmobility.model;

import com.aptmobility.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by safiq on 26/06/15.
 */
public class Encounter {
    int encounter_id;
    int user_id;
    String datetime;
    int partner_id;
    String rate_the_sex;
    String is_drug_used;
    String encounter_notes;
    String is_possible_sex_tomorrow;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public Encounter() {
    }

    public Encounter(int user_id, String datetime, int partner_id, String rate_the_sex, String is_drug_used, String encounter_notes, String is_possible_sex_tomorrow,String status_update,boolean status_encrypt) {
        this.user_id = user_id;
        this.datetime = datetime;
        this.partner_id = partner_id;
        this.rate_the_sex = rate_the_sex;
        this.is_drug_used = is_drug_used;
        this.encounter_notes = encounter_notes;
        this.is_possible_sex_tomorrow = is_possible_sex_tomorrow;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }
    public Encounter(int encounter_id,int user_id, String datetime, int partner_id, String rate_the_sex, String is_drug_used, String encounter_notes, String is_possible_sex_tomorrow,String status_update,boolean status_encrypt) {
        this.encounter_id=encounter_id;
        this.user_id = user_id;
        this.datetime = datetime;
        this.partner_id = partner_id;
        this.rate_the_sex = rate_the_sex;
        this.is_drug_used = is_drug_used;
        this.encounter_notes = encounter_notes;
        this.is_possible_sex_tomorrow = is_possible_sex_tomorrow;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getEncounter_id() {
        return encounter_id;
    }

    public void setEncounter_id(int encounter_id) {
        this.encounter_id = encounter_id;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getEncounter_partner_id() {
        return partner_id;
    }

    public void setEncounter_partner_id(int encounter_partner_id) {
        this.partner_id = encounter_partner_id;
    }

    public String getRate_the_sex() {
        return rate_the_sex;
    }

    public void setRate_the_sex(String rate_the_sex) {
        this.rate_the_sex = rate_the_sex;
    }

    public String getIs_drug_used() {
        return is_drug_used;
    }

    public void setIs_drug_used(String is_drug_used) {
        this.is_drug_used = is_drug_used;
    }

    public String getEncounter_notes() {
        return encounter_notes;
    }

    public void setEncounter_notes(String encounter_notes) {
        this.encounter_notes = encounter_notes;
    }
    public String getIs_possible_sex_tomorrow() {
        return is_possible_sex_tomorrow;
    }

    public void setIs_possible_sex_tomorrow(String is_possible_sex_tomorrow) {
        this.is_possible_sex_tomorrow = is_possible_sex_tomorrow;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getEncounter_user_id() {
        return user_id;
    }

    public void setEncounter_user_id(int encounter_user_id) {
        this.user_id = encounter_user_id;
    }

    public boolean getStatus_encrypt() {
        return status_encrypt;
    }

    public void setStatus_encrypt(boolean status_encrypt) {
        this.status_encrypt = status_encrypt;
    }

    public void decryptEncounter(){
        if (this.status_encrypt) {
            this.datetime = LynxManager.decryptString(datetime);
            this.rate_the_sex = LynxManager.decryptString(rate_the_sex);
            this.is_drug_used = LynxManager.decryptString(is_drug_used);
            this.encounter_notes = LynxManager.decryptString(encounter_notes);
            this.is_possible_sex_tomorrow = LynxManager.decryptString(is_possible_sex_tomorrow);
            this.status_encrypt = false;
        }
    }
    public void encryptEncounter() {
        if (!this.status_encrypt) {
            this.datetime = LynxManager.encryptString(datetime);
            this.is_drug_used = LynxManager.encryptString(is_drug_used);
            this.encounter_notes = LynxManager.encryptString(encounter_notes);
            this.is_possible_sex_tomorrow = LynxManager.encryptString(is_possible_sex_tomorrow);
            this.status_encrypt = true;
        }
    }
    public static class CompDate implements Comparator<Encounter> {
        private int mod = 1;
        public CompDate(boolean desc) {
            if (desc) mod =-1;
        }
        @Override
        public int compare(Encounter arg0, Encounter arg1) {
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
    public JSONObject getJson(){

        JSONObject enc_jsonObj = new JSONObject();

        try {
            enc_jsonObj.put("encounter_id", encounter_id);
            enc_jsonObj.put("user_id", user_id);
            enc_jsonObj.put("datetime", LynxManager.decryptString(datetime));
            enc_jsonObj.put("partner_id", partner_id);
            enc_jsonObj.put("rate_the_sex", LynxManager.decryptString(rate_the_sex));
            enc_jsonObj.put("is_drug_used", LynxManager.decryptString(is_drug_used));
            enc_jsonObj.put("encounter_notes", LynxManager.decryptString(encounter_notes));
            enc_jsonObj.put("is_possible_sex_tomorrow", LynxManager.decryptString(is_possible_sex_tomorrow));
            enc_jsonObj.put("status_update", status_update );
            enc_jsonObj.put("status_encrypt", status_encrypt);
            enc_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return enc_jsonObj;
    }
}
