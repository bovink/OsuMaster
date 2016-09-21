package main;

import database.ScoreDatabase;

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
//        scoreDatabase.startDelete();

    }



}
