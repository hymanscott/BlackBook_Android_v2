package com.aptmobility.model;

/**
 * Created by hariv_000 on 7/16/2015.
 */
public class TestingInstructions {
    int testing_instruction_id;
    int testing_id;
    String question;
    String answer;
    String video_link;
    String pdf_link;
    String created_at;

    public TestingInstructions() {
    }

    public TestingInstructions(int testing_instruction_id, int testing_id, String question, String answer, String video_link, String pdf_link) {
        this.testing_instruction_id = testing_instruction_id;
        this.testing_id = testing_id;
        this.question = question;
        this.answer = answer;
        this.video_link = video_link;
        this.pdf_link = pdf_link;
    }

    public TestingInstructions(int testing_id, String question, String answer, String video_link, String pdf_link) {
        this.testing_id = testing_id;
        this.question = question;
        this.answer = answer;
        this.video_link = video_link;
        this.pdf_link = pdf_link;
    }


    public int getTesting_instruction_id() {
        return testing_instruction_id;
    }

    public void setTesting_instruction_id(int testing_instruction_id) {
        this.testing_instruction_id = testing_instruction_id;
    }

    public int getTesting_id() {
        return testing_id;
    }

    public void setTesting_id(int testing_id) {
        this.testing_id = testing_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getPdf_link() {
        return pdf_link;
    }

    public void setPdf_link(String pdf_link) {
        this.pdf_link = pdf_link;
    }

}

