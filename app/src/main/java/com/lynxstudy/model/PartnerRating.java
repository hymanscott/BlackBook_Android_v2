package com.lynxstudy.model;

import com.lynxstudy.lynx.LynxManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by safiq on 26/06/15.
 */
public class PartnerRating {
    int partner_rating_id;
    int user_id;
    int partner_id;
    int user_rating_field_id;
    String rating;
    String rating_field;
    String status_update;
    String created_at;

    public PartnerRating() {
    }

    public PartnerRating(int user_id, int partner_id, int user_rating_field_id, String rating,String rating_field,String status_update) {
        this.user_id = user_id;
        this.partner_id = partner_id;
        this.user_rating_field_id = user_rating_field_id;
        this.status_update = status_update;
        this.rating = rating;
        this.rating_field =rating_field;
    }

    public int getPartner_rating_id() {
        return partner_rating_id;
    }

    public void setPartner_rating_id(int partner_rating_id) {
        this.partner_rating_id = partner_rating_id;
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

    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public int getUser_rating_field_id() {
        return user_rating_field_id;
    }

    public void setUser_rating_field_id(int user_rating_field_id) {
        this.user_rating_field_id = user_rating_field_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating_field() {
        return rating_field;
    }

    public void setRating_field(String rating_field) {
        this.rating_field = rating_field;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static class comparePartnerRating implements Comparator<PartnerRating> {
        public comparePartnerRating() {
        }

        @Override
        public int compare(PartnerRating rating1, PartnerRating rating2) {
            return rating2.getRating().compareToIgnoreCase(rating1.getRating());
        }
    }

    public JSONObject getJson(){

        JSONObject pr_jsonObj = new JSONObject();

        try {
            pr_jsonObj.put("partner_rating_id", partner_rating_id);
            pr_jsonObj.put("user_id", user_id);
            pr_jsonObj.put("partner_id", partner_id);
            pr_jsonObj.put("user_rating_field_id", user_rating_field_id);
            pr_jsonObj.put("rating", rating);
            pr_jsonObj.put("rating_field", rating_field);
            pr_jsonObj.put("status_update", status_update );
            pr_jsonObj.put("created_at", created_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pr_jsonObj;
    }
}
