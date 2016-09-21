package util;

import java.sql.*;

/**
 * Created by bovink on 2016/9/18.
 * 操作Sqlite数据库的通用工具方法
 */
public class SqliteUtil {

    /**
     * 获取数据库连接实例
     *
     * @return 连接
     */
    public static Connection getConnection(String string) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite://G:/osu/" + string);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return connection;
    }

    public static Connection getMySQLConn() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String serverName = "localhost";
        String database = "world";
        String url = "jdbc:mysql://" + serverName + "/" + database;

        String user = "root";
        String password = "123456";
        return DriverManager.getConnection(url, user, password);
    }
}
