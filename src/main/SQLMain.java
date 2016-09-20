package main;

import api.API;
import bean.BeatmapBean;
import bean.ScoreBean;
import database.BeatmapTable;
import database.ScoreTable;
import org.json.JSONException;
import util.SqliteUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static bean.ScoreBean.generateList;
import static database.BeatmapTable.queryBeatmap;
import static http.Http.httpGet;

/**
 * Created by bovink on 2016/9/19.
 * 获取Json数据，加载到本地数据库
 */
public class SQLMain {

    public static void main(String[] args) {

//        BeatmapDatabase beatmapDatabase = new BeatmapDatabase();
//        beatmapDatabase.startInsert();
        ScoreDatabase scoreDatabase = new ScoreDatabase();
        scoreDatabase.startInsert();

    }

    /*
    * 1月
    * 开始请求数据
    * 判断本月是否结束，没有继续请求
    * 插入数据库时判断
    * 直到把1月的数据请求完
    *
    */
    private static class BeatmapDatabase {
        Connection connection = SqliteUtil.getConnection("beatmap.db");
        /**
         * 开始时间
         */
        private String startTime = "2007-10-01-00:00:00";
        /**
         * 截止时间
         */
        private String expiryTime = "2016-09-01-00:00:00";
        /**
         * 请求获取的时间
         */
        private SimpleDateFormat inputFormat;
        /**
         * 把请求的时间格式化输出
         */
        private SimpleDateFormat outputFormat;

        BeatmapDatabase() {
            inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            outputFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINA);
        }

        void startInsert() {
            requestJSON(startTime);
        }

        void requestJSON(String time) {
            System.out.println("开始时间:" + time);
            // 获取json
            String url = API.BEATMAP + "&since=" + time;
            String result = httpGet(url);
            // 转换json
            ArrayList<BeatmapBean.BeatmapBeanInfo> beatmaps;
            try {
                beatmaps = BeatmapBean.generateList(result);
                // 遍历beatmap，如果数据库中不存在则插入数据库
                for (BeatmapBean.BeatmapBeanInfo beatmap : beatmaps) {
                    InsertRunnable insertRunnable = new InsertRunnable(connection, beatmap);
                    Thread thread = new Thread(insertRunnable);
                    thread.start();
                }
                // 获取最后一条通过时间
                String endTime = beatmaps.get(beatmaps.size() - 1).getApproved_date();
                Date endDate = inputFormat.parse(endTime);
                Date expiryDate = outputFormat.parse(expiryTime);
                // 判断该时间是否超过2016年9月
                if (endDate.before(expiryDate)) {
                    Thread.sleep(2000);
                    String startTime = outputFormat.format(endDate);
                    requestJSON(startTime);
                }


            } catch (JSONException | ParseException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /*
    * 1月
    * 开始请求数据
    * 判断本月是否结束，没有继续请求
    * 插入数据库时判断
    * 直到把1月的数据请求完
    *
    */
    private static class ScoreDatabase {
        Connection beatmapConn = SqliteUtil.getConnection("beatmap.db");
        Connection scoreConn = SqliteUtil.getConnection("score.db");

        private void startInsert() {
            getBeatmapId();
        }

        private void getBeatmapId() {
            Statement statement;
            try {
                // 已经遍历到了1401
                //  接下来从800000到881702
                statement = beatmapConn.createStatement();
                String sql = "SELECT * FROM BEATMAP WHERE APPROVED = 1 AND BEATMAP_ID > 861310 ORDER BY BEATMAP_ID";
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    requestJSON(resultSet.getString("beatmap_id"));
                    Thread.sleep(1000);
                }

                statement.close();
                beatmapConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        private void requestJSON(String beatmap_id) {

            System.out.println(beatmap_id);
            String url = API.SCORES + "&b=" + beatmap_id + "&limit=100";
            String result = httpGet(url);
            // 某张图的分数
            ArrayList<ScoreBean.ScoresBeanInfo> scores;
            try {
                scores = generateList(result);

                // 找出最大PP值
                for (ScoreBean.ScoresBeanInfo score : scores) {
                    InsertScoreRunnable insertScoreRunnable = new InsertScoreRunnable(scoreConn, score, beatmap_id);
                    Thread thread = new Thread(insertScoreRunnable);
                    thread.start();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入数据库 线程
     */
    private static class InsertRunnable implements Runnable {
        BeatmapBean.BeatmapBeanInfo beatmapBeanInfo;
        Connection connection;

        InsertRunnable(Connection connection, BeatmapBean.BeatmapBeanInfo beatmapBeanInfo) {
            this.beatmapBeanInfo = beatmapBeanInfo;
            this.connection = connection;
        }

        @Override
        public void run() {
            // 如果不存在该数据，则插入数据
            if (!queryBeatmap(connection, Integer.valueOf(beatmapBeanInfo.getBeatmap_id()))) {
                BeatmapTable.insertBeatmap(connection, beatmapBeanInfo);
            }
        }
    }

    /**
     * 插入分数数据库 线程
     */
    private static class InsertScoreRunnable implements Runnable {
        ScoreBean.ScoresBeanInfo scoreBeanInfo;
        Connection connection;
        String beatmap_id;

        InsertScoreRunnable(Connection connection, ScoreBean.ScoresBeanInfo scoresBeanInfo, String beatmap_id) {
            this.scoreBeanInfo = scoresBeanInfo;
            this.connection = connection;
            this.beatmap_id = beatmap_id;
        }

        @Override
        public void run() {
            // 如果不存在该数据，则插入数据
            if (!ScoreTable.queryScore(connection, Long.valueOf(scoreBeanInfo.getScore_id()))) {
                ScoreTable.insertScore(connection, beatmap_id, scoreBeanInfo);
            }
        }
    }
}
