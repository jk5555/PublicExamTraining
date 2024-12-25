package com.kun.plugin.publicexamtraining.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.kun.plugin.publicexamtraining.ui.jcef.JcefFactory;
import com.kun.plugin.publicexamtraining.util.ResourceUtils;

import javax.swing.*;

public class SystemTrainingPanel implements Disposable {

    private static final String TAG = "SystemTraining";
    private final JCEFHtmlPanel jcefHtmlPanel;
    private final Project project;
    private final ToolWindow toolWindow;


    public SystemTrainingPanel(ToolWindow toolWindow, Project project) {
        jcefHtmlPanel = JcefFactory.getBrowser(project, TAG);
        this.project = project;
        this.toolWindow = toolWindow;
        init();
    }

    private void init() {
        jcefHtmlPanel.setHtml(ResourceUtils.loadHtml("/template/test.html"));
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
    }


}
