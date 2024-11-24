package com.kun.plugin.publicexamtraining.data.dao;



import com.kun.plugin.publicexamtraining.common.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Init {


    private static final String DB_URL = String.format("jdbc:h2:file:%s/db/publicexam;MODE=MySQL;DB_CLOSE_ON_EXIT=TRUE;AUTO_SERVER=TRUE;CACHE_SIZE=2048;", Constants.CACHE_HOME);

    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "sa";
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_NAME = "publicexam";
    private static final String DB_TABLE = "editor";
    private static final String DB_TABLE_SQL = "CREATE TABLE IF NOT EXISTS editor1 (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), content VARCHAR(255), create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";

    public static void init() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            statement.executeUpdate(DB_TABLE_SQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("DB_URL = " + DB_URL);
        init();
    }




}
