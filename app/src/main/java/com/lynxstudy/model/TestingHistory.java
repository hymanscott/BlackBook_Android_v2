package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by hariv_000 on 7/2/2015.
 */
public class TestingHistory {
    int testing_history_id;
    int testing_id;
    int user_id;
    String testing_date;
    String status_update;
    boolean status_encrypt;
    String created_at;
    public TestingHistory() {
    }

    public TestingHistory(int id, int testing_id,int user_id, String testing_date,String status_update,boolean status_encrypt) {
        this.testing_history_id = id;
        this.testing_id = testing_id;
        this.user_id = user_id;
        this.testing_date = testing_date;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public TestingHistory(int testing_id,int user_id, String testing_date, String status_update,boolean status_encrypt) {
        this.testing_id = testing_id;
        this.user_id = user_id;
        this.testing_date = testing_date;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getTesting_history_id() {
        return testing_history_id;
    }

    public void setTesting_history_id(int testing_history_id) {
        this.testing_history_id = testing_history_id;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public int getTesting_id() {
        return testing_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTesting_date() {
        return testing_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setTesting_id(int testing_id) {
        this.testing_id = testing_id;
    }

    public void setTesting_date(String testing_date) {
        this.testing_date = testing_date;
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
    public void decryptTestingHistory(){
        if (this.status_encrypt) {
            this.testing_date = LynxManager.decryptString(testing_date);
            this.status_encrypt = false;
        }
    }
    public void encryptTestingHistory(){
        if (!this.status_encrypt) {
            this.testing_date = LynxManager.encryptString(testing_date);
            this.status_encrypt = true;
        }
    }
    public static class CompDate implements Comparator<TestingHistory> {
        private int mod = 1;
        public CompDate(boolean desc) {
            if (desc) mod =-1;
        }
        @Override
        public int compare(TestingHistory arg0, TestingHistory arg1) {
            DateFormat formatter ;
            Date date0 = null;
            Date date1 = null;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date0 = (Date)formatter.parse(LynxManager.decryptString(arg0.testing_date));
                date1 = (Date)formatter.parse(LynxManager.decryptString(arg1.testing_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return mod*date0.compareTo(date1);
        }
    }
    public JSONObject getJson(){

        JSONObject th_jsonObj = new JSONObject();

        try {
            th_jsonObj.put("testing_history_id", testing_history_id);
            th_jsonObj.put("testing_id", testing_id);
            th_jsonObj.put("user_id", user_id);
            th_jsonObj.put("testing_date", LynxManager.decryptString(testing_date));
            th_jsonObj.put("status_update", status_update );
            th_jsonObj.put("status_encrypt", status_encrypt);
            th_jsonObj.put("created_at", created_at);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return th_jsonObj;
    }
}
