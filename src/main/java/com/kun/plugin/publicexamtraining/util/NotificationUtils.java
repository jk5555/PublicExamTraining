package com.kun.plugin.publicexamtraining.util;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class NotificationUtils {

    private static final String NOTIFICATION_GROUP_ID = "PublicExamTraining Notification Group";

    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroupManager.getInstance().getNotificationGroup(NOTIFICATION_GROUP_ID);

    public static void notify(Project project, String message) {
        notify(project, message, NotificationType.INFORMATION);
    }

    public static void notify(Project project, String message, NotificationType notificationType) {
        NOTIFICATION_GROUP.createNotification(message, notificationType).notify(project);
    }


}
