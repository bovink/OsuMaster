package database;

import api.API;
import bean.ScoreBean;
import com.google.gson.JsonSyntaxException;
import org.json.JSONException;
import util.SqliteUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import static bean.ScoreBean.generateList;
import static http.Http.httpGet;

/**
 * Created by bovink on 2016/9/21.
 * 操作分数数据库
 */
public class ScoreDatabase {

    /*
    * 1月
    * 开始请求数据
    * 判断本月是否结束，没有继续请求
    * 插入数据库时判断
    * 直到把1月的数据请求完
    *
    */
    Connection beatmapConn = SqliteUtil.getConnection("beatmap.db");
    Connection scoreConn = SqliteUtil.getConnection("score.db");
    ConcurrentHashMap<String, ArrayList<ScoreBean.ScoresBeanInfo>> scoreTotal = new ConcurrentHashMap<>();
    ArrayList<String> beatmap_ids = new ArrayList<>();

    public void startInsert() {
        getBeatmapId("3000", "4000");
    }

    /**
     * 删掉多余的分数，每张图只留前50名
     */
    public void startDelete() {
        getBeatmap();
    }

    private void getBeatmap() {
        Statement statement;

        try {
            statement = beatmapConn.createStatement();
            // 获取所有APPROVED了的图，按BEATMAP_ID排序
            String sql = "SELECT * FROM BEATMAP WHERE APPROVED = 1  ORDER BY BEATMAP_ID ASC";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String beatmap_id = resultSet.getString("beatmap_id");
                deleteScore(beatmap_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteScore(String beatmap_id) {
        Statement statement;
        try {
            statement = scoreConn.createStatement();
            // 获取该图的分数，按PP排名
            String sql = "SELECT * FROM SCORE WHERE BEATMAP_ID = " + beatmap_id + " ORDER BY SCORE DESC";
            ResultSet scoreResultSet = statement.executeQuery(sql);
            int index = 0;
            while (scoreResultSet.next()) {
                index += 1;
                String scoreId = scoreResultSet.getString("score_id");
                // 低于50名的删掉
                if (index > 50) {
                    ScoreTable.delete(scoreConn, Long.valueOf(scoreId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void getBeatmapId(String start, String end) {
        System.out.println(start + " and " + end);
        Statement statement;
        try {
            // 分10次，1次十万
            statement = beatmapConn.createStatement();
            String sql = "SELECT * FROM BEATMAP WHERE APPROVED = 1 AND ID > " + start + " AND ID < " + end + " ORDER BY BEATMAP_ID";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                // 将获取的beatmap_id全部保存到列表中25
                beatmap_ids.add(resultSet.getString("beatmap_id"));
            }

            // 开启线程，将获取的beatmap_ids分批请求数据，并插入数据库
            int max = 100;
            ArrayList<Thread> list = new ArrayList<>();
            for (int i = 0; i < max; i++) {
                AddRunnable addRunnable = new AddRunnable(i, max, beatmap_ids);
                Thread thread = new Thread(addRunnable);
                list.add(thread);
                thread.start();
            }

            for (Thread thread : list) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("长度:" + scoreTotal.size());

            InsertScoreRunnable insertScoreRunnable = new InsertScoreRunnable(scoreConn, scoreTotal);
            Thread thread = new Thread(insertScoreRunnable);
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scoreTotal.clear();
            System.out.println("end");
            String newStart = String.valueOf(Long.valueOf(start) + 1000);
            String newEnd = String.valueOf(Long.valueOf(end) + 1000);
            getBeatmapId(newStart, newEnd);

            statement.close();
            beatmapConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void requestJSON(String beatmap_id) {

        String url = API.SCORES + "&b=" + beatmap_id + "&limit=50";
        String result = httpGet(url);
        // 某张图的分数
        ArrayList<ScoreBean.ScoresBeanInfo> scores;
        try {
            scores = generateList(result);

            scoreTotal.put(beatmap_id, scores);
            System.out.println(beatmap_id);

            // 找出最大PP值
            for (ScoreBean.ScoresBeanInfo score : scores) {
//                    InsertScoreRunnable insertScoreRunnable = new InsertScoreRunnable(scoreConn, score, beatmap_id);
//                    Thread thread = new Thread(insertScoreRunnable);
//                    thread.start();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 分批request
    private class AddRunnable implements Runnable {
        ArrayList<String> strings;
        int index;
        int max;

        public AddRunnable(int index, int max, ArrayList<String> strings) {
            this.index = index;
            this.max = max;
            this.strings = strings;
        }

        int tag = 0;
        boolean success = true;

        @Override
        public void run() {
            for (int i = strings.size() * index / max; i < strings.size() * (index + 1) / max; i++) {
                try {
                    // 当失败时，执行标签
                    if (!success) {
                        i = tag;
                    }
                    success = true;
                    requestJSON(strings.get(i));

                } catch (JsonSyntaxException e) {
                    success = false;
                    tag = i;

                }
            }

        }

    }

    /**
     * 插入分数数据库 线程
     */
    private class InsertScoreRunnable implements Runnable {
        ScoreBean.ScoresBeanInfo scoreBeanInfo;
        Connection connection;
        String beatmap_id;
        ConcurrentHashMap<String, ArrayList<ScoreBean.ScoresBeanInfo>> scoreTotal;

        InsertScoreRunnable(Connection connection, ScoreBean.ScoresBeanInfo scoresBeanInfo, String beatmap_id) {
            this.scoreBeanInfo = scoresBeanInfo;
            this.connection = connection;
            this.beatmap_id = beatmap_id;
        }

        public InsertScoreRunnable(Connection connection, ConcurrentHashMap<String, ArrayList<ScoreBean.ScoresBeanInfo>> scoreTotal) {
            this.connection = connection;
            this.scoreTotal = scoreTotal;
        }

        @Override
        public void run() {
            Enumeration<String> keys = scoreTotal.keys();
            while (keys.hasMoreElements()) {
                beatmap_id = keys.nextElement();
                ArrayList<ScoreBean.ScoresBeanInfo> scores = scoreTotal.get(beatmap_id);
                for (ScoreBean.ScoresBeanInfo score : scores) {

                    //如果不存在该数据，则插入数据
                    if (!ScoreTable.queryScore(connection, Long.valueOf(score.getScore_id()))) {
                        ScoreTable.insertScore(connection, beatmap_id, score);
                    }

                }
            }

        }
    }
}
