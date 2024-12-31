package com.kun.plugin.publicexamtraining.ui;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WindowFactory implements ToolWindowFactory, DumbAware {

    private static final Map<String, ToolMainPanel> cache = new ConcurrentHashMap<>();

    public static ToolMainPanel getToolMainPanel(@NotNull String projectName) {
        return cache.get(projectName);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ToolMainPanel toolMainPanel = new ToolMainPanel(toolWindow, project);
        cache.put(project.getName(), toolMainPanel);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(toolMainPanel, "", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.setAnchor(ToolWindowAnchor.RIGHT, null);


    }


}
