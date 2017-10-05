package com.lynxstudy.model;

/**
 * Created by Hari on 2017-09-26.
 */

public class UserBadges {
    private int user_badge_id;
    private int badge_id;
    private int user_id;
    private int is_shown;
    private String notes;
    private String status_update;
    private String created_at;

    public UserBadges() {
    }

    public UserBadges(int badge_id, int user_id, int is_shown, String badge_notes, String status_update) {
        this.badge_id = badge_id;
        this.user_id = user_id;
        this.is_shown = is_shown;
        this.notes = badge_notes;
        this.status_update = status_update;
    }

    public int getUser_badge_id() {
        return user_badge_id;
    }

    public void setUser_badge_id(int user_badge_id) {
        this.user_badge_id = user_badge_id;
    }

    public int getBadge_id() {
        return badge_id;
    }

    public void setBadge_id(int badge_id) {
        this.badge_id = badge_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIs_shown() {
        return is_shown;
    }

    public void setIs_shown(int is_shown) {
        this.is_shown = is_shown;
    }

    public String getBadge_notes() {
        return notes;
    }

    public void setBadge_notes(String badge_notes) {
        this.notes = badge_notes;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
