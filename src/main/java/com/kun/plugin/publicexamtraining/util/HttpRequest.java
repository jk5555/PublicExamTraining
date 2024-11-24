package com.kun.plugin.publicexamtraining.util;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;




public class HttpRequest {

    private final String url;

    private final String body;
    /**
     * POST
     */
    private final String contentType;

    private final Map<String, String> header;

    private final boolean cache;

    private final String cacheParam;

    private HttpRequest(String url, String body, String contentType, Map<String, String> header, boolean cache, String cacheParam) {
        this.url = url;
        this.body = body;
        this.contentType = contentType;
        this.header = header;
        this.cache = cache;
        this.cacheParam = cacheParam;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public boolean isCache() {
        return cache;
    }

    public String getCacheParam() {
        return cacheParam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HttpRequest that = (HttpRequest) o;

        return new EqualsBuilder().append(url, that.url).append(body, that.body).append(contentType, that.contentType).append(header, that.header).append(cacheParam, that.cacheParam).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(url).append(body).append(contentType).append(header).append(cacheParam).toHashCode();
    }

    public static HttpRequestBuilder builderGet(String url) {
        return new HttpRequestBuilder().get(url);
    }

    public static HttpRequestBuilder builderPost(String url, String contentType) {
        return new HttpRequestBuilder().post(url, contentType);
    }

    public static HttpRequestBuilder builderPut(String url, String contentType) {
        return new HttpRequestBuilder().put(url, contentType);
    }

    public static class HttpRequestBuilder {
        private String url;

        private String body;
        /**
         * POST
         */
        private String contentType;

        private Type type;

        private final Map<String, String> header = new HashMap<>();

        private boolean cache = false;

        private String cacheParam;

        private HttpRequestBuilder() {
        }

        private HttpRequestBuilder get(String url) {
            this.url = url;
            this.type = Type.GET;
            return this;
        }

        private HttpRequestBuilder post(String url, String contentType) {
            this.url = url;
            this.contentType = contentType;
            this.type = Type.POST;
            return this;
        }

        private HttpRequestBuilder put(String url, String contentType) {
            this.url = url;
            this.contentType = contentType;
            this.type = Type.PUT;
            return this;
        }

        public HttpRequestBuilder body(String body) {
            this.body = body;
            return this;
        }

        public HttpRequestBuilder addHeader(String name, String value) {
            this.header.put(name, value);
            return this;
        }

        public HttpRequestBuilder addHeader(Map<String, String> header) {
            this.header.putAll(header);
            return this;
        }

        public HttpRequestBuilder cache(boolean cache) {
            this.cache = cache;
            return this;
        }

        public HttpRequestBuilder cacheParam(String cacheParam) {
            this.cacheParam = cacheParam;
            this.cache = true;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(url, body, contentType, header, cache, cacheParam);
        }

        @NotNull
        public HttpResponse request() {
            HttpRequest httpRequest = build();
            switch (type) {
                case GET:
                    return HttpRequestUtils.executeGet(httpRequest);
                case POST:
                    return HttpRequestUtils.executePost(httpRequest);
                default:
                    throw new RuntimeException("Type not supported");
            }

        }

    }

    private enum Type {
        GET, POST, PUT
    }
}
