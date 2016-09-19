package ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by bovink on 2016/9/18.
 */
public class TestSqlite {

    public static void main(String[] args) {
//        BeatmapTable.createTable();
//        BeatmapTable.insertBeatmap();
//        BeatmapTable.queryBeatmap(10000000);
//        BeatmapTable.deleteBeatmap(54899);

        String startTime = "2007-10-01-00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINA);
        try {
            Date date = sdf.parse(startTime);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            printDate(calendar);
            // 当不是2016年时或者是2016年但不是9月份时
            while (calendar.get(Calendar.YEAR) != 2016 ||  calendar.get(Calendar.MONTH) + 1 != 9) {
                calendar.add(Calendar.MONTH, 1);
                printDate(calendar);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private static void printDate(GregorianCalendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINA);
        String time = sdf.format(date);
        System.out.println("time:" + time);
    }


}
