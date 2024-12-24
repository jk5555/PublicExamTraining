package com.kun.plugin.publicexamtraining.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.jcef.JBCefJSQuery;
import com.kun.plugin.publicexamtraining.util.NotificationUtils;

import javax.swing.*;

public class SystemTrainingPanel extends BaseHtmlPanel{

    public static final String htmlTemplatePath = "/template/test.html";

    public SystemTrainingPanel(Project project) {
        super(project, htmlTemplatePath);
    }


    @Override
    public JComponent buildComponent() {
        addEvent(request -> {
            NotificationUtils.notify(getProject(), "点击事件成功执行");
            return new JBCefJSQuery.Response("点击事件成功执行");
        });
        executeJavaScript("window.cefQuery = function(query) {" +
                "    return new Promise(function(resolve, reject) {" +
                "        window.cefQueryImpl({" +
                "            request: query.request," +
                "            onSuccess: function(response) { resolve(response); }," +
                "            onFailure: function(error_code, error_message) { reject(error_message); }" +
                "        });" +
                "    });" +
                "};");
        return getComponent();
    }




}
