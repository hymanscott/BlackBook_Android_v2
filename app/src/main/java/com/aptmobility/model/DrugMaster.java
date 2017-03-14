package com.aptmobility.model;

import java.lang.String;

/**
 * Created by safiq on 12/06/15.
 */
public class DrugMaster {



    int drug_id;
    String drugName;
    String created_at;

    public DrugMaster(){

    }

    public DrugMaster(String drugName) {

        this.drugName = drugName;

    }

    public int getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(int drug_id) {
        this.drug_id = drug_id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}