package com.aptmobility.model;

/**
 * Created by safiq on 12/06/15.
 */
public class TestNameMaster {



    int testing_id;
    String testName;
    String created_at;

    public TestNameMaster(){

    }

    public TestNameMaster(String testName) {

        this.testName = testName;

    }

    public int getTesting_id() {
        return testing_id;
    }

    public void setTesting_id(int testing_id) {
        this.testing_id = testing_id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}