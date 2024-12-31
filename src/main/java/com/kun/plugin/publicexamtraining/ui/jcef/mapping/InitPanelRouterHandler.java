package com.kun.plugin.publicexamtraining.ui.jcef.mapping;

import com.kun.plugin.publicexamtraining.data.DataService;
import com.kun.plugin.publicexamtraining.ui.jcef.RequestMapping;
import com.kun.plugin.publicexamtraining.util.LogUtils;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

@RequestMapping("processInit")
public class InitPanelRouterHandler extends CefMessageRouterHandlerAdapter {

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request,
                           boolean persistent, CefQueryCallback callback) {
        try {
            boolean initFlag = DataService.isInitFlag();
            if (initFlag) {
                callback.success("success");
                return true;
            }

            DataService.initPaper();
            DataService.setInitSuccess();
            LogUtils.LOG.info("试卷列表初始化成功");
            callback.success("success");
            return true;
        } catch (Exception e) {
            //log output
            callback.failure(500, "执行失败");
        }
        return false;
    }

    @Override
    public void onQueryCanceled(CefBrowser browser, CefFrame frame, long queryId) {
        super.onQueryCanceled(browser, frame, queryId);
    }


}
