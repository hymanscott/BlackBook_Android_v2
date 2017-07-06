package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by safiq on 16/06/15.
 */
public class Partners {
    int	partner_id;
    int	user_id;
    String	nickname;
    String gender;
    String	hiv_status;
    String undetectable_for_sixmonth;
    String	is_added_to_partners;
    String created_at;
    String status_update;
    int partner_idle;
    boolean status_encrypt;

    public Partners() {
    }

    public Partners( int user_id, String nickname, String gender, String hiv_status,String undetectable_for_sixmonth, String is_added_to_partners, String status_update,boolean status_encrypt) {

        this.user_id = user_id;
        this.nickname = nickname;
        this.gender = gender;
        this.hiv_status = hiv_status;
        this.undetectable_for_sixmonth = undetectable_for_sixmonth;
        this.is_added_to_partners = is_added_to_partners;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public Partners(int id, int user_id, String nickname, String gender, String hiv_status,String undetectable_for_sixmonth, String is_added_to_partners, String status_update,boolean status_encrypt) {
        this.partner_id = id;
        this.user_id = user_id;
        this.nickname = nickname;
        this.gender = gender;
        this.hiv_status = hiv_status;
        this.undetectable_for_sixmonth = undetectable_for_sixmonth;
        this.is_added_to_partners = is_added_to_partners;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getPartner_idle() {
        return partner_idle;
    }

    public void setPartner_idle(int partner_idle) {
        this.partner_idle = partner_idle;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHiv_status() {
        return hiv_status;
    }

    public void setHiv_status(String hiv_status) {
        this.hiv_status = hiv_status;
    }

    public String getUndetectable_for_sixmonth() {
        return undetectable_for_sixmonth;
    }

    public void setUndetectable_for_sixmonth(String undetectable_for_sixmonth) {
        this.undetectable_for_sixmonth = undetectable_for_sixmonth;
    }

    public String getIs_added_to_partners() {
        return is_added_to_partners;
    }

    public void setIs_added_to_partners(String is_added_to_partners) {
        this.is_added_to_partners = is_added_to_partners;
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
    public void decryptPartners(){
        if (this.status_encrypt) {
            this.nickname = LynxManager.decryptString(nickname);
            this.gender =  LynxManager.decryptString(gender);
            this.hiv_status = LynxManager.decryptString(hiv_status);
            this.undetectable_for_sixmonth = LynxManager.decryptString(undetectable_for_sixmonth);
            this.is_added_to_partners = LynxManager.decryptString(is_added_to_partners);
            this.status_encrypt = false;
        }
    }
    public void encryptPartners(){
        if (!this.status_encrypt) {
            this.nickname = LynxManager.encryptString(nickname);
            this.gender =  LynxManager.encryptString(gender);
            this.hiv_status = LynxManager.encryptString(hiv_status);
            this.undetectable_for_sixmonth = LynxManager.encryptString(undetectable_for_sixmonth);
            this.is_added_to_partners = LynxManager.encryptString(is_added_to_partners);
            this.status_encrypt = true;
        }
    }
    public static class comparePartner implements Comparator<Partners>{
        public comparePartner() {
        }

        @Override
        public int compare(Partners partners1, Partners partners2) {
            return LynxManager.decryptString(partners1.getNickname()).compareToIgnoreCase(LynxManager.decryptString(partners2.getNickname()));
        }
    }
    public JSONObject getJson(){

        JSONObject partner_jsonObj = new JSONObject();

        try {
            partner_jsonObj.put("partner_id", partner_id);
            partner_jsonObj.put("user_id", user_id);
            partner_jsonObj.put("nickname", LynxManager.decryptString(nickname));
            partner_jsonObj.put("gender", LynxManager.decryptString(gender));
            partner_jsonObj.put("hiv_status", LynxManager.decryptString(hiv_status));
            partner_jsonObj.put("undetectable_for_sixmonth", LynxManager.decryptString(undetectable_for_sixmonth));
            partner_jsonObj.put("is_added_to_partners", LynxManager.decryptString(is_added_to_partners));
            partner_jsonObj.put("status_update", status_update );
            partner_jsonObj.put("status_encrypt", status_encrypt);
            partner_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return partner_jsonObj;
    }
}
