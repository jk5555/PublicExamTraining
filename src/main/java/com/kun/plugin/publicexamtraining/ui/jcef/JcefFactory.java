package com.kun.plugin.publicexamtraining.ui.jcef;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefClient;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.kun.plugin.publicexamtraining.util.PackageUtils;
import org.apache.commons.lang3.StringUtils;
import org.cef.CefApp;
import org.cef.browser.CefMessageRouter;
import org.cef.handler.CefMessageRouterHandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JcefFactory {

    // 映射处理器包路径
    public static final String mappingHandlerPackage = "com.kun.plugin.publicexamtraining.ui.jcef.mapping";
    private static final Map<String, JCEFHtmlPanel> cache = new ConcurrentHashMap<>();
    private static final JBCefClient client;

    static {
        client = JBCefApp.getInstance().createClient();
        List<? extends CefMessageRouterHandlerAdapter> allMappingHandlerList = getAllMappingHandler();
        for (CefMessageRouterHandlerAdapter handler : allMappingHandlerList) {
            Pair<String, String> mappingUrl = getMappingUrl(handler.getClass());
            if (mappingUrl != null) {
                CefMessageRouter.CefMessageRouterConfig routerConfig = new CefMessageRouter.CefMessageRouterConfig(mappingUrl.getFirst(), mappingUrl.getSecond());
                CefMessageRouter messageRouter = CefMessageRouter.create(routerConfig, handler);
                client.getCefClient().addMessageRouter(messageRouter);
            }
        }

        //用于处理以http://inner/开头的请求。 用于拦截特定请求，转发请求到本地以获取本地资源
        CefApp.getInstance().registerSchemeHandlerFactory("http", "inner", new DataSchemeHandlerFactory());

    }

    private static List<CefMessageRouterHandlerAdapter> getAllMappingHandler() {
        Set<Class<?>> classes = PackageUtils.scanClasses(mappingHandlerPackage);
        ArrayList<CefMessageRouterHandlerAdapter> result = new ArrayList<>();
        for (Class<?> clazz : classes) {
            if (CefMessageRouterHandlerAdapter.class.isAssignableFrom(clazz)) {
                try {
                    result.add((CefMessageRouterHandlerAdapter) clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;

    }


    public static Pair<String, String> getMappingUrl(Class<? extends CefMessageRouterHandlerAdapter> clazz) {
        RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
        if (annotation != null && StringUtils.isNotBlank(annotation.value())) {
            return Pair.create(annotation.value(), annotation.value() + "Cancel");
        }
        return null;

    }


    public static JCEFHtmlPanel getBrowser(Project project, String tag) {
        String key = project.getName() + tag;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        JCEFHtmlPanel htmlPanel = new JCEFHtmlPanel(client, "");
        cache.put(key, htmlPanel);
        return htmlPanel;
    }

    /**
     * 获取ide夜间/白天主题样式
     *
     * @return dark/light
     */
    public static String getGlobalStyle() {
        if (EditorColorsManager.getInstance().isDarkEditor())
            return "dark";
        return "light";
    }


}
