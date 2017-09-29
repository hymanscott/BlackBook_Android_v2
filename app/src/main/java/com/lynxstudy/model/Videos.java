package com.lynxstudy.model;

/**
 * Created by Hari on 2017-04-25.
 */

public class Videos {
    int video_id;
    String name;
    String description;
    String video_url;
    String video_image_url;
    int priority;
    int is_watched;
    String created_at;

    public Videos(){

    }
    public Videos(String name, String description, String video_url, String video_image_url, int priority,int is_watched) {
        this.name = name;
        this.description = description;
        this.video_url = video_url;
        this.video_image_url = video_image_url;
        this.is_watched = is_watched;
        this.priority = priority;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_image_url() {
        return video_image_url;
    }

    public void setVideo_image_url(String video_image_url) {
        this.video_image_url = video_image_url;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getIs_watched() {
        return is_watched;
    }

    public void setIs_watched(int is_watched) {
        this.is_watched = is_watched;
    }
}
