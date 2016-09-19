package sql;

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

    /**
     * 创建Beatmap表
     */
    public static void createTable() {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "SELECT * FROM sqlite_master WHERE type='table' AND name = 'BEATMAPS'";
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                sql = "CREATE TABLE BEATMAPS" +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "APPROVED INTEGER," +
                        "APPROVED_DATE CHAR(19)," +
                        "LAST_UPDATE CHAR(19)," +
                        "ARTIST VARCHAR(50)," +
                        "BEATMAP_ID INTEGER," +
                        "BEATMAPSET_ID INTEGER," +
                        "BPM INTEGER," +
                        "CREATOR VARCHAR(30)," +
                        "DIFFICULTYRATING REAL," +
                        "DIFF_SIZE INTEGER," +
                        "DIFF_OVERALL INTEGER," +
                        "DIFF_APPROACH INTEGER," +
                        "DIFF_DRAIN INTEGER," +
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
    public static void insertBeatmap(BeatmapBean.BeatmapBeanInfo beatmapInfo) {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "INSERT INTO BEATMAPS (APPROVED, APPROVED_DATE, LAST_UPDATE, ARTIST, BEATMAP_ID, BEATMAPSET_ID," +
                    " BPM, CREATOR, DIFFICULTYRATING, DIFF_SIZE, DIFF_OVERALL, DIFF_APPROACH, DIFF_DRAIN, HIT_LENGTH, " +
                    "GENRE_ID, LANGUAGE_ID, TITLE, TOTAL_LENGTH, VERSION, FILE_MD5, MODE, FAVOURITE_COUNT, PLAYCOUNT, " +
                    "PASSCOUNT, MAX_COMBO) " +
                    "VALUES (" + Integer.valueOf(beatmapInfo.getApproved()) + ",\"" +
                    beatmapInfo.getApproved_date() + "\",\"" + beatmapInfo.getLast_update() + "\",\"" + beatmapInfo.getArtist() + "\"," +
                    Integer.valueOf(beatmapInfo.getBeatmap_id()) + "," + Integer.valueOf(beatmapInfo.getBeatmapset_id()) + "," + Double.valueOf(beatmapInfo.getBpm()) + ",\"" +
                    beatmapInfo.getCreator() + "\"," + Double.valueOf(beatmapInfo.getDifficultyrating()) + "," + Integer.valueOf(beatmapInfo.getDiff_size()) + "," +
                    Integer.valueOf(beatmapInfo.getDiff_overall()) + "," + Integer.valueOf(beatmapInfo.getDiff_approach()) + "," + Integer.valueOf(beatmapInfo.getDiff_drain()) + "," +
                    Integer.valueOf(beatmapInfo.getHit_length()) + "," + Integer.valueOf(beatmapInfo.getGenre_id()) + "," + Integer.valueOf(beatmapInfo.getLanguage_id()) + ",\"" +
                    beatmapInfo.getTitle() + "\"," + Integer.valueOf(beatmapInfo.getTotal_length()) + ",\"" + beatmapInfo.getVersion() + "\",\"" +
                    beatmapInfo.getFile_md5() + "\"," + Integer.valueOf(beatmapInfo.getMode()) + "," + Integer.valueOf(beatmapInfo.getFavourite_count()) + "," +
                    Integer.valueOf(beatmapInfo.getPlaycount()) + "," + Integer.valueOf(beatmapInfo.getPasscount()) + "," + Integer.valueOf(beatmapInfo.getMax_combo()) + ");";
            statement.executeUpdate(sql);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询BeatmapId为指定id的数据
     *
     * @param beatmapId 指定id
     * @return 是否存在
     */
    public static boolean queryBeatmap(int beatmapId) {
        Connection connection = getConnection();
        Statement statement;

        try {
            statement = connection.createStatement();

            String sql = "SELECT * FROM BEATMAPS WHERE BEATMAP_ID = " + beatmapId;
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                System.out.println("TITLE =\t\t\t\t" + resultSet.getString("title"));
                System.out.println("ARTIST=\t\t\t\t" + resultSet.getString("artist"));
                return true;
            } else {
                System.out.println("不存在该数据");
            }

            resultSet.close();
            statement.close();
            connection.close();
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
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "UPDATE BEATMAPS SET APPROVED = " + approved + " WHERE BEATMAP_ID = " + beatmapId;
            statement.executeUpdate(sql);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除beatmapId为指定id的beatmap
     * @param beatmapId 指定id
     */
    public static void deleteBeatmap(int beatmapId) {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "DELETE FROM BEATMAPS WHERE BEATMAP_ID = " + beatmapId;
            statement.executeUpdate(sql);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
