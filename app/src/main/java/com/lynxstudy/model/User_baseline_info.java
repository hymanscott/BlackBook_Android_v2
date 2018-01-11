package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import java.lang.String;

/**
 * Created by safiq on 12/06/15.
 */
public class User_baseline_info {


    int baseline_id;
    int user_id;
    String hiv_negative_count;
    String hiv_positive_count;
    String hiv_unknown_count;
    String no_of_times_top_hivposs;
    String top_condom_use_percent;
    String no_of_times_bot_hivposs;
    String bottom_condom_use_percent;
    String is_primary_partner;
    int sexpro_score;
    String sexpro_prep;
    String sexpro_calculated_date;
    String status_update;
    boolean status_encrypt;
    String created_at;

    // constructors
    public User_baseline_info() {
    }

    public User_baseline_info(int user_id, String hiv_negative_count, String hiv_positive_count, String hiv_unknown_count, String no_of_times_top_hivposs, String top_condom_use_percent, String no_of_times_bot_hivposs, String bottom_condom_use_percent, String is_primary_partner, int sexpro_score,String sexpro_prep, String sexpro_calculated_date, String status_update,boolean status_encrypt) {

        this.user_id = user_id;
        this.hiv_negative_count = hiv_negative_count;
        this.hiv_positive_count = hiv_positive_count;
        this.hiv_unknown_count = hiv_unknown_count;
        this.no_of_times_top_hivposs = no_of_times_top_hivposs;
        this.top_condom_use_percent = top_condom_use_percent;
        this.no_of_times_bot_hivposs = no_of_times_bot_hivposs;
        this.bottom_condom_use_percent = bottom_condom_use_percent;
        this.is_primary_partner = is_primary_partner;
        this.sexpro_score =sexpro_score;
        this.sexpro_prep = sexpro_prep;
        this.sexpro_calculated_date = sexpro_calculated_date;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getBaseline_id() {
        return baseline_id;
    }

    public void setBaseline_id(int baseline_id) {
        this.baseline_id = baseline_id;
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

    public String getHiv_negative_count() {
        return hiv_negative_count;
    }

    public void setHiv_negative_count(String hiv_negative_count) {
        this.hiv_negative_count = hiv_negative_count;
    }

    public String getHiv_positive_count() {
        return hiv_positive_count;
    }

    public void setHiv_positive_count(String hiv_positive_count) {
        this.hiv_positive_count = hiv_positive_count;
    }

    public String getHiv_unknown_count() {
        return hiv_unknown_count;
    }

    public void setHiv_unknown_count(String hiv_unknown_count) {
        this.hiv_unknown_count = hiv_unknown_count;
    }

    public String getNo_of_times_top_hivposs() {
        return no_of_times_top_hivposs;
    }

    public void setNo_of_times_top_hivposs(String no_of_times_top_hivposs) {
        this.no_of_times_top_hivposs = no_of_times_top_hivposs;
    }

    public String getTop_condom_use_percent() {
        return top_condom_use_percent;
    }

    public void setTop_condom_use_percent(String top_condom_use_percent) {
        this.top_condom_use_percent = top_condom_use_percent;
    }

    public String getNo_of_times_bot_hivposs() {
        return no_of_times_bot_hivposs;
    }

    public void setNo_of_times_bot_hivposs(String no_of_times_bot_hivposs) {
        this.no_of_times_bot_hivposs = no_of_times_bot_hivposs;
    }

    public String getBottom_condom_use_percent() {
        return bottom_condom_use_percent;
    }

    public void setBottom_condom_use_percent(String bottom_condom_use_percent) {
        this.bottom_condom_use_percent = bottom_condom_use_percent;
    }

    public String getIs_primary_partner() {
        return is_primary_partner;
    }

    public void setIs_primary_partner(String is_primary_partner) {
        this.is_primary_partner = is_primary_partner;
    }

    public int getSexpro_score() {
        return sexpro_score;
    }

    public void setSexpro_score(int sexpro_score) {
        this.sexpro_score = sexpro_score;
    }

    public String getSexpro_prep() {
        return sexpro_prep;
    }

    public void setSexpro_prep(String sexpro_prep) {
        this.sexpro_prep = sexpro_prep;
    }

    public String getSexpro_calculated_date() {
        return sexpro_calculated_date;
    }

    public void setSexpro_calculated_date(String sexpro_calculated_date) {
        this.sexpro_calculated_date = sexpro_calculated_date;
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
    public void decryptUserBaselineInfo(){
        if (this.status_encrypt) {
            this.hiv_negative_count = LynxManager.decryptString(hiv_negative_count);
            this.hiv_positive_count = LynxManager.decryptString(hiv_positive_count);
            this.hiv_unknown_count = LynxManager.decryptString(hiv_unknown_count);
            this.no_of_times_top_hivposs = LynxManager.decryptString(no_of_times_top_hivposs);
            this.top_condom_use_percent = LynxManager.decryptString(top_condom_use_percent);
            this.no_of_times_bot_hivposs = LynxManager.decryptString(no_of_times_bot_hivposs);
            this.bottom_condom_use_percent = LynxManager.decryptString(bottom_condom_use_percent);
            this.is_primary_partner = LynxManager.decryptString(is_primary_partner);
            this.status_encrypt = false;
        }
    }
    public void encryptUserBaselineInfo(){
        if (!this.status_encrypt) {
            this.hiv_negative_count = LynxManager.encryptString(hiv_negative_count);
            this.hiv_positive_count = LynxManager.encryptString(hiv_positive_count);
            this.hiv_unknown_count = LynxManager.encryptString(hiv_unknown_count);
            this.no_of_times_top_hivposs = LynxManager.encryptString(no_of_times_top_hivposs);
            this.top_condom_use_percent = LynxManager.encryptString(top_condom_use_percent);
            this.no_of_times_bot_hivposs = LynxManager.encryptString(no_of_times_bot_hivposs);
            this.bottom_condom_use_percent = LynxManager.encryptString(bottom_condom_use_percent);
            this.is_primary_partner = LynxManager.encryptString(is_primary_partner);
            this.status_encrypt = true;
        }
    }
}
