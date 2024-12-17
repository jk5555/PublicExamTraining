package com.kun.plugin.publicexamtraining.common;

public interface PluginConstants {
    String PLUGIN_ID = "Public Exam Training";


    String CUSTOM_FILE_NAME = "[$!{question.frontendQuestionId}]${question.title}";


    String CUSTOM_TEMPLATE = "${question.content}\n\n${question.code}";
    String LEVEL_COLOUR = "#5CB85C;#F0AD4E;#D9534F";


    String ACTION_PREFIX = "publicexam";
    String ACTION_SUFFIX = "";


    //todo : changelogurl, 指向GitHub上项目的changelog.md文件
    String CHANGELOGURL = "https://github.com/jk5555";
}
