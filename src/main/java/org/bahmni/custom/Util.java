package org.bahmni.custom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sandeepe on 02/03/16.
 */
public class Util {
    public static Date getFormattedDate(String dateStr) {
        return getFormattedDate(dateStr,"dd-MMM-yyyy");

    }

    public static Date getFormattedDate(String dateStr, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        try {
            return fmt.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }

    }

    public static Object getObjectFromClassName(String reportClass) {
        try {
            Class<?> clazz = Class.forName(reportClass);
            Object classInstance = clazz.newInstance();
            return classInstance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
