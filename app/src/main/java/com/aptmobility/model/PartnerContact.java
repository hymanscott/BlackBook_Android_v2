package com.aptmobility.model;

import com.aptmobility.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by safiq on 25/06/15.
 */
public class PartnerContact {
    int partner_contact_id;
    int partner_id;
    int user_id;
    String name;
    String address;
    String city;
    String state;
    String zip;
    String phone;
    String email;
    String met_at;
    String handle;
    String partner_type;
    String partner_notes;
    String relationship_period;
    String	partner_have_other_partners;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public PartnerContact() {
    }

    public PartnerContact(int partner_id,int user_id, String name, String address, String city , String state, String zip,
                          String phone, String email, String met_at,String handle, String partner_type,
                          String partner_have_other_partners,String	relationship_period, String partner_notes, String status_update,boolean status_encrypt) {
        this.partner_id = partner_id;
        this.user_id = user_id;
        this.name = name;
        this.address = address;
        this.city= city;
        this.state=state;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.met_at = met_at;
        this.handle = handle;
        this.partner_type = partner_type;
        this.partner_notes = partner_notes;
        this.partner_have_other_partners = partner_have_other_partners;
        this.relationship_period = relationship_period;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getPartner_contact_id() {
        return partner_contact_id;
    }

    public void setPartner_contact_id(int partner_contact_id) {
        this.partner_contact_id = partner_contact_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMet_at() {
        return met_at;
    }

    public void setMet_at(String met_at) {
        this.met_at = met_at;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPartner_type() {
        return partner_type;
    }

    public void setPartner_type(String partner_type) {
        this.partner_type = partner_type;
    }

    public String getPartner_notes() {
        return partner_notes;
    }

    public void setPartner_notes(String partner_notes) {
        this.partner_notes = partner_notes;
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
    public void decryptPartnerContact(){
        if (this.status_encrypt) {
            this.name = LynxManager.decryptString(name);
            this.address = LynxManager.decryptString(address);
            this.city= LynxManager.decryptString(city);
            this.state= LynxManager.decryptString(state);
            this.zip = LynxManager.decryptString(zip);
            this.phone = LynxManager.decryptString(phone);
            this.email = LynxManager.decryptString(email);
            this.met_at = LynxManager.decryptString(met_at);
            this.handle = LynxManager.decryptString(handle);
            this.partner_type = LynxManager.decryptString(partner_type);
            this.partner_notes = LynxManager.decryptString(partner_notes);
            this.partner_have_other_partners = LynxManager.decryptString(partner_have_other_partners);
            this.relationship_period = LynxManager.decryptString(relationship_period);
            this.status_encrypt = false;
        }
    }
    public void encryptPartnerContact(){
        if (!this.status_encrypt) {
            this.name = LynxManager.encryptString(name);
            this.address = LynxManager.encryptString(address);
            this.city= LynxManager.encryptString(city);
            this.state= LynxManager.encryptString(state);
            this.zip = LynxManager.encryptString(zip);
            this.phone = LynxManager.encryptString(phone);
            this.email = LynxManager.encryptString(email);
            this.met_at = LynxManager.encryptString(met_at);
            this.handle = LynxManager.encryptString(handle);
            this.partner_type = LynxManager.encryptString(partner_type);
            this.partner_notes = LynxManager.encryptString(partner_notes);
            this.partner_have_other_partners = LynxManager.encryptString(partner_have_other_partners);
            this.relationship_period = LynxManager.encryptString(relationship_period);
            this.status_encrypt = true;
        }
    }
    public JSONObject getJson(){

        JSONObject pc_jsonObj = new JSONObject();

        try {
            pc_jsonObj.put("partner_contact_id", partner_contact_id);
            pc_jsonObj.put("partner_id", partner_id);
            pc_jsonObj.put("user_id", user_id);
            pc_jsonObj.put("name", LynxManager.decryptString(name));
            pc_jsonObj.put("address", LynxManager.decryptString(address));
            pc_jsonObj.put("city", LynxManager.decryptString( city));
            pc_jsonObj.put("state", LynxManager.decryptString(state));
            pc_jsonObj.put("zip", LynxManager.decryptString(zip));
            pc_jsonObj.put("phone", LynxManager.decryptString(phone));
            pc_jsonObj.put("email", LynxManager.decryptString(email));
            pc_jsonObj.put("met_at", LynxManager.decryptString(met_at));
            pc_jsonObj.put("handle", LynxManager.decryptString(handle));
            pc_jsonObj.put("partner_type", LynxManager.decryptString(partner_type));
            pc_jsonObj.put("partner_notes", LynxManager.decryptString(partner_notes));
            pc_jsonObj.put("relationship_period", LynxManager.decryptString(relationship_period));
            pc_jsonObj.put("partner_have_other_partners", LynxManager.decryptString(partner_have_other_partners));
            pc_jsonObj.put("status_update", status_update );
            pc_jsonObj.put("status_encrypt", status_encrypt);
            pc_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pc_jsonObj;
    }
}
