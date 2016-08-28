package collection;

import java.util.ArrayList;

/**
 * Created by Bovink on 2016/5/8 0008.
 */
public class Collection {
    private String name;
    private int beatmapTotal;
    private ArrayList<String> beatmapMD5List = new ArrayList<>();

    public ArrayList<String> getBeatmapMD5List() {
        return beatmapMD5List;
    }

    public void setBeatmapMD5List(ArrayList<String> beatmapMD5List) {
        this.beatmapMD5List = beatmapMD5List;
    }

    public int getBeatmapTotal() {
        return beatmapTotal;
    }

    public void setBeatmapTotal(int beatmapTotal) {
        this.beatmapTotal = beatmapTotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
