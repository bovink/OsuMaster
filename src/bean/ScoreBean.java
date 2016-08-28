package bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Bovink on 2016/5/7 0007.
 */
public class ScoreBean {

    public static ArrayList<ScoresBeanInfo> generateList(String json) throws JSONException {

        Type type = new TypeToken<ArrayList<ScoresBeanInfo>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<ScoresBeanInfo> list = gson.fromJson(json, type);
        return list;
    }

    public static class ScoresBeanInfo {
        private String score;
        private String username;
        private String count300;
        private String count100;
        private String count50;
        private String countmiss;
        private String maxcombo;
        private String countkatu;
        private String countgeki;
        private String perfect;
        private String enabled_mods;
        private String user_id;
        private String date;
        private String rank;
        private String pp;

        public String getCount100() {
            return count100;
        }

        public void setCount100(String count100) {
            this.count100 = count100;
        }

        public String getCount300() {
            return count300;
        }

        public void setCount300(String count300) {
            this.count300 = count300;
        }

        public String getCount50() {
            return count50;
        }

        public void setCount50(String count50) {
            this.count50 = count50;
        }

        public String getCountgeki() {
            return countgeki;
        }

        public void setCountgeki(String countgeki) {
            this.countgeki = countgeki;
        }

        public String getCountkatu() {
            return countkatu;
        }

        public void setCountkatu(String countkatu) {
            this.countkatu = countkatu;
        }

        public String getCountmiss() {
            return countmiss;
        }

        public void setCountmiss(String countmiss) {
            this.countmiss = countmiss;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getEnabled_mods() {
            return enabled_mods;
        }

        public void setEnabled_mods(String enabled_mods) {
            this.enabled_mods = enabled_mods;
        }

        public String getMaxcombo() {
            return maxcombo;
        }

        public void setMaxcombo(String maxcombo) {
            this.maxcombo = maxcombo;
        }

        public String getPerfect() {
            return perfect;
        }

        public void setPerfect(String perfect) {
            this.perfect = perfect;
        }

        public String getPp() {
            return pp;
        }

        public void setPp(String pp) {
            this.pp = pp;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }


}

