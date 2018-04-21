package com.lynxstudy.model;

/**
 * Created by Hari on 2018-04-09.
 */

public class GroupEncounterSexTypes {

    int group_encounter_sex_type_id;
    int group_encounter_id;
    String sex_type;
    int user_id;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public GroupEncounterSexTypes() {
    }

    public GroupEncounterSexTypes(int group_encounter_id, String sex_type, int user_id, String status_update, boolean status_encrypt, String created_at) {
        this.group_encounter_id = group_encounter_id;
        this.sex_type = sex_type;
        this.user_id = user_id;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
        this.created_at = created_at;
    }

    public GroupEncounterSexTypes(int group_encounter_id, String sex_type, int user_id, String status_update, boolean status_encrypt) {
        this.group_encounter_id = group_encounter_id;
        this.sex_type = sex_type;
        this.user_id = user_id;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getGroup_encounter_sex_type_id() {
        return group_encounter_sex_type_id;
    }

    public void setGroup_encounter_sex_type_id(int group_encounter_sex_type_id) {
        this.group_encounter_sex_type_id = group_encounter_sex_type_id;
    }

    public int getGroup_encounter_id() {
        return group_encounter_id;
    }

    public void setGroup_encounter_id(int group_encounter_id) {
        this.group_encounter_id = group_encounter_id;
    }

    public String getSex_type() {
        return sex_type;
    }

    public void setSex_type(String sex_type) {
        this.sex_type = sex_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
