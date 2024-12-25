package com.kun.plugin.publicexamtraining.ui.jcef;

import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.kun.plugin.publicexamtraining.util.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandler;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;

import java.net.URL;

/**
 * 继承 CefResourceHandler 接口，自定义处理 Chromium 浏览器加载的资源（如网页、图像、样式表等）。
 * 通过实现该接口，可以覆盖默认的资源加载行为，并提供自定义的资源加载逻辑。
 */
public class DataResourceHandler implements CefResourceHandler {

    private static final String INNER_SCHEME = "http://inner/";
    private WebviewOpenedConnection state;

    /**
     * 用于处理资源请求，你可以通过该方法获取请求的 URL、请求头部信息，并返回相应的响应结果。
     */
    @Override
    public boolean processRequest(CefRequest cefRequest, CefCallback cefCallback) {
        String url = cefRequest.getURL();
        //判断请求是否是用于获取内部静态资源的，如果是则拦截请求，并从项目里对应配置获取对应文件返回
        //如果是请求外部资源，则跳过
        if (StringUtils.isNotBlank(url) && url.startsWith(INNER_SCHEME)) {
            String pathToResource = url.replace(INNER_SCHEME, "/");
            pathToResource = pathToResource.split("\\?")[0];
            URL resourceUrl = getClass().getResource(pathToResource);
            if (resourceUrl != null) {
                try {
                    VirtualFile f = VfsUtil.findFileByURL(resourceUrl);
                    resourceUrl = VfsUtil.convertToURL(f.getUrl());
                    this.state = new WebviewOpenedConnection(resourceUrl.openConnection());
                } catch (Exception exception) {
                    LogUtils.LOG.error("获取资源失败", exception);
                }
            }
            cefCallback.Continue();
            return true;
        }
        return false;
    }


    /**
     * 用于设置资源响应的头部信息，例如 Content-Type、Cache-Control 等。
     */
    @Override
    public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl) {
        this.state.getResponseHeaders(cefResponse, responseLength, redirectUrl);
    }

    /**
     * 用于读取资源的内容，可以从这个方法中读取资源的数据并将其传递给浏览器
     */
    @Override
    public boolean readResponse(byte[] dataOut, int designedBytesToRead, IntRef bytesRead, CefCallback callback) {
        return this.state.readResponse(dataOut, designedBytesToRead, bytesRead, callback);
    }

    /**
     * 请求取消
     */
    @Override
    public void cancel() {
        this.state.close();
    }

}
