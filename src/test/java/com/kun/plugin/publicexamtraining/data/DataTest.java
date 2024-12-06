package com.kun.plugin.publicexamtraining.data;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import com.kun.plugin.publicexamtraining.data.dao.H2DbManager;
import com.kun.plugin.publicexamtraining.data.dao.entity.QuestionEntity;
import com.kun.plugin.publicexamtraining.data.model.Paper;
import com.kun.plugin.publicexamtraining.data.spider.OpenQuestionPipeline;
import com.kun.plugin.publicexamtraining.data.spider.OpenQuestionProcessor;
import com.kun.plugin.publicexamtraining.util.HttpRequestUtils;
import com.kun.plugin.publicexamtraining.util.QuestionTypeHelper;
import com.kun.plugin.publicexamtraining.util.TextMatcher;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                .addPipeline(new OpenQuestionPipeline())
                .setScheduler(new QueueScheduler())//去重
                .addUrl("https://www.gkzenti.cn/paper/1720413487446")
                .thread(2)
                .run();
    }

    @Test
    public void test41() {
        Spider.create(new OpenQuestionProcessor())
                .addPipeline(new FilePipeline("data/cache/"))
                .setScheduler(new QueueScheduler())//去重
                .addUrl("https://www.gkzenti.cn/paper/1720413487446")
                .thread(2)
                .run();
    }

    @Test
    public void test42() {
        //跳过验证页
        String body = HttpRequestUtils.doGet("https://www.gkzenti.cn/explain/1720413487446");
        Html html = new Html(body);
        String path = html.xpath("//form[@class='navbar-form']/*/img/@src").get();
        String svg = HttpRequestUtils.doGet("https://www.gkzenti.cn" + StringUtils.trim(path));
        System.out.println(svg);
        SVGUniverse universe = new SVGUniverse();
        SVGDiagram diagram = universe.loadSVG(new File("input.svg"));

        // 渲染SVG到BufferedImage
        SVGImagePanel panel = new SVGImagePanel();
        panel.setSVGDiagram(diagram);
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        panel.paint(image.getGraphics());

        // 保存BufferedImage为PNG
        ImageIO.write(image, "png", new File("output.png"));

    }

    @Test
    public void test5() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String text = "五、资料 分析。所给出的图、表、文字或综合性资料均有若干个问题要你回答。你应根据资料提供的信息进行分析、比较、计算和判断处理。";
        Set<String> words = Set.of("资料分析");
        double matchScore = TextMatcher.calculateMatchScore(text, words);
        System.out.println("匹配值: " + matchScore);
        stopWatch.stop();
        System.out.println(stopWatch.formatTime());
    }

    @Test
    public void test6() {
        String questionType = QuestionTypeHelper.getQuestionType("(一)小垃圾系统级量关系别");
        System.out.println("----------->" +questionType);
    }

    @Test
    public void test7() {
        Pattern pattern = Pattern.compile("^(\\d+)[、，.:：]?([A-Za-z])$");
        String text = "123：A";
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            // matcher.group(1) 将返回第一个捕获组的内容，即数字部分
            System.out.println("数字部分是: " + matcher.group(1));
            System.out.println("数字部分是: " + matcher.group(2));
        } else {
            System.out.println("字符串不匹配正则表达式");
        }
        System.out.println(matcher.find());
    }

    @Test
    public void test8() throws IOException {
        String body = HttpRequestUtils.doGet("https://www.gkzenti.cn/captcha/math");
        IOUtils.write(body, new FileOutputStream("data/cache/www.gkzenti.cn/math.svg"), StandardCharsets.UTF_8);
        System.out.println(body);
    }







}
