package com.kun.plugin.publicexamtraining.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResourceUtils {


    public static String getResourceAsString(String path) {
        try {
            return IOUtils.toString(Objects.requireNonNull(ResourceUtils.class.getResourceAsStream(path)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }


    public static String loadHtml(String path) {
        try {
            return IOUtils.toString(Objects.requireNonNull(ResourceUtils.class.getResourceAsStream(path)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "<html><body><h1>页面加载失败</h1></body></html>";
        }
    }


}
