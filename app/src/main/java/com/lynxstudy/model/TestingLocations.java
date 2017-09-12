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
    String type;
    String prep_clinic;
    String hiv_clinic;
    String sti_clinic;
    String operation_hours;
    String insurance;
    String ages;
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

    public TestingLocations(String name, String address,String phone_number, String latitude, String longitude, String url, String type,String prep_clinic,String hiv_clinic,String sti_clinic,String operation_hours,String insurance,String ages) {
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
        this.type = type;
        this.prep_clinic = prep_clinic;
        this.hiv_clinic = hiv_clinic;
        this.sti_clinic = sti_clinic;
        this.operation_hours = operation_hours;
        this.insurance = insurance;
        this.ages = ages;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrep_clinic() {
        return prep_clinic;
    }

    public void setPrep_clinic(String prep_clinic) {
        this.prep_clinic = prep_clinic;
    }

    public String getHiv_clinic() {
        return hiv_clinic;
    }

    public void setHiv_clinic(String hiv_clinic) {
        this.hiv_clinic = hiv_clinic;
    }

    public String getSti_clinic() {
        return sti_clinic;
    }

    public void setSti_clinic(String sti_clinic) {
        this.sti_clinic = sti_clinic;
    }

    public String getOperation_hours() {
        return operation_hours;
    }

    public void setOperation_hours(String operation_hours) {
        this.operation_hours = operation_hours;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getAges() {
        return ages;
    }

    public void setAges(String ages) {
        this.ages = ages;
    }
}
