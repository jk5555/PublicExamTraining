package com.kun.plugin.publicexamtraining.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tempTest {
    private static final String contentTypeJson = "application/json;charset=UTF-8";

    private static final HttpClient client = HttpClient.newHttpClient();
    public static String doGet(String url) {
        HttpResponse httpResponse = executeGet(HttpRequest.builderGet(url).cache(true).build());
        if (httpResponse.isSuccess()) {
            return httpResponse.getBody();
        }

        return StringUtils.EMPTY;
    }



    public static String doPost(String url, String body) {
        HttpResponse httpResponse = executePost(HttpRequest.builderPost(url, contentTypeJson).body(body).cache(true).build());
        if (httpResponse.isSuccess()) {
            return httpResponse.getBody();
        }

        return StringUtils.EMPTY;
    }


    public static String doPostNoBody(String url) {
        return doPost(url, StringUtils.EMPTY);
    }

    /**
     * 将 Map<String, String> 转换为 String[]，每个键和值依次排列。
     *
     * @param header 头部信息的 Map
     * @return 转换后的列表
     */
    private static String[] convertHeaderToArray(Map<String, String> header) {
        List<String> headerList = new ArrayList<>();
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                headerList.add(entry.getKey());
                headerList.add(entry.getValue());
            }
        } else {
            headerList.add("1");
            headerList.add("1");
        }
        return headerList.toArray(new String[0]);
    }


    @NotNull
    public static HttpResponse executeGet(HttpRequest request) {
        HttpResponse httpResponse = new HttpResponse();
        try {
            Map<String, String> header = request.getHeader();
            //把header中的键值对转成一个key，value排列存放的list集合
            java.net.http.HttpRequest httpRequestModel = java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(request.getUrl()))
                    .headers(convertHeaderToArray(header))
                    .header( "Content-Type", contentTypeJson)
                    .GET()
                    .build();

            java.net.http.HttpResponse<String> response = client.send(httpRequestModel, java.net.http.HttpResponse.BodyHandlers.ofString());
            httpResponse.setStatusCode(response.statusCode());
            httpResponse.setBody(response.body());
            httpResponse.setUrl(request.getUrl());
        } catch (Exception e) {
            httpResponse.setStatusCode(-1);
            httpResponse.setBody(StringUtils.EMPTY);
        }
        return httpResponse;
    }

    @NotNull
    public static HttpResponse executePost(HttpRequest request) {
        HttpResponse httpResponse = new HttpResponse();
        try {
            Map<String, String> header = request.getHeader();
            //把header中的键值对转成一个key，value排列存放的list集合
            java.net.http.HttpRequest httpRequestModel = java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(request.getUrl()))
                    .headers(convertHeaderToArray(header))
                    .header( "Content-Type", "application/json;charset=UTF-8")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(request.getBody()))
                    .build();

            java.net.http.HttpResponse<String> response = client.send(httpRequestModel, java.net.http.HttpResponse.BodyHandlers.ofString());
            httpResponse.setStatusCode(response.statusCode());
            httpResponse.setBody(response.body());
            httpResponse.setUrl(request.getUrl());
        } catch (Exception e) {
            httpResponse.setStatusCode(-1);
            httpResponse.setBody(StringUtils.EMPTY);
        }
        return httpResponse;
    }



    public static void main(String[] args) {
//        String s = doGet("https://www.gkzenti.cn/api/json?cls=行测&province=湖北");
//        System.err.println(s);
        String s1 = doGet("https://www.gkzenti.cn/paper/1720413487446");
        System.err.println(s1);
    }




}
