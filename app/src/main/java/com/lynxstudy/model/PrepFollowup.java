package com.lynxstudy.model;

/**
 * Created by hariprasad on 14/06/18.
 */

public class PrepFollowup {

    int prep_followup_id;
    int user_id;
    int is_weekly_checkin;
    String datetime;
    String prep;
    String score;
    String score_alt;
    String no_of_prep_days;
    String have_encounters_to_report;
    String status_update;
    String status_encrypt;
    String created_at;

    public PrepFollowup() {
    }

    public PrepFollowup(int prep_followup_id, int user_id, int is_weekly_checkin, String date, String prep, String score, String score_alt, String no_of_prep_days, String have_encounters_to_report, String status_update, String status_encrypt, String created_at) {
        this.prep_followup_id = prep_followup_id;
        this.is_weekly_checkin = is_weekly_checkin;
        this.user_id = user_id;
        this.datetime = date;
        this.prep = prep;
        this.score = score;
        this.score_alt = score_alt;
        this.no_of_prep_days = no_of_prep_days;
        this.have_encounters_to_report = have_encounters_to_report;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
        this.created_at = created_at;
    }

    public int getPrep_followup_id() {
        return prep_followup_id;
    }

    public void setPrep_followup_id(int prep_followup_id) {
        this.prep_followup_id = prep_followup_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIs_weekly_checkin() {
        return is_weekly_checkin;
    }

    public void setIs_weekly_checkin(int is_weekly_checkin) {
        this.is_weekly_checkin = is_weekly_checkin;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPrep() {
        return prep;
    }

    public void setPrep(String prep) {
        this.prep = prep;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore_alt() {
        return score_alt;
    }

    public void setScore_alt(String score_alt) {
        this.score_alt = score_alt;
    }

    public String getNo_of_prep_days() {
        return no_of_prep_days;
    }

    public void setNo_of_prep_days(String no_of_prep_days) {
        this.no_of_prep_days = no_of_prep_days;
    }

    public String getHave_encounters_to_report() {
        return have_encounters_to_report;
    }

    public void setHave_encounters_to_report(String have_encounters_to_report) {
        this.have_encounters_to_report = have_encounters_to_report;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public String getStatus_encrypt() {
        return status_encrypt;
    }

    public void setStatus_encrypt(String status_encrypt) {
        this.status_encrypt = status_encrypt;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
