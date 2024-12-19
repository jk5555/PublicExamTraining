package com.kun.plugin.publicexamtraining.ui;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class WindowFactory implements ToolWindowFactory, DumbAware {


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        //todo


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
