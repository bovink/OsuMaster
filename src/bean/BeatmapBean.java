package bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Bovink on 2016/5/7 0007.
 */
public class BeatmapBean {

    public static ArrayList<BeatmapBeanInfo> generateList(String json) throws JSONException {

        Type type = new TypeToken<ArrayList<BeatmapBeanInfo>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<BeatmapBeanInfo> list = gson.fromJson(json, type);
        return list;
    }

    public static class BeatmapBeanInfo {
        private String approved;
        private String approved_date;
        private String last_update;
        private String artist;
        private String beatmap_id;
        private String beatmapset_id;
        private String bpm;
        private String creator;
        private String difficultyrating;
        private String diff_size;
        private String diff_overall;
        private String diff_approach;
        private String diff_drain;
        private String hit_length;
        private String source;
        private String genre_id;
        private String language_id;
        private String title;
        private String total_length;
        private String version;
        private String file_md5;
        private String mode;
        private String tags;
        private String favourite_count;
        private String playcount;
        private String passcount;
        private String max_combo;

        public String getApproved() {
            return approved;
        }

        public void setApproved(String approved) {
            this.approved = approved;
        }

        public String getApproved_date() {
            return approved_date;
        }

        public void setApproved_date(String approved_date) {
            this.approved_date = approved_date;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getBeatmap_id() {
            return beatmap_id;
        }

        public void setBeatmap_id(String beatmap_id) {
            this.beatmap_id = beatmap_id;
        }

        public String getBeatmapset_id() {
            return beatmapset_id;
        }

        public void setBeatmapset_id(String beatmapset_id) {
            this.beatmapset_id = beatmapset_id;
        }

        public String getBpm() {
            return bpm;
        }

        public void setBpm(String bpm) {
            this.bpm = bpm;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getDiff_approach() {
            return diff_approach;
        }

        public void setDiff_approach(String diff_approach) {
            this.diff_approach = diff_approach;
        }

        public String getDiff_drain() {
            return diff_drain;
        }

        public void setDiff_drain(String diff_drain) {
            this.diff_drain = diff_drain;
        }

        public String getDiff_overall() {
            return diff_overall;
        }

        public void setDiff_overall(String diff_overall) {
            this.diff_overall = diff_overall;
        }

        public String getDiff_size() {
            return diff_size;
        }

        public void setDiff_size(String diff_size) {
            this.diff_size = diff_size;
        }

        public String getDifficultyrating() {
            return difficultyrating;
        }

        public void setDifficultyrating(String difficultyrating) {
            this.difficultyrating = difficultyrating;
        }

        public String getFavourite_count() {
            return favourite_count;
        }

        public void setFavourite_count(String favourite_count) {
            this.favourite_count = favourite_count;
        }

        public String getFile_md5() {
            return file_md5;
        }

        public void setFile_md5(String file_md5) {
            this.file_md5 = file_md5;
        }

        public String getGenre_id() {
            return genre_id;
        }

        public void setGenre_id(String genre_id) {
            this.genre_id = genre_id;
        }

        public String getHit_length() {
            return hit_length;
        }

        public void setHit_length(String hit_length) {
            this.hit_length = hit_length;
        }

        public String getLanguage_id() {
            return language_id;
        }

        public void setLanguage_id(String language_id) {
            this.language_id = language_id;
        }

        public String getLast_update() {
            return last_update;
        }

        public void setLast_update(String last_update) {
            this.last_update = last_update;
        }

        public String getMax_combo() {
            return max_combo;
        }

        public void setMax_combo(String max_combo) {
            this.max_combo = max_combo;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getPasscount() {
            return passcount;
        }

        public void setPasscount(String passcount) {
            this.passcount = passcount;
        }

        public String getPlaycount() {
            return playcount;
        }

        public void setPlaycount(String playcount) {
            this.playcount = playcount;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTotal_length() {
            return total_length;
        }

        public void setTotal_length(String total_length) {
            this.total_length = total_length;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}

