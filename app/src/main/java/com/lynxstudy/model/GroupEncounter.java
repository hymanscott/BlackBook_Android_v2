package com.lynxstudy.model;

/**
 * Created by Hari on 2018-04-09.
 */

public class GroupEncounter {
    int group_encounter_id;
    int user_id;
    int no_of_people;
    String datetime;
    String rate_the_sex;
    String condom_use;
    String cum_inside;
    String drunk_status;
    String notes;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public GroupEncounter() {
    }

    public GroupEncounter(int user_id, int no_of_people, String datetime, String rate_the_sex, String condom_use, String cum_inside, String drunk_status, String notes, String status_update, boolean status_encrypt, String created_at) {
        this.user_id = user_id;
        this.no_of_people = no_of_people;
        this.datetime = datetime;
        this.rate_the_sex = rate_the_sex;
        this.condom_use = condom_use;
        this.cum_inside = cum_inside;
        this.drunk_status = drunk_status;
        this.notes = notes;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
        this.created_at = created_at;
    }

    public int getNo_of_people() {
        return no_of_people;
    }

    public void setNo_of_people(int no_of_people) {
        this.no_of_people = no_of_people;
    }

    public int getGroup_encounter_id() {
        return group_encounter_id;
    }

    public void setGroup_encounter_id(int group_encounter_id) {
        this.group_encounter_id = group_encounter_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRate_the_sex() {
        return rate_the_sex;
    }

    public void setRate_the_sex(String rate_the_sex) {
        this.rate_the_sex = rate_the_sex;
    }

    public String getCondom_use() {
        return condom_use;
    }

    public void setCondom_use(String condom_use) {
        this.condom_use = condom_use;
    }

    public String getCum_inside() {
        return cum_inside;
    }

    public void setCum_inside(String cum_inside) {
        this.cum_inside = cum_inside;
    }

    public String getDrunk_status() {
        return drunk_status;
    }

    public void setDrunk_status(String drunk_status) {
        this.drunk_status = drunk_status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
}
