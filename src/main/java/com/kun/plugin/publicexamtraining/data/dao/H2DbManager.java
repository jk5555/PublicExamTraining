package com.kun.plugin.publicexamtraining.data.dao;

import com.kun.plugin.publicexamtraining.common.DataConstants;
import com.kun.plugin.publicexamtraining.data.dao.entity.PaperEntity;
import com.kun.plugin.publicexamtraining.data.dao.entity.QuestionEntity;
import com.kun.plugin.publicexamtraining.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.List;

public class H2DbManager {


    private static final String DB_URL = String.format("jdbc:h2:file:%s/db/publicexam;MODE=MySQL;DB_CLOSE_ON_EXIT=TRUE;AUTO_SERVER=TRUE;CACHE_SIZE=2048;", DataConstants.CACHE_HOME);

    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "sa";
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_NAME = "publicexam";
    private static final String DB_TABLE_SQL = "CREATE TABLE IF NOT EXISTS paper\n" +
            "(\n" +
            "    id          INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "    source_id   varchar(64) comment '来源唯一标识，指的是题源的主键',\n" +
            "    source_url  varchar(256) comment '题源的url',\n" +
            "    title       varchar(256) comment '试卷标题',\n" +
            "    source      varchar(256) comment '试卷来源',\n" +
            "    province    varchar(64) comment '省份',\n" +
            "    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
            "    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS question\n" +
            "(\n" +
            "    id              INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "    paper_id        INT comment '试卷ID',\n" +
            "    source          varchar(64) comment '题源（试卷标题）',\n" +
            "    question_type   varchar(64) comment '问题类型',\n" +
            "    question_stem   text comment '问题题干',\n" +
            "    answer_select   text comment '答案选项，选项之间用|*|分隔',\n" +
            "    answer          varchar(64) comment '正确答案',\n" +
            "    answer_analysis text comment '答案解析',\n" +
            "    create_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
            "    update_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP\n" +
            ");\n";

    public static void init() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            String[] split = StringUtils.split(DB_TABLE_SQL, ";");
            for (String sql : split) {
                if (StringUtils.isNotBlank(sql)) {
                    statement.addBatch(sql);
                }
            }
            statement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(statement, connection);
        }
    }

    public static String getDbUrl() {
        return DB_URL;
    }

    private static void close(Statement statement, Connection connection) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static {
        init();
    }


    public static int insertQuestionBatch(List<QuestionEntity> questionEntityList) {
        if (questionEntityList == null || questionEntityList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.prepareStatement("insert into QUESTION (PAPER_ID, SOURCE, QUESTION_TYPE, QUESTION_STEM, ANSWER_SELECT, ANSWER, ANSWER_ANALYSIS)\n" +
                    "values (?,?,?,?,?,?,?);");
            int count = 0;
            for (QuestionEntity question : questionEntityList) {
                count++;
                if (StringUtils.isNotBlank(question.getQuestionStem()) && StringUtils.isNotBlank(question.getAnswerSelect())) {
                    statement.setInt(1, CommonUtils.ifNull(question.getPaperId(), 0));
                    statement.setString(2, question.getSource());
                    statement.setString(3, question.getQuestionType());
                    statement.setString(4, question.getQuestionStem());
                    statement.setString(5, question.getAnswerSelect());
                    statement.setString(6, question.getAnswer());
                    statement.setString(7, question.getAnswerAnalysis());
                    statement.addBatch();
                }
                if (count % 100 == 0) {
                    int[] res = statement.executeBatch();
                    successCount += sumSuccessResult(res);
                }
            }
            int[] res1 = statement.executeBatch();
            successCount += sumSuccessResult(res1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(statement, connection);
        }

        return successCount;
    }

    private static int sumSuccessResult(int[] res) {
        int successCount = 0;
        if (res == null) {
            return 0;
        }
        for (int re : res) {
            if (re > 0) {
                successCount++;
            }
        }
        return successCount;
    }

    public static int insertPaperBatch(List<PaperEntity> paperList) {
        if (paperList == null || paperList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.prepareStatement("insert into PAPER (SOURCE_ID, SOURCE_URL, TITLE, SOURCE, PROVINCE) values (?,?,?,?,?)");
            int count = 0;
            for (PaperEntity paper : paperList) {
                count++;
                if (StringUtils.isNotBlank(paper.getTitle()) && StringUtils.isNotBlank(paper.getSourceUrl())) {
                    statement.setString(1, paper.getSourceId());
                    statement.setString(2, paper.getSourceUrl());
                    statement.setString(3, paper.getTitle());
                    statement.setString(4, paper.getSource());
                    statement.setString(5, paper.getProvince());
                    statement.addBatch();
                }
                if (count % 100 == 0) {
                    int[] res = statement.executeBatch();
                    successCount += sumSuccessResult(res);
                }
            }
            int[] res1 = statement.executeBatch();
            successCount += sumSuccessResult(res1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(statement, connection);
        }

        return successCount;
    }


}
