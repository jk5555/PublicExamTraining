package com.kun.plugin.publicexamtraining.ui.jcef.mapping;

import com.kun.plugin.publicexamtraining.ui.jcef.RequestMapping;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

@RequestMapping("")
public class SystemTrainingTabInitRouterHandler extends CefMessageRouterHandlerAdapter {
    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request,
                           boolean persistent, CefQueryCallback callback) {
        try {
            System.out.println(request);
            callback.success("执行成功");
            return true;
        } catch (Exception e) {
            //log output
        }
        return false;
    }

    @Override
    public void onQueryCanceled(CefBrowser browser, CefFrame frame, long queryId) {
        super.onQueryCanceled(browser, frame, queryId);
    }



}
