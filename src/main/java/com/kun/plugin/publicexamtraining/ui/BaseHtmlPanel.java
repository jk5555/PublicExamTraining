package com.kun.plugin.publicexamtraining.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.jcef.JBCefBrowserBase;
import com.intellij.ui.jcef.JBCefJSQuery;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Function;

public abstract class BaseHtmlPanel {

    private final Project project;

    private String htmlTemplate;

    //html面板支持渲染复杂html
    private final JCEFHtmlPanel jcefHtmlPanel;

    //JavaScript事件处理
    private final JBCefJSQuery jsQuery;


    public BaseHtmlPanel(Project project, String htmlTemplatePath) {
        // 读取本地HTML文件内容
        JCEFHtmlPanel jcefHtmlPanel = new JCEFHtmlPanel("");
        JBCefJSQuery jbCefJSQuery = JBCefJSQuery.create((JBCefBrowserBase) jcefHtmlPanel);
        String htmlContent;
        try {
            htmlContent = IOUtils.toString(Objects.requireNonNull(BaseHtmlPanel.class.getResourceAsStream(htmlTemplatePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            htmlContent = "<html><body><h1>Error loading Page</h1></body></html>";
        }
        this.htmlTemplate = htmlContent;
        this.jcefHtmlPanel = jcefHtmlPanel;
        this.jsQuery = jbCefJSQuery;
        this.project = project;
        jcefHtmlPanel.loadHTML(htmlContent);

    }

    public abstract JComponent buildComponent();

    public void updateHtml(String html) {
        this.htmlTemplate = html;
        jcefHtmlPanel.loadHTML(html);
    }

    public String getHtml() {
        return htmlTemplate;
    }

    public Project getProject() {
        return project;
    }

    public void addEvent(Function<? super String, ? extends JBCefJSQuery.Response> handler) {
        jsQuery.addHandler(handler);
    }

    public void executeJavaScript(String script) {
        jcefHtmlPanel.getCefBrowser().executeJavaScript(script, "", 0);
    }

    protected JComponent getComponent() {
        return jcefHtmlPanel.getComponent();
    }


}
