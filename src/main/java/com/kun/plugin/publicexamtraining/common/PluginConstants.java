package com.kun.plugin.publicexamtraining.common;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import java.awt.*;

public interface PluginConstants {
    String PLUGIN_ID = "Public Exam Training";


    String CUSTOM_FILE_NAME = "[$!{question.frontendQuestionId}]${question.title}";


    String CUSTOM_TEMPLATE = "${question.content}\n\n${question.code}";
    String LEVEL_COLOUR = "#5CB85C;#F0AD4E;#D9534F";


    String ACTION_PREFIX = "publicexam";
    String ACTION_SUFFIX = "";


    //todo : changelogurl, 指向GitHub上项目的changelog.md文件
    String CHANGELOGURL = "https://github.com/jk5555";


    //主要可以查看com.intellij.util.ui.UIUtil和com.intellij.ui.JBColor这两个类
    //获取字体大小
    Font font = UIUtil.getLabelFont();
    //获取背景颜色
    Color bg = JBColor.background();
    //获取字体颜色
    Color labelFontColor = UIUtil.getLabelFontColor(UIUtil.FontColor.NORMAL);
    //获取按钮的背景颜色
    JBColor buttonBg = JBColor.namedColor("Button.default.startBackground", JBUI.CurrentTheme.Focus.defaultButtonColor());
    //获取边框的颜色
    Color border = JBColor.border();

}
