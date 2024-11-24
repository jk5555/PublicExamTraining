package com.kun.plugin.publicexamtraining.util;






public class HttpResponse {

    private int statusCode;

    private String body;

    private String url;

    //生成一个判断http请求是否成功的函数
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
