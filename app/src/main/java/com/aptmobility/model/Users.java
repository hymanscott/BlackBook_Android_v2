package com.aptmobility.model;

import com.aptmobility.lynx.LynxManager;

/**
 * Created by safiq on 12/06/15.
 */
public class Users {


    int user_id;
    String firstname;
    String lastname;
    String email;
    String password;
    String mobile;
    String passcode;
    String address;
    String city;
    String state;
    String zip;
    String securityquestion;
    String securityanswer;
    String dob;
    String race;
    String gender;
    String is_prep;
    String status_update;
    boolean status_encrypt;
    String created_at;


    // constructors
    public Users() {
    }

    public Users(int id, String firstname, String lastname, String email, String password, String mobile, String passcode, String address, String city, String state, String zip, String securityquestion, String securityanswer, String dob, String race, String gender, String is_prep, String status_update, boolean status_encrypt) {
        this.user_id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.passcode = passcode;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.securityquestion = securityquestion;
        this.securityanswer = securityanswer;
        this.dob = dob;
        this.race = race;
        this.gender = gender;
        this.is_prep = is_prep;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public Users(String firstname, String lastname, String email, String password, String mobile, String passcode, String address, String city, String state, String zip, String securityquestion, String securityanswer, String dob, String race, String gender, String is_prep, String status_update, boolean status_encrypt) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.passcode = passcode;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.securityquestion = securityquestion;
        this.securityanswer = securityanswer;
        this.dob = dob;
        this.race = race;
        this.gender = gender;
        this.is_prep = is_prep;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;

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

    public void setPassword(String password){
        this.password   =   password;
    }

    public void setPasscode(String passcode){
        this.passcode   =   passcode;
    }


    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public String getPassword(){
        return this.password;
    }

    public String getPasscode(){
        return this.passcode;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getSecurityquestion() {
        return securityquestion;
    }

    public void setSecurityquestion(String securityquestion) {
        this.securityquestion = securityquestion;
    }

    public String getSecurityanswer() {
        return securityanswer;
    }

    public void setSecurityanswer(String securityanswer) {
        this.securityanswer = securityanswer;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIs_prep() {
        return is_prep;
    }

    public void setIs_prep(String is_prep) {
        this.is_prep = is_prep;
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

    public void decryptUser(){
        if (this.status_encrypt) {
            this.firstname = LynxManager.decryptString(firstname);
            this.lastname = LynxManager.decryptString(lastname);
            this.email = LynxManager.decryptString(email);
            this.password = LynxManager.decryptString(password);
            this.mobile = LynxManager.decryptString(mobile);
            this.passcode = LynxManager.decryptString(passcode);
            this.address = LynxManager.decryptString(address);
            this.city = LynxManager.decryptString(city);
            this.state = LynxManager.decryptString(state);
            this.zip = LynxManager.decryptString(zip);
            this.securityquestion = LynxManager.decryptString(securityquestion);
            this.securityanswer = LynxManager.decryptString(securityanswer);
            this.dob = LynxManager.decryptString(dob);
            this.race = LynxManager.decryptString(race);
            this.gender = LynxManager.decryptString(gender);
            this.is_prep = LynxManager.decryptString(is_prep);
            this.status_encrypt = false;
        }
    }
    public void encryptUser(){
        if (!this.status_encrypt) {
            this.firstname = LynxManager.encryptString(firstname);
            this.lastname = LynxManager.encryptString(lastname);
            this.email = LynxManager.encryptString(email);
            this.password = LynxManager.encryptString(password);
            this.mobile = LynxManager.encryptString(mobile);
            this.passcode = LynxManager.encryptString(passcode);
            this.address = LynxManager.encryptString(address);
            this.city = LynxManager.encryptString(city);
            this.state = LynxManager.encryptString(state);
            this.zip = LynxManager.encryptString(zip);
            this.securityquestion = LynxManager.encryptString(securityquestion);
            this.securityanswer = LynxManager.encryptString(securityanswer);
            this.dob = LynxManager.encryptString(dob);
            this.race = LynxManager.encryptString(race);
            this.gender = LynxManager.encryptString(gender);
            this.is_prep = LynxManager.encryptString(is_prep);
            this.status_encrypt = true;
        }
    }
}
