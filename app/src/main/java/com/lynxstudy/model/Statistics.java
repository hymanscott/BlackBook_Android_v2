package com.lynxstudy.model;

/**
 * Created by Hari on 2017-05-15.
 */

public class Statistics {
    private int statistics_id;
    private String from_activity;
    private String to_activity;
    private String activity;
    private String action;
    private String starttime;
    private String endtime;
    private String statusUpdate;

    public Statistics() {
    }

    public int getStatistics_id() {
        return statistics_id;
    }

    public void setStatistics_id(int statistics_id) {
        this.statistics_id = statistics_id;
    }

    public String getFrom_activity() {
        return from_activity;
    }

    public void setFrom_activity(String from_activity) {
        this.from_activity = from_activity;
    }

    public String getTo_activity() {
        return to_activity;
    }

    public void setTo_activity(String to_activity) {
        this.to_activity = to_activity;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(String statusUpdate) {
        this.statusUpdate = statusUpdate;
    }
}
