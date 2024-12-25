package com.kun.plugin.publicexamtraining.ui.jcef;


import org.cef.callback.CefCallback;
import org.cef.handler.CefLoadHandler;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefResponse;

import java.io.InputStream;
import java.net.URLConnection;

public class WebviewOpenedConnection {
    private final URLConnection connection;

    private InputStream inputStream;

    public WebviewOpenedConnection(URLConnection connection) {
        this.connection = connection;
        try {
            this.inputStream = connection.getInputStream();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl) {
        try {
            String url = this.connection.getURL().toString();
            cefResponse.setMimeType(this.connection.getContentType());
            try {
                responseLength.set(this.inputStream.available());
                cefResponse.setStatus(200);
            } catch (Exception e) {
                cefResponse.setError(CefLoadHandler.ErrorCode.ERR_FILE_NOT_FOUND);
                cefResponse.setStatusText(e.getLocalizedMessage());
                cefResponse.setStatus(404);
            }
        } catch (Exception e) {
            cefResponse.setError(CefLoadHandler.ErrorCode.ERR_FILE_NOT_FOUND);
            cefResponse.setStatusText(e.getLocalizedMessage());
            cefResponse.setStatus(404);
        }
    }

    public boolean readResponse(byte[] dataOut, int designedBytesToRead, IntRef bytesRead, CefCallback callback) {
        try {
            int availableSize = this.inputStream.available();
            if (availableSize > 0) {
                int maxBytesToRead = Math.min(availableSize, designedBytesToRead);
                int realNumberOfReadBytes = this.inputStream.read(dataOut, 0, maxBytesToRead);
                bytesRead.set(realNumberOfReadBytes);
                return true;
            }
        } catch (Exception exception) {
            //log output
        } finally {
            this.close();
        }
        return false;
    }

    public void close() {
        try {
            if (this.inputStream != null)
                this.inputStream.close();
        } catch (Exception exception) {
            //log output
        }
    }
}
