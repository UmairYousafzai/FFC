package com.example.ffccloud.utils;

import android.util.Log;

import androidx.databinding.InverseMethod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Converter {

    @InverseMethod("stringToInt")
    public static String intToString(int value)
    {

        return String.valueOf(value);
    }

    public static int stringToInt(String value)
    {
        int  number=0;
       try
       {
           number= Integer.parseInt(value);

       }
       catch (Exception e)
       {
           Log.e("Conversion error: ",""+e.getMessage());
       }

       return number;

    }

    public static String StringToFormatDate(String date)
    {
        Date date1 = null;
        String formatDate="";
        try {
             date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dateFormat= new SimpleDateFormat("dd MMM yyyy");

        if (date1 != null) {
            formatDate= dateFormat.format(date1);
        }
        return formatDate;

    }

    public static String StringToFormatTime(String date)
    {
        Date date1 = null;
        String time="";
        try {
             date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dateFormat= new SimpleDateFormat("h:mm a");

        if (date1 != null) {
            time= dateFormat.format(date1);
        }
        return time;

    }
    public static String StringToFormatTimeTarget(String date)
    {

        Date date1 = null;
        String time="";

        if (date!=null)
        {
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat dateFormat= new SimpleDateFormat("h:mm a");

            if (date1 != null) {
                time= dateFormat.format(date1);
            }
        }

        return time;

    }

    public static String StringToFormatDateTarget(String date)
    {
        Date date1 = null;
        String formatDate="";
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy");

        if (date1 != null) {
            formatDate= dateFormat.format(date1);
        }
        return formatDate;

    }
}
