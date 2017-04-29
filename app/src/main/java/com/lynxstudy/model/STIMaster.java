package com.lynxstudy.model;

import java.lang.String;

/**
 * Created by safiq on 12/06/15.
 */
public class STIMaster {



    int sti_id;
    String stiName;
    String created_at;

    public STIMaster(){

    }

    public STIMaster( String stiName) {

        this.stiName = stiName;

    }

    public int getSti_id() {
        return sti_id;
    }

    public void setSti_id(int sti_id) {
        this.sti_id = sti_id;
    }

    public String getstiName() {
        return stiName;
    }

    public void setstiName(String stiName) {
        this.stiName = stiName;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}