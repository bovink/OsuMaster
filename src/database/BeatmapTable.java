package database;

import bean.BeatmapBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static util.SqliteUtil.getConnection;

/**
 * Created by bovink on 2016/9/18.
 * 操作Beatmap表
 */
public class BeatmapTable {
    private static String databaseName = "beatmap.db";

    /**
     * 创建Beatmap表
     */
    public static void createTable() {
        Connection connection = getConnection(databaseName);
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "SELECT * FROM sqlite_master WHERE type='table' AND name = 'BEATMAP'";
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                sql = "CREATE TABLE BEATMAP" +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "APPROVED INTEGER," +
                        "APPROVED_DATE CHAR(19)," +
                        "LAST_UPDATE CHAR(19)," +
                        "ARTIST VARCHAR(50)," +
                        "BEATMAP_ID INTEGER," +
                        "BEATMAPSET_ID INTEGER," +
                        "BPM REAL," +
                        "CREATOR VARCHAR(30)," +
                        "DIFFICULTYRATING REAL," +
                        "DIFF_SIZE REAL," +
                        "DIFF_OVERALL REAL," +
                        "DIFF_APPROACH REAL," +
                        "DIFF_DRAIN REAL," +
                        "HIT_LENGTH INTEGER," +
                        "GENRE_ID INTEGER," +
                        "LANGUAGE_ID INTEGER," +
                        "TITLE VARCHAR(50)," +
                        "TOTAL_LENGTH INTEGER," +
                        "VERSION VARCHAR(50)," +
                        "FILE_MD5 CHAR(32)," +
                        "MODE INTEGER," +
                        "FAVOURITE_COUNT INTEGER," +
                        "PLAYCOUNT INTEGER," +
                        "PASSCOUNT INTEGER," +
                        "MAX_COMBO INTEGER)";
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
     * 插入beatmap信息
     *
     * @param beatmapInfo
     */
    public static void insertBeatmap(Connection connection, BeatmapBean.BeatmapBeanInfo beatmapInfo) {
        Statement statement;

        String artist = beatmapInfo.getArtist();
        String creator = beatmapInfo.getCreator();
        String title = beatmapInfo.getTitle();
        String version = beatmapInfo.getVersion();
        String maxCombo = beatmapInfo.getMax_combo();
        if (maxCombo == null) {
            maxCombo = "0";
        }
        artist = artist.replace("\"", "\'");
        creator = creator.replace("\"", "\'");
        title = title.replace("\"", "\'");
        version = version.replace("\"", "\'");


        String sql = "INSERT INTO BEATMAP (APPROVED, APPROVED_DATE, LAST_UPDATE, ARTIST, BEATMAP_ID, BEATMAPSET_ID," +
                " BPM, CREATOR, DIFFICULTYRATING, DIFF_SIZE, DIFF_OVERALL, DIFF_APPROACH, DIFF_DRAIN, HIT_LENGTH, " +
                "GENRE_ID, LANGUAGE_ID, TITLE, TOTAL_LENGTH, VERSION, FILE_MD5, MODE, FAVOURITE_COUNT, PLAYCOUNT, " +
                "PASSCOUNT, MAX_COMBO) " +
                "VALUES (" + Integer.valueOf(beatmapInfo.getApproved()) + ",\"" +
                beatmapInfo.getApproved_date() + "\",\"" + beatmapInfo.getLast_update() + "\",\"" + artist + "\"," +
                Integer.valueOf(beatmapInfo.getBeatmap_id()) + "," + Integer.valueOf(beatmapInfo.getBeatmapset_id()) + "," + Double.valueOf(beatmapInfo.getBpm()) + ",\"" +
                creator + "\"," + Double.valueOf(beatmapInfo.getDifficultyrating()) + "," + Double.valueOf(beatmapInfo.getDiff_size()) + "," +
                Double.valueOf(beatmapInfo.getDiff_overall()) + "," + Double.valueOf(beatmapInfo.getDiff_approach()) + "," + Double.valueOf(beatmapInfo.getDiff_drain()) + "," +
                Integer.valueOf(beatmapInfo.getHit_length()) + "," + Integer.valueOf(beatmapInfo.getGenre_id()) + "," + Integer.valueOf(beatmapInfo.getLanguage_id()) + ",\"" +
                title + "\"," + Integer.valueOf(beatmapInfo.getTotal_length()) + ",\"" + version + "\",\"" +
                beatmapInfo.getFile_md5() + "\"," + Integer.valueOf(beatmapInfo.getMode()) + "," + Integer.valueOf(beatmapInfo.getFavourite_count()) + "," +
                Integer.valueOf(beatmapInfo.getPlaycount()) + "," + Integer.valueOf(beatmapInfo.getPasscount()) + "," + maxCombo + ");";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);

            statement.close();
//            connection.close();
        } catch (SQLException e) {
            System.out.println("插入数据:" + sql);
            e.printStackTrace();
        }
    }

    /**
     * 查询BeatmapId为指定id的数据
     *
     * @param beatmapId 指定id
     * @return 是否存在
     */
    public static boolean queryBeatmap(Connection connection, int beatmapId) {
        Statement statement;

        try {
            statement = connection.createStatement();

            String sql = "SELECT * FROM BEATMAP WHERE BEATMAP_ID = " + beatmapId;
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return true;
            }

            resultSet.close();
            statement.close();
//            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 更新beatmapId为指定id的beatmap的approved为指定approved
     *
     * @param beatmapId 指定id
     * @param approved  指定approved
     */
    public static void updateBeatmap(int beatmapId, int approved) {
        Connection connection = getConnection(databaseName);
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "UPDATE BEATMAP SET APPROVED = " + approved + " WHERE BEATMAP_ID = " + beatmapId;
            statement.executeUpdate(sql);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除beatmapId为指定id的beatmap
     *
     * @param beatmapId 指定id
     */
    public static void deleteBeatmap(int beatmapId) {
        Connection connection = getConnection(databaseName);
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "DELETE FROM BEATMAP WHERE BEATMAP_ID = " + beatmapId;
            statement.executeUpdate(sql);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
