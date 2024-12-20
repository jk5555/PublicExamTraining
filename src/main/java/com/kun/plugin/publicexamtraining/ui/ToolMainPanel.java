package com.kun.plugin.publicexamtraining.ui;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;

import java.awt.*;

public class ToolMainPanel extends SimpleToolWindowPanel implements Disposable {

    private final JBTabs tabs;

    public ToolMainPanel(ToolWindow toolWindow, Project project) {
        super(Boolean.TRUE, Boolean.TRUE);
        setLayout(new BorderLayout());

        // 创建JBTabs实例
        tabs = new JBTabsImpl(project);

        // 创建第一个标签页
        RandomTrainHtmlPanel randomTrainHtmlPanel = new RandomTrainHtmlPanel("<html><b>这是粗体文本</b><br><i>这是斜体文本</i></html>");
        JBPanel tab1Content = new JBPanel(new BorderLayout());
        tab1Content.add(randomTrainHtmlPanel, BorderLayout.CENTER);
        TabInfo tab1 = new TabInfo(tab1Content);
        tab1.setText("专项训练");
        tabs.addTab(tab1);

        // 创建第二个标签页
        JBPanel tab2Content = new JBPanel(new BorderLayout());
        tab2Content.add(new JBLabel("这是第二个标签页的内容"), BorderLayout.CENTER);
        TabInfo tab2 = new TabInfo(tab2Content);
        tab2.setText("标签2");
        tabs.addTab(tab2);

        // 将tabs添加到主面板
        setContent(tabs.getComponent());
    }

    // 获取当前选中的标签页
    public TabInfo getSelectedTab() {
        return tabs.getSelectedInfo();
    }

    // 切换到指定的标签页
    public void selectTab(int index) {
        if (index >= 0 && index < tabs.getTabCount()) {
            tabs.select(tabs.getTabAt(index), true);
        }
    }

    /**
     * Usually not invoked directly, see class javadoc.
     */
    @Override
    public void dispose() {
        Disposer.dispose(this);
    }
}

