package com.kun.plugin.publicexamtraining.data.spider;

import org.apache.commons.lang3.RandomUtils;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.HashSet;
import java.util.Set;

/**
 * 公开题库网爬虫处理器
 */
public class OpenQuestionProcessor implements PageProcessor {


//    public static final String PAPER = Constants.OPEN_QUESTION_PAPER_PATTERN;
//
//    public static final String ANSWER = "/answer/\\d+";
//
//    public static final String EXPLAIN = "/explain/\\d+";

    public static final String URL_POST = "https://www\\.gkzenti\\.cn/(paper|answer|explain)/\\d+";

    private final Set<String> visitedUrls = new HashSet<>();


    private final Site site = Site
            .me()
            .setDomain("www.gkzenti.cn")
            .setSleepTime(RandomUtils.nextInt(2000, 4000))
            .setRetryTimes(3)
            .setRetrySleepTime(RandomUtils.nextInt(800, 1200))
            .setTimeOut(10000)
            .setCharset("UTF-8")
            .setUseGzip(true)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
//        if (visitedUrls.contains(page.getUrl().toString())) {
//            return;
//        }
//        visitedUrls.add(page.getUrl().toString());
        page.addTargetRequests(page.getHtml().xpath("//ol[@class=\"breadcrumb\"]").links().regex(URL_POST).all());


        page.putField("title", page.getHtml().xpath("/head/title/text()").get());
//        page.putField("content", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']"));
//        page.putField("date",
//                page.getHtml().xpath("//div[@id='articlebody']//span[@class='time SG_txtc']").regex("\\((.*)\\)"));
        //处理试卷

        //处理答案

        //处理解析


    }

    @Override
    public Site getSite() {
        return site;
    }


}

