package com.kun.plugin.publicexamtraining.ui;

import com.intellij.ui.jcef.JBCefBrowserBase;
import com.intellij.ui.jcef.JBCefJSQuery;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Function;

public class BaseHtmlPanel {

    private String htmlTemplate;

    //html面板支持渲染复杂html
    private JCEFHtmlPanel jcefHtmlPanel;

    //JavaScript事件处理
    private JBCefJSQuery jsQuery;


    public BaseHtmlPanel(String htmlTemplatePath) {
        // 读取本地HTML文件内容
        JCEFHtmlPanel jcefHtmlPanel = new JCEFHtmlPanel("");
        try {
            String htmlContent = IOUtils.toString(Objects.requireNonNull(BaseHtmlPanel.class.getResourceAsStream(htmlTemplatePath)), StandardCharsets.UTF_8);
            JBCefJSQuery jbCefJSQuery = JBCefJSQuery.create((JBCefBrowserBase) jcefHtmlPanel);
            this.htmlTemplate = htmlContent;
            this.jcefHtmlPanel = jcefHtmlPanel;
            this.jsQuery = jbCefJSQuery;
        } catch (IOException e) {
            String errorHtml = "<html><body><h1>Error loading Page</h1></body></html>";
            jcefHtmlPanel.loadHTML(errorHtml);
        }

    }

    public JComponent buildComponent() {
        jcefHtmlPanel.loadHTML(htmlTemplate);
        return jcefHtmlPanel.getComponent();
    }

    public void updateHtml(String html) {
        this.htmlTemplate = html;
    }

    public String getHtml() {
        return htmlTemplate;
    }

    public void addEvent(Function<? super String, ? extends JBCefJSQuery.Response> handler) {
        jsQuery.addHandler(handler);
    }

    public void executeJavaScript(String script) {
        jcefHtmlPanel.getCefBrowser().executeJavaScript(script, "", 0);
    }




}
