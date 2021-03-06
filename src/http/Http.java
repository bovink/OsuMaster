package http;


import api.API;
import bean.BeatmapBean;
import bean.BeatmapBean.BeatmapBeanInfo;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import ui.OsuMasterApplication.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static bean.ScoreBean.ScoresBeanInfo;
import static bean.ScoreBean.generateList;

/**
 * Created by Bovink on 2016/5/7 0007.
 * 网络处理
 */
public class Http {
    /**
     * beatmap下载地址文件储存路径
     */
    private String downloadPath = "G:/Project/IdeaProjects/buffer";
    /**
     * 一次遍历中符合条件的beatmap_id 列表
     */
    private ArrayList<String> beatmapIdList = new ArrayList<>();
    /**
     * 一次遍历中符合条件的beatmapset_id 列表
     */
    private ArrayList<String> beatmapSetIdList = new ArrayList<>();
    /**
     * 一次遍历中符合条件的file_md5 列表
     */
    private ArrayList<String> fileMD5List = new ArrayList<>();
    /**
     * beatmap下载地址String列表
     */
    private ArrayList<String> target = new ArrayList<>();

    /**
     * 记录当前查询的beatmap的索引
     */
    private int index = 0;
    /**
     * 记录当前查询的beatmap的总数
     */
    private int currentTotal;
    /**
     * 是否继续查询beatmap，flag
     */
    private boolean continueGet = true;

    //    private String startTime = "2015-01-01-00:00:00";
    /**
     * 遍历开始时间
     */
    private String startTime = "2009-02-01-00:00:17";
    /**
     * 遍历结束时间
     */
    private String endTime = startTime.substring(5, 7);

    private String mods = "0";
    private OnGetScoreListener listener;

    /**
     * PP对值，前后差值为10
     */
    private ArrayList<Pair> pairs = new ArrayList<>();

    /**
     * Integer为pairs
     */
    private HashMap<Integer, ArrayList<String>> collections = new HashMap<>();

    /**
     * 判断是否超过日期
     *
     * @param time 指定时间
     */
    private void judgeExpiry(String time) {
        String year = time.substring(0, 4);
        String month = time.substring(5, 7);
        if (!Objects.equals(Integer.valueOf(month), Integer.valueOf(endTime))) {
            System.out.println(startTime);
            continueGet = false;
        }

    }


    /**
     * 初始化Collections
     */
    public void initCollections() {
        for (int i = 0; i < pairs.size(); i++) {
            collections.put(i, new ArrayList<>());
        }
    }


    /**
     * 获取从开始时间起一定数量的beatmap
     */
    public void getBeatmap() {
        // 获取json
        String url = API.BEATMAP + "&since=" + startTime;
        String result = httpGet(url);
        // 转换json
        ArrayList<BeatmapBeanInfo> list;
        try {
            list = BeatmapBean.generateList(result);
            // 储存beatmap id
            for (BeatmapBeanInfo aList : list) {
                beatmapIdList.add(aList.getBeatmap_id());
                beatmapSetIdList.add(aList.getBeatmapset_id());
                fileMD5List.add(aList.getFile_md5());
            }

            currentTotal = beatmapIdList.size();
            System.out.println(startTime);
            // 处理时间
            String end = list.get(list.size() - 1).getApproved_date();
            startTime = end.substring(0, 10) + "-" + end.substring(11);
            judgeExpiry(startTime);
            // 过滤
            getScore();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 输出下载地址
     */
    private void outputTarget() {
        File file = new File(downloadPath + "/" + startTime.substring(0, 4) + endTime + ".txt");

        StringBuilder sb = new StringBuilder();
        for (String aTarget : target) {
            sb.append(aTarget);
            sb.append("\r\n");
        }
        try {
            FileUtils.write(file, sb.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取分数
     */
    private void getScore() {
        if (index > beatmapIdList.size() - 1) {
//            continueGet = false;
            // 重置
            if (continueGet) {
                index = 0;
                beatmapIdList.clear();
                beatmapSetIdList.clear();
                fileMD5List.clear();
                getBeatmap();
            } else {
                outputTarget();
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/resource/remind.wav");
                    AudioStream as = new AudioStream(inputStream);
                    AudioPlayer.player.start(as);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        String url = API.SCORES + "&b=" + beatmapIdList.get(index) + "&mods=" + mods + "&limit=1";
        if (listener != null) {
            listener.getScore(String.valueOf(currentTotal - 1 - index));

            System.out.println("rest:" + String.valueOf(currentTotal - 1 - index));
        }
        String result = httpGet(url);
        // 某张图的分数
        ArrayList<ScoresBeanInfo> list;
        try {
            list = generateList(result);
            float max = 0;

            // 找出最大PP值
            for (ScoresBeanInfo aList : list) {

                // 如果FC
                if (aList.getPerfect().equals("1")) {
                    if (aList.getPp() != null) {
                        float PP = Float.valueOf(aList.getPp());

                        // 如果PP值太高直接跳过
                        if (max < PP) {
                            max = PP;
                        }
                    }
                }
            }
            // 循环pairs，找到满足条件的区间
            for (int i = 0; i < pairs.size(); i++) {
                if (max >= pairs.get(i).getFirst() && max < pairs.get(i).getSecond()) {
                    // 血猫下载地址前缀
                    String prefix = "http://bloodcat.com/osu/s/";
                    target.add(prefix + beatmapSetIdList.get(index));
                    ArrayList<String> md5s = collections.get(i);
                    md5s.add(fileMD5List.get(index));

                    break;
                }

            }
            index += 1;
            getScore();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理网络请求，获取字符串
     *
     * @param url 地址
     * @return json数据
     */
    public static String httpGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                String line;
                while ((line = bReader.readLine()) != null) {
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        endTime = startTime.substring(5, 7);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setMods(String mods) {
        this.mods = mods;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setListener(OnGetScoreListener listener) {
        this.listener = listener;
    }

    public void setPairs(ArrayList<Pair> pairs) {
        this.pairs = pairs;
    }

    public HashMap<Integer, ArrayList<String>> getCollections() {
        return collections;
    }

    /**
     * 外部接口
     */
    public interface OnGetScoreListener {
        void getScore(String rest);
    }
}

