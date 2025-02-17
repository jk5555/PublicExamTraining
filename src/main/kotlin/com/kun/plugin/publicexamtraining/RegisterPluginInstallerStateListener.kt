package com.kun.plugin.publicexamtraining

import com.intellij.ide.plugins.PluginManagerCore.getPlugin
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.kun.plugin.publicexamtraining.common.PluginConstants
import com.kun.plugin.publicexamtraining.ui.setting.PersistentConfig


class RegisterPluginInstallerStateListener : StartupActivity {
    override fun runActivity(project: Project) {
        val newVersion = getPlugin(PluginId.getId(PluginConstants.PLUGIN_ID))!!.version
        val config = PersistentConfig.getInstance().initConfig
        val oldVersion: String?
        if (config == null) {
            oldVersion = newVersion
        } else {
            oldVersion = config.pluginVersion
            config.pluginVersion = newVersion
        }

        if (newVersion != oldVersion) {
            HTMLEditorProvider.openEditor(
                project,
                "What's New in " + PluginConstants.PLUGIN_ID,
                PluginConstants.CHANGELOGURL,
                "'<div style='text-align: center;padding-top: 3rem'>" +
                        "<div style='padding-top: 1rem; margin-bottom: 0.8rem;'>Failed to load!</div>" +
                        "'<div><a href='" + PluginConstants.CHANGELOGURL + "' target='_blank'" +
                        "style='font-size: 2rem'>Open in browser</a></div>" +
                        "</div>"
            )
        }
    }


}
