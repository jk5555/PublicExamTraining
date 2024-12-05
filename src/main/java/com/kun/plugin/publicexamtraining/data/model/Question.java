package com.kun.plugin.publicexamtraining.data.model;

import com.kun.plugin.publicexamtraining.data.dao.entity.QuestionEntity;

public class Question extends QuestionEntity {

    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
