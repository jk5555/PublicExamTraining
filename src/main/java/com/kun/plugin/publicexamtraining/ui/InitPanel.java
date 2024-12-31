package com.kun.plugin.publicexamtraining.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.kun.plugin.publicexamtraining.ui.jcef.JcefFactory;
import com.kun.plugin.publicexamtraining.util.ResourceUtils;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandlerAdapter;

import javax.swing.*;

public class InitPanel implements Disposable {

    private static final String TAG = "InitTab";
    private final JCEFHtmlPanel jcefHtmlPanel;
    private final Project project;
    private final ToolWindow toolWindow;


    public InitPanel(ToolWindow toolWindow, Project project) {
        jcefHtmlPanel = JcefFactory.getBrowser(project, TAG);
        this.project = project;
        this.toolWindow = toolWindow;
        init();
    }

    private void init() {
        final String projectName = project.getName();
        jcefHtmlPanel.setHtml(ResourceUtils.loadHtml("/template/initPanel.html"));
        jcefHtmlPanel.getJBCefClient().addLoadHandler(new CefLoadHandlerAdapter() {
            @Override
            public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
                if (frame.isMain()) {
                    browser.executeJavaScript("let newDiv = document.createElement(\"div\");\n" +
                            "    newDiv.id = \"projectId\";\n" +
                            "    newDiv.style.display = \"none\";\n" +
                            "    newDiv.innerText = \""+projectName+"\";\n" +
                            "    document.body.appendChild(newDiv);", frame.getURL(), 0);
                }
            }
        }, jcefHtmlPanel.getCefBrowser());
    }


    public JComponent getComponent() {
        return jcefHtmlPanel.getComponent();
    }

    /**
     * Usually not invoked directly, see class javadoc.
     */
    @Override
    public void dispose() {
        Disposer.dispose(this);
        Disposer.dispose(jcefHtmlPanel);
    }


}
