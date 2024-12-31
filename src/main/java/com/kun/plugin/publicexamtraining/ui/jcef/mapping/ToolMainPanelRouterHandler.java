package com.kun.plugin.publicexamtraining.ui.jcef.mapping;

import com.kun.plugin.publicexamtraining.ui.ToolMainPanel;
import com.kun.plugin.publicexamtraining.ui.WindowFactory;
import com.kun.plugin.publicexamtraining.ui.jcef.RequestMapping;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

import javax.swing.*;

@RequestMapping("refreshToolMainPanel")
public class ToolMainPanelRouterHandler  extends CefMessageRouterHandlerAdapter {

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request,
                           boolean persistent, CefQueryCallback callback) {
        try {
            ToolMainPanel toolMainPanel = WindowFactory.getToolMainPanel(request);
            if (toolMainPanel != null) {
                SwingUtilities.invokeLater(toolMainPanel::refresh);
            }

            callback.success("success");
            return true;
        } catch (Exception e) {
            //log output
            callback.failure(500, "执行失败");
        }
        return false;
    }




}
