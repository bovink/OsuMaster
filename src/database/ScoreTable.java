package database;

import bean.ScoreBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static util.SqliteUtil.getConnection;

/**
 * Created by bovink on 2016/9/20.
 * 用户分数表
 */
public class ScoreTable {

    private static String databaseName = "score.db";

    /**
     * 创建Score表
     */
    public static void createTable() {
        Connection connection = getConnection(databaseName);
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "SELECT * FROM sqlite_master WHERE type='table' AND name = 'SCORE'";
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                sql = "CREATE TABLE SCORE" +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "BEATMAP_ID INTEGER," +
                        "SCORE_ID INTEGER," +
                        "SCORE INTEGER," +
                        "USERNAME VARCHAR(50)," +
                        "COUNT300 INTEGER," +
                        "COUNT100 INTEGER," +
                        "COUNT50 INTEGER," +
                        "COUNTMISS INTEGER," +
                        "MAXCOMBO INTEGER," +
                        "COUNTKATU INTEGER," +
                        "COUNTGEKI INTEGER," +
                        "PERFECT INTEGER," +
                        "ENABLE_MODS INTEGER," +
                        "USER_ID INTEGER," +
                        "DATE CHAR(19)," +
                        "RANK CHAR(2)," +
                        "PP REAL)";
                statement.executeUpdate(sql);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入score信息
     *
     * @param scoreInfo 分数
     */
    public static void insertScore(Connection connection, String beatmap_id, ScoreBean.ScoresBeanInfo scoreInfo) {
        Statement statement;

        String sql = "INSERT INTO SCORE (BEATMAP_ID, SCORE_ID, SCORE, USERNAME, COUNT300, COUNT100, COUNT50, COUNTMISS, MAXCOMBO," +
                " COUNTKATU, COUNTGEKI, PERFECT, ENABLE_MODS, USER_ID, DATE, RANK, PP) " +
                "VALUES (" +
                Integer.valueOf(beatmap_id) + "," +
                Long.valueOf(scoreInfo.getScore_id()) + "," +
                Long.valueOf(scoreInfo.getScore()) + ",\"" +
                scoreInfo.getUsername() + "\"," +
                Integer.valueOf(scoreInfo.getCount300()) + "," +
                Integer.valueOf(scoreInfo.getCount100()) + "," +
                Integer.valueOf(scoreInfo.getCount50()) + "," +
                Integer.valueOf(scoreInfo.getCountmiss()) + "," +
                Integer.valueOf(scoreInfo.getMaxcombo()) + "," +
                Integer.valueOf(scoreInfo.getCountkatu()) + "," +
                Integer.valueOf(scoreInfo.getCountgeki()) + "," +
                Integer.valueOf(scoreInfo.getPerfect()) + "," +
                Long.valueOf(scoreInfo.getEnabled_mods()) + "," +
                Long.valueOf(scoreInfo.getUser_id()) + ",\"" +
                scoreInfo.getDate() + "\",\"" +
                scoreInfo.getRank() + "\"," +
                Double.valueOf(scoreInfo.getPp()) + ");";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);

            statement.close();
        } catch (SQLException e) {
            System.out.println("sql:" + sql);
            e.printStackTrace();
        }
    }

    /**
     * 查询ScoreId为指定id的数据
     *
     * @param scoreId 指定id
     * @return 是否存在
     */
    public static boolean queryScore(Connection connection, long scoreId) {
        Statement statement;

        try {
            statement = connection.createStatement();

            String sql = "SELECT * FROM SCORE WHERE SCORE_ID = " + scoreId;
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return true;
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void delete(Connection connection, long scoreId) {
        Statement statement;

        try {
            statement = connection.createStatement();
            String sql = "DELETE FROM SCORE WHERE SCORE_ID = " + scoreId;

            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
