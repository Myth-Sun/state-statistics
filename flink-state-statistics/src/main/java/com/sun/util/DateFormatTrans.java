package com.sun.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatTrans {
    public static String timestampToString(String timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(new Date(Long.parseLong(timestamp)));
        return str;
    }

    public static void main(String[] args) {
        String s = timestampToString("1511658000000");
        System.out.println(s.substring(0,7));
    }

}
