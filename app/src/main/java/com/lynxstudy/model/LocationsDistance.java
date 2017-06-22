package com.lynxstudy.model;

import java.util.Comparator;

/**
 * Created by hariv_000 on 7/22/2015.
 */
public class LocationsDistance {
    int location_distance_id;
    double latitude;
    double longitude;
    int distance;
    String name;
    String type;
    public LocationsDistance() {
    }

    public LocationsDistance(int id, double latitude, double longitude, int distance, String name,String type) {
        this.location_distance_id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getLocation_distance_id() {
        return location_distance_id;
    }

    public void setLocation_distance_id(int location_distance_id) {
        this.location_distance_id = location_distance_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Comparator<LocationsDistance> LocDist = new Comparator<LocationsDistance>() {

        @Override
        public int compare(LocationsDistance lhs, LocationsDistance rhs) {

            int dist_one = (int) lhs.getDistance();
            int dist_two = (int) rhs.getDistance();
            return dist_one-dist_two;
        }};
    }