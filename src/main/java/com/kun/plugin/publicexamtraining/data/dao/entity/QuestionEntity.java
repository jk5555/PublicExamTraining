package com.kun.plugin.publicexamtraining.data.dao.entity;

import java.sql.Date;

public class QuestionEntity {
    private Integer id;
    //试卷ID
    private Integer paperId;
    //题源
    private String source;
    //问题类型
    private String questionType;
    //问题题干
    private String questionStem;
    //答案选项，选项之间用|*|分隔
    private String answerSelect;
    //正确答案
    private String answer;
    //答案解析
    private String answerAnalysis;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionStem() {
        return questionStem;
    }

    public void setQuestionStem(String questionStem) {
        this.questionStem = questionStem;
    }

    public String getAnswerSelect() {
        return answerSelect;
    }

    public void setAnswerSelect(String answerSelect) {
        this.answerSelect = answerSelect;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerAnalysis() {
        return answerAnalysis;
    }

    public void setAnswerAnalysis(String answerAnalysis) {
        this.answerAnalysis = answerAnalysis;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
