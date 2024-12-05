package com.kun.plugin.publicexamtraining.common;

public interface Constants {

    String CACHE_HOME = System.getProperty("user.home") + "/.intellij-idea/plugin/publicexam";

    String OPEN_SITE_URL_POST = "https://www\\.gkzenti\\.cn/(paper|answer|explain)/\\d+";
    String OPEN_QUESTION_PAPER_PATTERN = "https://www\\.gkzenti\\.cn/paper/(\\d+)";
    String OPEN_ANSWER_PAPER_PATTERN = "https://www\\.gkzenti\\.cn/answer/(\\d+)";
    String OPEN_EXPLAIN_PAPER_PATTERN = "https://www\\.gkzenti\\.cn/explain/(\\d+)";

    String OPTIONS_SEPARATOR = "|*|";


    String QUESTION_PARAM = "questionList";
    String ANSWER_PARAM = "answerList";
    String EXPLAIN_PARAM = "explainList";

}
