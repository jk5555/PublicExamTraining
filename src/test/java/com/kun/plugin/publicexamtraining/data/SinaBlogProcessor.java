package com.kun.plugin.publicexamtraining.data;


import org.apache.commons.io.FileUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SinaBlogProcessor implements PageProcessor {

    public static final String URL_LIST = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html";

    public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";

    private final Site site = Site
            .me()
            .setDomain("blog.sina.com.cn")
            .setSleepTime(3000)
            .setRetryTimes(3)
            .setRetrySleepTime(1000)
            .setTimeOut(10000)
            .setCharset("UTF-8")
            .setUseGzip(true)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        //列表页
//        if (page.getUrl().regex(URL_LIST).match()) {
//            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"articleList\"]").links().regex(URL_POST).all());
//            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
//            //文章页
//        } else {
//            page.putField("title", page.getHtml().xpath("//div[@class='articalTitle']/h2"));
//            page.putField("content", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']"));
//            page.putField("date",
//                    page.getHtml().xpath("//div[@id='articlebody']//span[@class='time SG_txtc']").regex("\\((.*)\\)"));
//        }
        List<String> all = page.getHtml().smartContent().all();
        all.forEach(System.out::println);
        System.out.println("=======================================================================================================");
        page.putField("url", page.getUrl());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new SinaBlogProcessor())
                .addPipeline(new ConsolePipeline())
                .addUrl("https://www.gkzenti.cn/paper/1720413487446")
                .thread(5)
                .run();
    }
}
