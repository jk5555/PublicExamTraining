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
import com.kun.plugin.publicexamtraining.data.DataService;

import javax.swing.*;
import java.awt.*;

public class ToolMainPanel extends SimpleToolWindowPanel implements Disposable {

    private final JBTabs tabs;
    private final ToolWindow toolWindow;
    private final Project project;
    private JDialog loadingDialog;

    public ToolMainPanel(ToolWindow toolWindow, Project project) {
        super(Boolean.TRUE, Boolean.TRUE);
        this.toolWindow = toolWindow;
        this.project = project;
        setLayout(new BorderLayout());

        // 创建JBTabs实例
        tabs = new JBTabsImpl(project);

        // 初始化标签页
        initTabs();

        // 将tabs添加到主面板
        setContent(tabs.getComponent());
        // 初始化加载动画
        initLoadingDialog();
    }


    private void initLoadingDialog() {
        loadingDialog = new JDialog();
        loadingDialog.setUndecorated(true);
        loadingDialog.setSize(200, 100);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.add(new JLabel("加载中...", SwingConstants.CENTER));
    }

    private void showLoadingDialog() {
        SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
    }

    private void hideLoadingDialog() {
        SwingUtilities.invokeLater(() -> loadingDialog.setVisible(false));
    }

    private void initTabs() {
        tabs.removeAllTabs();

        // 创建第一个标签页
        TabInfo systemTab = new TabInfo(new SystemTrainingPanel(toolWindow, project).getComponent());
        systemTab.setText("系统训练");
        // 创建第二个标签页
        JBPanel tab2Content = new JBPanel(new BorderLayout());
        tab2Content.add(new JBLabel("这是第二个标签页的内容:"+project.getName()), BorderLayout.CENTER);
        TabInfo randomTab = new TabInfo(tab2Content);
        randomTab.setText("标签2");

        TabInfo initPanelTab = new TabInfo(new InitPanel(toolWindow, project).getComponent());
        initPanelTab.setText("初始化");

        tabs.addTab(systemTab);
        tabs.addTab(randomTab);
        tabs.addTab(initPanelTab);

        processInitTabHidden();
    }

    private void processInitTabHidden() {
        if (DataService.isInitFlag()) {
            tabs.getTabAt(0).setHidden(false);
            tabs.getTabAt(1).setHidden(false);
            tabs.getTabAt(2).setHidden(true);
            selectTab(0);
        } else {
            tabs.getTabAt(0).setHidden(true);
            tabs.getTabAt(1).setHidden(true);
            tabs.getTabAt(2).setHidden(false);
            selectTab(2);
        }
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

    // 刷新面板
    public void refresh() {
        showLoadingDialog();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initTabs();
                hideLoadingDialog();
            }
        });
    }

    /**
     * Usually not invoked directly, see class javadoc.
     */
    @Override
    public void dispose() {
        Disposer.dispose(this);
    }
}

