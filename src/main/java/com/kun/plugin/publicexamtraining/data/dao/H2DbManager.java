package com.kun.plugin.publicexamtraining.data.dao;

import com.kun.plugin.publicexamtraining.common.DataConstants;
import com.kun.plugin.publicexamtraining.data.dao.entity.PaperEntity;
import com.kun.plugin.publicexamtraining.data.dao.entity.QuestionEntity;
import com.kun.plugin.publicexamtraining.util.CommonUtils;
import com.kun.plugin.publicexamtraining.util.LogUtils;
import com.kun.plugin.publicexamtraining.util.ResourceUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2DbManager {


    private static final String DB_URL = String.format("jdbc:h2:file:%s/db/publicexam;MODE=MySQL;DB_CLOSE_ON_EXIT=TRUE;AUTO_SERVER=TRUE;CACHE_SIZE=2048;", DataConstants.CACHE_HOME);

    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "sa";
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_NAME = "publicexam";
    private static final String DB_TABLE_SQL = ResourceUtils.getResourceAsString("/db/table.sql");


    static {
        init();
    }

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
            LogUtils.LOG.error(e);
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
            LogUtils.LOG.error(e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LogUtils.LOG.error(e);
        }
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
            LogUtils.LOG.error(e);
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
            statement = connection.prepareStatement("INSERT INTO PAPER (SOURCE_ID, SOURCE_URL, TITLE, SOURCE, PROVINCE) values (?,?,?,?,?) ON DUPLICATE KEY UPDATE SOURCE_ID = SOURCE_ID;");
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
            LogUtils.LOG.error(e);
            throw new RuntimeException(e);
        } finally {
            close(statement, connection);
        }

        return successCount;
    }


    public static int selectInitFlag() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select init_status from init_flag where id = 1;");
            if (resultSet.next()) {
                return resultSet.getInt("init_status");
            }
        } catch (Exception e) {
            LogUtils.LOG.error(e);
        } finally {
            close(statement, connection);
        }
        return 0;
    }

    public static void updateInitFlag() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            statement.executeUpdate("update init_flag set init_status = 1 where id = 1;");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(statement, connection);
        }
    }

    public static List<PaperEntity> queryPapers(String province) {
        List<PaperEntity> paperEntityList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.prepareStatement("select ID,SOURCE_ID,SOURCE_URL, TITLE,SOURCE,PROVINCE,CREATE_TIME,UPDATE_TIME from PAPER where PROVINCE= ? order by ID;");
            statement.setString(1, province);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PaperEntity paperEntity = setPaper(resultSet);
                paperEntityList.add(paperEntity);
            }

        } catch (Exception e) {
            LogUtils.LOG.error(e);
        } finally {
            close(statement, connection);
        }
        return paperEntityList;
    }

    public static List<QuestionEntity> queryAllQuestions(int paperId) {
        List<QuestionEntity> questionEntityList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.prepareStatement("select ID, PAPER_ID, SOURCE, QUESTION_TYPE, QUESTION_STEM, ANSWER_SELECT, ANSWER, ANSWER_ANALYSIS, CREATE_TIME, UPDATE_TIME from QUESTION where PAPER_ID=? order by ID;");
            statement.setInt(1, paperId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                QuestionEntity questionEntity = new QuestionEntity();
                questionEntity.setId(resultSet.getInt("ID"));
                questionEntity.setPaperId(resultSet.getInt("PAPER_ID"));
                questionEntity.setSource(resultSet.getString("SOURCE"));
                questionEntity.setQuestionType(resultSet.getString("QUESTION_TYPE"));
                questionEntity.setQuestionStem(resultSet.getString("QUESTION_STEM"));
                questionEntity.setAnswerSelect(resultSet.getString("ANSWER_SELECT"));
                questionEntity.setAnswer(resultSet.getString("ANSWER"));
                questionEntity.setAnswerAnalysis(resultSet.getString("ANSWER_ANALYSIS");
                questionEntity.setCreateTime(resultSet.getDate("CREATE_TIME"));
                questionEntity.setUpdateTime(resultSet.getDate("UPDATE_TIME"));
                questionEntityList.add(questionEntity);
            }

        } catch (Exception e) {
            LogUtils.LOG.error(e);
        } finally {
            close(statement, connection);
        }
        return questionEntityList;
    }

    public static PaperEntity queryPaperById(int paperId) {
        PaperEntity paperEntity = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.prepareStatement("select ID,SOURCE_ID,SOURCE_URL, TITLE,SOURCE,PROVINCE,CREATE_TIME,UPDATE_TIME from PAPER where ID= ?");
            statement.setInt(1, paperId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                paperEntity = setPaper(resultSet);
            }

        } catch (Exception e) {
            LogUtils.LOG.error(e);
        } finally {
            close(statement, connection);
        }
        return paperEntity;
    }

    private static PaperEntity setPaper(ResultSet resultSet) throws SQLException {
        PaperEntity paperEntity = new PaperEntity();
        paperEntity.setId(resultSet.getInt("ID"));
        paperEntity.setSourceId(resultSet.getString("SOURCE_ID"));
        paperEntity.setSourceUrl(resultSet.getString("SOURCE_URL"));
        paperEntity.setTitle(resultSet.getString("TITLE"));
        paperEntity.setSource(resultSet.getString("SOURCE"));
        paperEntity.setProvince(resultSet.getString("PROVINCE"));
        paperEntity.setCreateTime(resultSet.getDate("CREATE_TIME"));
        paperEntity.setUpdateTime(resultSet.getDate("UPDATE_TIME"));
        return paperEntity;
    }
}
