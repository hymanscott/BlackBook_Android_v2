package com.aptmobility.model;

import com.aptmobility.lynx.LynxManager;

/**
 * Created by safiq on 16/06/15.
 */
public class UserPrimaryPartner {
    int primarypartner_id;
    int	user_id;
    String	name;
    String	gender;
    String	hiv_status;
    String undetectable_for_sixmonth;
    String relationship_period;
    String	partner_have_other_partners;
    String	is_added_to_blackbook;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public UserPrimaryPartner() {
    }

    public UserPrimaryPartner( int user_id, String name, String gender, String hiv_status,String undetectable_for_sixmonth,String partner_have_other_partners,String relationship_period, String is_added_to_blackbook, String status_update,boolean status_encrypt) {

        this.user_id = user_id;
        this.name = name;
        this.gender = gender;
        this.hiv_status = hiv_status;
        this.undetectable_for_sixmonth = undetectable_for_sixmonth;
        this.partner_have_other_partners = partner_have_other_partners;
        this.relationship_period = relationship_period;
        this.is_added_to_blackbook = is_added_to_blackbook;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public UserPrimaryPartner(int primarypartner_id, int user_id, String name, String gender, String hiv_status, String undetectable_for_sixmonth, String relationship_period, String partner_have_other_partners, String is_added_to_blackbook, String status_update, boolean status_encrypt) {
        this.primarypartner_id = primarypartner_id;
        this.user_id = user_id;
        this.name = name;
        this.gender = gender;
        this.hiv_status = hiv_status;
        this.undetectable_for_sixmonth = undetectable_for_sixmonth;
        this.relationship_period = relationship_period;
        this.partner_have_other_partners = partner_have_other_partners;
        this.is_added_to_blackbook = is_added_to_blackbook;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public int getPrimarypartner_id() {
        return primarypartner_id;
    }

    public void setPrimarypartner_id(int primarypartner_id) {
        this.primarypartner_id = primarypartner_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRelationship_period() {
        return relationship_period;
    }

    public void setRelationship_period(String relationship_period) {
        this.relationship_period = relationship_period;
    }

    public String getPartner_have_other_partners() {
        return partner_have_other_partners;
    }

    public void setPartner_have_other_partners(String partner_have_other_partners) {
        this.partner_have_other_partners = partner_have_other_partners;
    }

    public String getIs_added_to_blackbook() {
        return is_added_to_blackbook;
    }

    public void setIs_added_to_blackbook(String is_added_to_blackbook) {
        this.is_added_to_blackbook = is_added_to_blackbook;
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
    public void decryptUserPrimaryPartner(){
        if (this.status_encrypt) {
            this.name = LynxManager.decryptString(name);
            this.gender = LynxManager.decryptString(gender);
            this.hiv_status = LynxManager.decryptString(hiv_status);
            this.undetectable_for_sixmonth = LynxManager.decryptString(undetectable_for_sixmonth);
            this.relationship_period = LynxManager.decryptString(relationship_period);
            this.partner_have_other_partners = LynxManager.decryptString(partner_have_other_partners);
            this.is_added_to_blackbook = LynxManager.decryptString(is_added_to_blackbook);
            this.status_encrypt = false;
        }
    }
    public void encryptUserPrimaryPartner(){
        if (!this.status_encrypt) {
            this.name = LynxManager.encryptString(name);
            this.gender = LynxManager.encryptString(gender);
            this.hiv_status = LynxManager.encryptString(hiv_status);
            this.undetectable_for_sixmonth = LynxManager.encryptString(undetectable_for_sixmonth);
            this.relationship_period = LynxManager.encryptString(relationship_period);
            this.partner_have_other_partners = LynxManager.encryptString(partner_have_other_partners);
            this.is_added_to_blackbook = LynxManager.encryptString(is_added_to_blackbook);
            this.status_encrypt = true;
        }
    }
}
