package com.lynxstudy.model;

/**
 * Created by Hari on 2017-09-25.
 */

public class BadgesMaster {

    int badge_id;
    String badge_name;
    String badge_description;
    String badge_notes;
    String badge_icon;
    String badge_activity;
    String created_at;

    public BadgesMaster() {
    }

    public BadgesMaster(int badge_id, String badge_name, String badge_description,String badge_notes, String badge_icon,String badge_activity) {
        this.badge_id = badge_id;
        this.badge_name = badge_name;
        this.badge_description = badge_description;
        this.badge_notes = badge_notes;
        this.badge_icon = badge_icon;
        this.badge_activity = badge_activity;
    }

    public int getBadge_id() {
        return badge_id;
    }

    public void setBadge_id(int badge_id) {
        this.badge_id = badge_id;
    }

    public String getBadge_name() {
        return badge_name;
    }

    public void setBadge_name(String badge_name) {
        this.badge_name = badge_name;
    }

    public String getBadge_description() {
        return badge_description;
    }

    public void setBadge_description(String badge_description) {
        this.badge_description = badge_description;
    }

    public String getBadge_notes() {
        return badge_notes;
    }

    public void setBadge_notes(String badge_notes) {
        this.badge_notes = badge_notes;
    }

    public String getBadge_icon() {
        return badge_icon;
    }

    public void setBadge_icon(String badge_icon) {
        this.badge_icon = badge_icon;
    }

    public String getBadge_activity() {
        return badge_activity;
    }

    public void setBadge_activity(String badge_activity) {
        this.badge_activity = badge_activity;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
