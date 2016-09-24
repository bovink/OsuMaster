package main;

import collection.Collection;
import collection.CollectionDb;
import collection.CollectionDecoder;
import collection.CollectionEncoder;
import util.SqliteUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Bovink on 2016/9/24.
 * 测试输出
 */
public class Generate {
    /**
     * Beatmap数据库连接
     */
    Connection beatmapConn;
    /**
     * 分数数据库连接
     */
    Connection mySQLconn;
    /**
     * 收集的md5
     */
    ArrayList<String> collection = new ArrayList<>();
    private CollectionDb collectionDb;
    String path = "G:/osu/test";

    public Generate() {
        beatmapConn = SqliteUtil.getConnection("beatmap.db");
        try {
            mySQLconn = SqliteUtil.getMySQLConn();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void generate() {
        decode();
        getBeatmapScore();

    }


    private String collectionName = "None 160-170";
    private String min = "160";
    private String max = "170";
    private int index = 1;

    /**
     * 获取符合标准的beatmap_id
     */
    public void getBeatmapScore() {
        String tableName;
        if (index == 1) {
            tableName = "score1";
        } else if (index == 2) {
            tableName = "score2";
        } else {
            return;
        }
        Statement statement;

        try {
            statement = mySQLconn.createStatement();
            // 获取所有APPROVED了的图，按BEATMAP_ID排序
            String sql = "SELECT * FROM " + tableName + " WHERE pp >= " + min + " AND  pp <= " + max + " AND perfect = 1 AND  enable_mods = 0 ORDER BY beatmap_id ASC";
            ResultSet resultSet = statement.executeQuery(sql);
            String previous = "";
            while (resultSet.next()) {
                String beatmap_id = resultSet.getString("beatmap_id");
                if (!beatmap_id.equals(previous)) {
                    getBeatmap(beatmap_id);
                }
                previous = beatmap_id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (index == 1) {
            index = 2;
            getBeatmapScore();
        } else if (index == 2) {

            encode();
            decode();
        }
    }

    /**
     * 获取符合标准的beatmap的md5
     */
    public void getBeatmap(String beatmap_id) {
        Statement statement;

        try {
            statement = beatmapConn.createStatement();
            // 获取所有APPROVED了的图，按BEATMAP_ID排序
            String sql = "SELECT * FROM BEATMAP WHERE BEATMAP_ID = " + beatmap_id;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String md5 = resultSet.getString("file_md5");
                collection.add(md5);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void decode() {
        CollectionDecoder decoder = new CollectionDecoder();
        decoder.setCollectionDbPath(path);
        decoder.decode();

        collectionDb = decoder.getCollectionDb();
    }

    public void encode() {
        Collection added = new Collection();
        added.setName(collectionName);
        added.setBeatmapTotal(collection.size());
        added.setBeatmapMD5List(collection);

        collectionDb.addCollection(added);


        CollectionEncoder encoder = new CollectionEncoder();
        encoder.setCollectionPath(path);
        encoder.setCollectionDb(collectionDb);
        encoder.encode();

    }
}
