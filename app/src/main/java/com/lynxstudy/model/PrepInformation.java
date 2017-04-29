package com.lynxstudy.model;

/**
 * Created by hariv_000 on 7/15/2015.
 */
public class PrepInformation {
    int prep_information_id;
    String prep_info_question;
    String prep_info_answer;
    String created_at;
    public PrepInformation() {
    }

    public PrepInformation(int id, String prep_info_question, String prep_info_answer, String created_at) {
        this.prep_information_id = id;
        this.prep_info_question = prep_info_question;
        this.prep_info_answer = prep_info_answer;
        this.created_at = created_at;
    }

    public PrepInformation(String prep_info_question, String prep_info_answer) {
        this.prep_info_question = prep_info_question;
        this.prep_info_answer = prep_info_answer;
    }

    public int getPrep_information_id() {
        return prep_information_id;
    }

    public void setPrep_information_id(int prep_information_id) {
        this.prep_information_id = prep_information_id;
    }

    public String getPrep_info_question() {
        return prep_info_question;
    }

    public void setPrep_info_question(String prep_info_question) {
        this.prep_info_question = prep_info_question;
    }

    public String getPrep_info_answer() {
        return prep_info_answer;
    }

    public void setPrep_info_answer(String prep_info_answer) {
        this.prep_info_answer = prep_info_answer;
    }
}
