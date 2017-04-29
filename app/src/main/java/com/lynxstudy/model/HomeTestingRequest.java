package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hariv_000 on 7/3/2015.
 */
public class HomeTestingRequest {
    int home_testing_request_id;
    int user_id;
    int testing_id;
    String address;
    String city;
    String state;
    String zip;
    String datetime;
    String status_update;
    boolean status_encrypt;
    String created_at;

    public HomeTestingRequest() {
    }

    public HomeTestingRequest(int id, int user_id, int testing_id, String address, String city, String state, String zip, String datetime,String status_update,boolean status_encrypt) {
        this.home_testing_request_id = id;
        this.user_id = user_id;
        this.testing_id = testing_id;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.datetime = datetime;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public HomeTestingRequest(int user_id, int testing_id, String address, String city, String state, String zip, String datetime, String status_update,boolean status_encrypt) {
        this.user_id = user_id;
        this.testing_id = testing_id;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.datetime = datetime;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getHome_testing_request_id() {
        return home_testing_request_id;
    }

    public void setHome_testing_request_id(int home_testing_request_id) {
        this.home_testing_request_id = home_testing_request_id;
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

    public int getTesting_id() {
        return testing_id;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTesting_id(int testing_id) {
        this.testing_id = testing_id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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
    public void decryptHomeTestingRequest(){
        if (this.status_encrypt) {
            this.address = LynxManager.decryptString(address);
            this.city = LynxManager.decryptString(city);
            this.state = LynxManager.decryptString(state);
            this.zip = LynxManager.decryptString(zip);
            this.datetime = LynxManager.decryptString(datetime);
            this.status_encrypt = false;
        }
    }
    public void encryptHomeTestingRequest(){
        if (!this.status_encrypt) {
            this.address = LynxManager.encryptString(address);
            this.city = LynxManager.encryptString(city);
            this.state = LynxManager.encryptString(state);
            this.zip = LynxManager.encryptString(zip);
            this.datetime = LynxManager.encryptString(datetime);
            this.status_encrypt = true;
        }
    }
    public JSONObject getJson(){

        JSONObject htr_jsonObj = new JSONObject();

        try {
            htr_jsonObj.put("home_testing_request_id", home_testing_request_id);
            htr_jsonObj.put("user_id", user_id);
            htr_jsonObj.put("testing_id", testing_id);
            htr_jsonObj.put("address", LynxManager.decryptString(address));
            htr_jsonObj.put("city", LynxManager.decryptString(city));
            htr_jsonObj.put("state", LynxManager.decryptString(state));
            htr_jsonObj.put("zip", LynxManager.decryptString(zip));
            htr_jsonObj.put("datetime", LynxManager.decryptString(datetime));
            htr_jsonObj.put("status_update", status_update );
            htr_jsonObj.put("status_encrypt", status_encrypt);
            htr_jsonObj.put("created_at", created_at);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return htr_jsonObj;
    }
}
