package com.kun.plugin.publicexamtraining.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.jcef.JBCefBrowserBase;
import com.intellij.ui.jcef.JBCefJSQuery;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public class MyToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 创建一个JBCefBrowser来展示HTML内容
        String localFilePath = Paths.get("/Users/kun/IdeaProjects/PublicExamTraining/src/main/resources/template/test.html").toUri().toString();
        JCEFHtmlPanel browser = new JCEFHtmlPanel(localFilePath);
        JBCefJSQuery jsQuery = JBCefJSQuery.create((JBCefBrowserBase) browser);
        jsQuery.addHandler(request -> {
            System.out.println(request);
            return null;
        });
        browser.getCefBrowser().executeJavaScript("let btn = document.getElementById('btn');\n" +
                "    btn.onclick = function (event) {\n" +
                "        alert('111');\n" +
                "        window.cefQuery({\n" +
                "            request: event, // 发送给 Java 的数据\n" +
                "            onSuccess: function(response) {\n" +
                "                alert('Response from Java:');\n" +
                "            },\n" +
                "            onFailure: function(error) {\n" +
                "                alert('Error:');\n" +
                "            }\n" +
                "        });\n" +
                "    }", localFilePath, 0);

        // 创建一个Content对象并添加到工具窗口
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(browser.getComponent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
