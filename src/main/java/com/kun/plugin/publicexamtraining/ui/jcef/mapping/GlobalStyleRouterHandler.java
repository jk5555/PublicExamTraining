package com.kun.plugin.publicexamtraining.ui.jcef.mapping;

import com.google.gson.Gson;
import com.kun.plugin.publicexamtraining.common.PluginConstants;
import com.kun.plugin.publicexamtraining.ui.jcef.JcefFactory;
import com.kun.plugin.publicexamtraining.ui.jcef.RequestMapping;
import com.kun.plugin.publicexamtraining.util.LogUtils;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@RequestMapping("queryGlobalStyle")
public class GlobalStyleRouterHandler extends CefMessageRouterHandlerAdapter {
    private static @NotNull HashMap<String, String> getStyleResult() {
        HashMap<String, String> result = new HashMap<>();
        result.put("globalColorStyle", JcefFactory.getGlobalStyle());
        result.put("backgroundColor", "#" + Integer.toHexString(PluginConstants.bg.getRGB()));
        result.put("fontSize", String.valueOf(PluginConstants.font.getSize()));
        result.put("labelFontColor", "#" + Integer.toHexString(PluginConstants.labelFontColor.getRGB()));
        result.put("buttonBgColor", "#" + Integer.toHexString(PluginConstants.buttonBg.getRGB()));
        result.put("borderColor", "#" + Integer.toHexString(PluginConstants.border.getRGB()));
        return result;
    }

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request,
                           boolean persistent, CefQueryCallback callback) {
        try {
            LogUtils.LOG.info("queryGlobalStyle: " + request);
            HashMap<String, String> result = getStyleResult();
            callback.success(new Gson().toJson(result));
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
