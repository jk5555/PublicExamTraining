package com.kun.plugin.publicexamtraining.data;

import com.kun.plugin.publicexamtraining.data.dao.H2DbManager;
import com.kun.plugin.publicexamtraining.data.dao.entity.QuestionEntity;
import com.kun.plugin.publicexamtraining.data.model.Paper;
import com.kun.plugin.publicexamtraining.data.spider.OpenQuestionProcessor;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.List;

public class DataTest {

    @Test
    public void test() {
        System.out.println(H2DbManager.getDbUrl());
        ArrayList<QuestionEntity> list = new ArrayList<>();
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setSource("2");
        questionEntity.setPaperId(1);
        questionEntity.setQuestionType("2");
        questionEntity.setQuestionStem("2");
        questionEntity.setAnswerSelect("2");
        questionEntity.setAnswer("2");
        questionEntity.setAnswerAnalysis("2");
        list.add(questionEntity);

        QuestionEntity questionEntity1 = new QuestionEntity();
        questionEntity1.setSource("3");
        questionEntity1.setPaperId(1);
        questionEntity1.setQuestionType("3");
        questionEntity1.setQuestionStem("3");
        questionEntity1.setAnswerSelect("3");
        questionEntity1.setAnswer("3");
        questionEntity1.setAnswerAnalysis("3");
        list.add(questionEntity1);


        System.out.println(H2DbManager.insertQuestionBatch(list));
    }

    @Test
    public void test2() {
        List<Paper> papers = DataService.getPapers("山东");
        System.out.println(papers.size());
    }

    @Test
    public void test3() {
        DataService.ALL_PROVINCE.forEach(province -> {
            StopWatch stopWatch = new StopWatch(province);
            stopWatch.start();
            List<Paper> papers = DataService.getPapers(province);
            stopWatch.split();
            int saved = DataService.savePapers(papers, province);
            stopWatch.stop();
            System.out.println(stopWatch.formatTime());
            System.out.println(province + ": " + saved);
        });
    }


    @Test
    public void test4() {
        Spider.create(new OpenQuestionProcessor())
                .addPipeline(new ConsolePipeline())
                .setScheduler(new QueueScheduler())//去重
                .addUrl("https://www.gkzenti.cn/paper/1720413487446")
                .thread(2)
                .run();
    }



}
