package com.kun.plugin.publicexamtraining.ui.setting;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.kun.plugin.publicexamtraining.common.PluginConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


@State(name = "PersistentConfig" + PluginConstants.ACTION_SUFFIX, storages = {@Storage(value = PluginConstants.ACTION_PREFIX + "-config.xml", roamingType = RoamingType.DISABLED)})
public class PersistentConfig implements PersistentStateComponent<PersistentConfig> {


    private static final String INITNAME = "initConfig";

    private final Map<String, Config> initConfig = new HashMap<>();


    public static PersistentConfig getInstance() {
        return ServiceManager.getService(PersistentConfig.class);
    }

    @Nullable
    @Override
    public PersistentConfig getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PersistentConfig persistentConfig) {
        XmlSerializerUtil.copyBean(persistentConfig, this);
    }


    @Nullable
    public Config getInitConfig() {
        return initConfig.get(INITNAME);
    }

    public void setInitConfig(Config config) {
        initConfig.put(INITNAME, config);
    }

    @NotNull
    public Config getConfig() {
        Config config = getInitConfig();
        if (config == null) {
            //todo 消息提醒
            throw new UnsupportedOperationException("not configured:File -> settings->tools->leetcode plugin");
        } else {
            return config;
        }

    }

    public String getTempFilePath() {
        return getConfig().getFilePath() + File.separator + PluginConstants.ACTION_PREFIX + File.separator;
    }


}
