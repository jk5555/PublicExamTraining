package com.kun.plugin.publicexamtraining.ui;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class WindowFactory implements ToolWindowFactory, DumbAware {


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(new ToolMainPanel(toolWindow, project), "", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.setAnchor(ToolWindowAnchor.RIGHT, null);


    }

    /**
     * Perform additional initialization routine here.
     *
     * @param toolWindow
     */
    @Override
    public void init(@NotNull ToolWindow toolWindow) {
        //todo 额外的初始化操作
    }


}
