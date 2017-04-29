package com.lynxstudy.model;

/**
 * Created by hariv_000 on 7/4/2015.
 */
public class TestingLocations {
    int testing_location_id;
    String name;
    String address;
    String phone_number;
    String latitude;
    String longitude;
    String url;
    String created_at;
    public TestingLocations() {
    }

    public TestingLocations(int id, String name, String address,String phone_number, String latitude, String longitude, String url) {
        this.testing_location_id = id;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
    }

    public TestingLocations(String name, String address,String phone_number, String latitude, String longitude, String url) {
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTesting_location_id() {
        return testing_location_id;
    }

    public void setTesting_location_id(int testing_location_id) {
        this.testing_location_id = testing_location_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
