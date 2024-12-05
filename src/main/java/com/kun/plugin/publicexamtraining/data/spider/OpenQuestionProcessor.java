package com.kun.plugin.publicexamtraining.data.spider;

import com.kun.plugin.publicexamtraining.common.Constants;
import com.kun.plugin.publicexamtraining.data.model.Question;
import com.kun.plugin.publicexamtraining.util.QuestionTypeHelper;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 公开题库网爬虫处理器
 */
public class OpenQuestionProcessor implements PageProcessor {

    private static final Pattern PATTERN = Pattern.compile(Constants.OPEN_SITE_URL_POST);
    private static final Pattern QUESTION = Pattern.compile(Constants.OPEN_QUESTION_PAPER_PATTERN);
    private static final Pattern ANSWER = Pattern.compile(Constants.OPEN_ANSWER_PAPER_PATTERN);
    private static final Pattern EXPLAIN = Pattern.compile(Constants.OPEN_EXPLAIN_PAPER_PATTERN);
    private final Site site = Site
            .me()
            .setDomain("www.gkzenti.cn")
            .setSleepTime(RandomUtils.nextInt(500, 1200))
            .setRetryTimes(3)
            .setRetrySleepTime(RandomUtils.nextInt(800, 1200))
            .setTimeOut(10000)
            .setCharset("UTF-8")
            .setUseGzip(true)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
    private Integer paperId;

    public OpenQuestionProcessor() {
    }

    public OpenQuestionProcessor(Integer paperId) {
        this.paperId = paperId;
    }

    @Override
    public void process(Page page) {
        if (!PATTERN.matcher(page.getUrl().get()).matches()) {
            return;
        }
        List<String> links = page.getHtml().xpath("//ol[@class=\"breadcrumb\"]").links().all();
        links.stream().filter(url -> PATTERN.matcher(url).matches()).forEach(page::addTargetRequest);


        //处理试卷
        if (QUESTION.matcher(page.getUrl().get()).matches()) {
            this.processQuestion(page);
        }

        //处理答案
        if (ANSWER.matcher(page.getUrl().get()).matches()) {
            this.processAnswer(page);
        }

        //处理解析
        if (EXPLAIN.matcher(page.getUrl().get()).matches()) {
            this.processExplain(page);
        }


    }


    private void processQuestion(Page page) {
        List<Question> list = new ArrayList<>();

        //标题，也是来源source
        String title = page.getHtml().xpath("/html/head/title/text()").get();
        List<String> allQuestionDiv = page.getHtml().xpath("//div[@id=\"printcontent\"]/div/").all();
        String questionType = "";
        for (String div : allQuestionDiv) {
            Html html = new Html(div);
            //获取题目类型
            String questionTypeText = html.xpath("//div[@class='row']/div[@class='subtitle']/text()").get();
            if (StringUtils.isNotBlank(questionTypeText)) {
                questionType = QuestionTypeHelper.getQuestionType(StringUtils.trim(questionTypeText));
                continue;
            }
            //替换图片url
            html.xpath("//img/@src").all().forEach(url -> {
                String newUrl = "";
                if (StringUtils.startsWith(url, "//")) {
                    newUrl = "https:" + url;
                }
                html.regex(url).replace(url, newUrl);
            });
            Question question = new Question();
            question.setPaperId(paperId);
            question.setSource(title);
            question.setQuestionType(questionType);
            //题目
            String number = html.xpath("//div[@class='row']/div[@class='left']/text()").get();
            question.setNumber(StringUtils.trim(number));
            String questionStem = html.xpath("//div[@class='row']/div[@class='right']/p/text()").get();
            question.setQuestionStem(StringUtils.trim(questionStem));
            //选项
            List<String> options = html.xpath("//div[@class='row']/div[@class='right']/div/text()").all();
            options = options.stream().map(String::trim).collect(Collectors.toList());
            question.setAnswerSelect(String.join(Constants.OPTIONS_SEPARATOR, options));

            list.add(question);

        }

        page.putField(Constants.QUESTION_PARAM, list);
    }

    private void processAnswer(Page page) {

    }

    private void processExplain(Page page) {
        page.putField("title", page.getHtml().xpath("/head/title/text()").get());
    }


    @Override
    public Site getSite() {
        return site;
    }


}

