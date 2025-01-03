package com.kun.plugin.publicexamtraining.data.spider;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

public class SpiderHelper {


    public static void spiderPaper(String url) {
        Spider.create(new OpenQuestionProcessor())
                .addPipeline(new OpenQuestionPipeline())
                .setScheduler(new QueueScheduler())//去重
                .addUrl(url)
                .thread(2)
                .run();
    }


}
