package com.kun.plugin.publicexamtraining.ui;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class WindowFactory implements ToolWindowFactory, DumbAware {


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        //todo
        NotificationGroupManager.getInstance()
                .getNotificationGroup("PublicExamTraining Notification Group")
                .createNotification("工具窗口被加载了", NotificationType.INFORMATION)
                .notify(project);

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
