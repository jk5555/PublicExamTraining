package com.kun.plugin.publicexamtraining.ui.jcef.mapping;

import com.google.gson.Gson;
import com.kun.plugin.publicexamtraining.data.DataService;
import com.kun.plugin.publicexamtraining.ui.jcef.RequestMapping;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

import java.util.List;

@RequestMapping("getAllProvince")
public class GetAllProvinceRouterHandler extends CefMessageRouterHandlerAdapter {

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request,
                           boolean persistent, CefQueryCallback callback) {
        try {
            List<String> allProvince = DataService.ALL_PROVINCE;
            String provinces = new Gson().toJson(allProvince);
            callback.success(provinces);
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
