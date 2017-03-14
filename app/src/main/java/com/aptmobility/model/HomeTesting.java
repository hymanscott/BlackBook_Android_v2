package com.aptmobility.model;

import com.aptmobility.lynx.LynxManager;

/**
 * Created by hariv_000 on 7/2/2015.
 */
public class HomeTesting {
    int id;
    int testing_id;
    String question;
    String answer;
    String video_link;
    String pdf_link;
    String status_update;
    String created_at;
    boolean status_encrypt;
    public HomeTesting() {
    }

    public HomeTesting(int id, int testing_id, String question, String answer, String video_link, String pdf_link,String status_update,boolean status_encrypt) {
        this.id = id;
        this.testing_id = testing_id;
        this.question = question;
        this.answer= answer;
        this.video_link = video_link;
        this.pdf_link = pdf_link;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public HomeTesting(int testing_id, String question, String answer,String video_link, String pdf_link,String status_update,boolean status_encrypt) {
        this.testing_id = testing_id;
        this.question = question;
        this.answer= answer;
        this.video_link = video_link;
        this.pdf_link = pdf_link;
        this.status_update = status_update;
        this.status_encrypt = status_encrypt;
    }

    public int getId() {
        return id;
    }

    public String getStatus_update() {
        return status_update;
    }

    public void setStatus_update(String status_update) {
        this.status_update = status_update;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTesting_id() {
        return testing_id;
    }

    public String getVideo_link() {
        return video_link;
    }

    public String getPdf_link() {
        return pdf_link;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTesting_id(int testing_id) {
        this.testing_id = testing_id;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public void setPdf_link(String pdf_link) {
        this.pdf_link = pdf_link;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean getStatus_encrypt() {
        return status_encrypt;
    }

    public void setStatus_encrypt(boolean status_encrypt) {
        this.status_encrypt = status_encrypt;
    }
    public void decryptHomeTesting(){
        if (this.status_encrypt) {
            this.question = LynxManager.decryptString(question);
            this.answer= LynxManager.decryptString(answer);
            this.video_link = LynxManager.decryptString(video_link);
            this.pdf_link = LynxManager.decryptString(pdf_link);
            this.status_encrypt = false;
        }
    }
    public void encryptHomeTesting(){
        if (!this.status_encrypt) {
            this.question = LynxManager.encryptString(question);
            this.answer= LynxManager.encryptString(answer);
            this.video_link = LynxManager.encryptString(video_link);
            this.pdf_link = LynxManager.encryptString(pdf_link);
            this.status_encrypt = true;
        }
    }
}
