package api;

/**
 * Created by Bovink on 2016/5/7 0007.
 */
public class API {
    private static String API_KEY = "6f94cce4f292203cc73611da22834c1c117fd308";

    private static String HOST = "http://osu.ppy.sh/api/";

    /**
     * 获取Beatmap
     */
    public static String BEATMAP = HOST + "get_beatmaps?k=" + API_KEY;
    /**
     * 获取Score
     */
    public static String SCORES = HOST + "get_scores?k=" + API_KEY;
}

