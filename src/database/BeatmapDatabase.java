package database;

import api.API;
import bean.BeatmapBean;
import org.json.JSONException;
import util.SqliteUtil;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static database.BeatmapTable.queryBeatmap;
import static http.Http.httpGet;

/**
 * Created by bovink on 2016/9/21.
 * 操作beatmap数据库
 */
public class BeatmapDatabase {

    /*
    * 1月
    * 开始请求数据
    * 判断本月是否结束，没有继续请求
    * 插入数据库时判断
    * 直到把1月的数据请求完
    *
    */
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


    /**
     * 插入数据库 线程
     */
    private class InsertRunnable implements Runnable {
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
}

