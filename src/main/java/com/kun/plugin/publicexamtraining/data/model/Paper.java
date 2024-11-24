package com.kun.plugin.publicexamtraining.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;



public class Paper {

    /**
     * 跳转链接:eg: <a href="https://www.gkzenti.cn/paper/1720413487446">https://www.gkzenti.cn/paper/1720413487446</a>
     */
    @JsonProperty("No")
    private String jumpUrl;

    /**
     * 试卷标题:eg: 2024年湖北省公务员录用考试《行测》题（网友回忆版）
     */
    @JsonProperty("Title")
    private String title;

    /**
     * 试卷来源:eg: fenbi
     */
    @JsonProperty("Source")
    private String source;

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


}
