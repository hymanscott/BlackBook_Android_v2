package com.lynxstudy.model;

/**
 * Created by Hari on 2018-03-02.
 */

public class AppAlerts {

    public AppAlerts() {
    }

    int id;
    String name;
    String created_date;
    String modified_date;

    public AppAlerts(String name, String created_date, String modified_date) {
        this.name = name;
        this.created_date = created_date;
        this.modified_date = modified_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }
}
