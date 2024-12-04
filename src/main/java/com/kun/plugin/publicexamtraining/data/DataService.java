package com.kun.plugin.publicexamtraining.data;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kun.plugin.publicexamtraining.common.Constants;
import com.kun.plugin.publicexamtraining.data.dao.H2DbManager;
import com.kun.plugin.publicexamtraining.data.dao.entity.PaperEntity;
import com.kun.plugin.publicexamtraining.data.model.Paper;
import com.kun.plugin.publicexamtraining.util.HttpRequestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataService {

    // 公开题库网接口地址
    public static final String API_URL = "https://www.gkzenti.cn/api/json?cls=行测&province=%s";

    public static final Pattern pattern = Pattern.compile(Constants.OPEN_QUESTION_PAPER_PATTERN);

    public static final List<String> ALL_PROVINCE = Arrays.asList("北京", "天津", "上海", "重庆", "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "内蒙古", "广西", "西藏", "宁夏", "新疆");


    public static List<Paper> getPapers(String province) {
        if (StringUtils.isBlank(province)) {
            return Collections.emptyList();
        }
        String response = HttpRequestUtils.doGet(String.format(API_URL, province));
        if (StringUtils.isNotBlank(response)) {
            return JSON.parseArray(response, Paper.class);
        }
        return Collections.emptyList();
    }

    public static int savePapers(List<Paper> papers, String province) {
        if (papers == null || papers.isEmpty()) {
            return 0;
        }

        List<PaperEntity> paperEntityList = Lists.transform(papers, paper -> {
            Matcher matcher = pattern.matcher(paper.getJumpUrl());
            PaperEntity paperEntity = new PaperEntity();
            paperEntity.setSourceId(matcher.find() ? matcher.group(1) : null);
            paperEntity.setSourceUrl(paper.getJumpUrl());
            paperEntity.setTitle(paper.getTitle());
            paperEntity.setSource(paper.getSource());
            paperEntity.setProvince(province);
            return paperEntity;
        });

        return H2DbManager.insertPaperBatch(paperEntityList);


    }





}
