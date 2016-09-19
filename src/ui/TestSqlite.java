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

        String startTime = "2009-01-01-00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINA);
        try {
            Date date = sdf.parse(startTime);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            while (calendar.get(Calendar.YEAR)  != 2016){
                calendar.add(Calendar.MONTH, 1);
                printDate(calendar);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private static void printDate(GregorianCalendar calendar) {
//        System.out.println("year:" + calendar.get(Calendar.YEAR));
//        System.out.println("month:" + calendar.get(Calendar.MONTH));
//        System.out.println("day:" + calendar.get(Calendar.DAY_OF_MONTH));
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINA);
        String time = sdf.format(date);
        System.out.println("time:" + time);

    }


}
