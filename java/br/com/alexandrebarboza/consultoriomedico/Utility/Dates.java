package br.com.alexandrebarboza.consultoriomedico.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexandre on 19/02/2017.
 */

public class Dates {

    private static String StringZeroFill(String field) {
        if (field.toString().length() == 1) {
            return "0" + field;
        }
        return field;
    }

    public static String DateToString(Date data, int size) {
        Locale locale = new Locale("pt", "BR");
        DateFormat format = DateFormat.getDateInstance(size, locale);
        String string = format.format(data);
        return string;
    }

    public static Date StringToDate(String str, String language, String country, boolean full) {
        Locale locale = new Locale(language, country);
        SimpleDateFormat format;
        Date result = null;
        if (full == true) {
            format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", locale);
        } else {
            format = new SimpleDateFormat("dd/MM/yyyy", locale);
        }
        try {
            result = (Date) format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String ShortDateFromString(String arg, String language, String country) {
        Locale locale = new Locale(language, country);
        DateFormat format1 = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", locale);
        DateFormat format2 = new SimpleDateFormat("dd/MM/yyyy", locale);
        Date date1         = null;
        Date date2         = null;
        String string1, string2 = null;
        try {
            date1 = (Date) format1.parse(arg);
            string1 = format2.format(date1);
            date2 = (Date) format2.parse(string1);
            string2 = format2.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return string2;
    }

    public static java.sql.Date getSQLDate(String data, boolean flag) {
        SimpleDateFormat sdf;
        if (flag == true) {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date parsed = null;
        try {
            parsed = sdf.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return new java.sql.Date(parsed.getTime());
    }

    public static java.util.Date convertFromDefaultDate(java.sql.Date sqlDate) {
        java.util.Date javaDate = null;
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return javaDate;
    }

    public static String getTime(String hora, String min) {
        hora = StringZeroFill(hora);
        min  = StringZeroFill(min);
        return hora + ":" + min + ":00";
    }
}
